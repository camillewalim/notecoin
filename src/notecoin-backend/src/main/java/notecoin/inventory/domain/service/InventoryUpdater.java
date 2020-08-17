package notecoin.inventory.domain.service;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Pair;

import lombok.AllArgsConstructor;
import notecoin.inventory.domain.model.Instruction;
import notecoin.inventory.domain.model.Instruction.Type;
import notecoin.inventory.domain.model.product.Product;
import notecoin.inventory.infra.data.InstructionRepository;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
@AllArgsConstructor
public class InventoryUpdater implements AbstractInventoryUpdater{
	
	private InstructionRepository positionsDao;
	private JpaRepository<Product, String> productDao;
	
	//		Product,	per Delivery Date, position: {given, receivables}
	private Map<String, TreeMap<Date, Pair<AtomicInteger,AtomicInteger>>> positions; 
	
	@Override
	@Deprecated // Mk-I feature (inventory non-event based)
	public Instruction update(String name, int quantity) {
		return positionsDao
			.findByProduct(new Product(name))
			.map(iq -> {
				iq.setQuantity(quantity);
				return iq;
			})
			.orElseThrow(() -> new IllegalArgumentException("Feature not implemented"));
	}

	@Override
	public Instruction instruction(String name, AbstractInventoryUpdater.OrderType type, int quantity, Date date, Date maturity) {
		// Guards
		if(new Date().getTime() > date.getTime())
			throw new IllegalArgumentException("Retroactive instruction not allowed");
		if(maturity!=null && maturity.getTime() < date.getTime())
			throw new IllegalArgumentException("maturity should be bigger than instruction.date");
		if((type==OrderType.BORROW || type==OrderType.LEND )&& maturity==null)
			throw new IllegalArgumentException("borrow/lend should have a maturity");
		
		Type itype = 
				type==OrderType.GIVE ? Type.given
			:	type==OrderType.TAKE ? Type.taken
			:	type==OrderType.LEND ? Type.receivable
			:	Type.payable;
		
		Instruction result = new Instruction(quantity, new Product(name), itype, date, maturity);
		instructionLoad(result);
		positionsDao.saveAndFlush(result);
		
		return result;
	}
	
	public void instructionLoad(Instruction i) {
		String name= i.getProduct().getName();
		int quantity= i.getQuantity();
		Date date = i.getDate();
		Type type= i.getType();
		boolean isLongDelta = type==Type.given || type==Type.receivable;
		boolean isLendAnBorrow = type==Type.payable || type==Type.receivable;
		Date maturity = isLendAnBorrow ? i.getMaturity() : null;
		
		// Find the product
		TreeMap<Date, Pair<AtomicInteger,AtomicInteger>> positionsByDate = 
			Optional.ofNullable(positions.get(name))
					.orElseGet(()->productDao	
						.findById(name)
						.map(p -> {
							TreeMap<Date, Pair<AtomicInteger,AtomicInteger>> map= new TreeMap<>(); 
							map.put(new Date(0L), Pair.of(new AtomicInteger(), new AtomicInteger()));
							positions.put(name, map);
							return map;
						})
						.orElseThrow(() -> new IllegalArgumentException("This product do not exist in inventory"))
					);
		
		// Find the date-range
		 Stream	
			.of(Optional.of(date), Optional.ofNullable(maturity))
			.filter(Optional::isPresent).map(Optional::get)
			.forEach(d ->Optional 
				.ofNullable(positionsByDate.floorEntry(d).getValue())
				.ifPresent(pair ->positionsByDate.put(d, Pair.of(
					new AtomicInteger(pair.getFirst().intValue()), 
					new AtomicInteger(pair.getSecond().intValue())))
				));
		Iterator<Entry<Date, Pair<AtomicInteger,AtomicInteger>>> dates = positionsByDate.tailMap(date).entrySet().iterator();
		
		// Iterating through date ranges and updating points
		Consumer<Entry<Date, Pair<AtomicInteger,AtomicInteger>>> update = next -> updatePosition(isLongDelta, isLendAnBorrow, next.getValue(), quantity);
		if(maturity==null)			// update until the end
			while(dates.hasNext())
				update.accept(dates.next());
		else						// update until the maturity
			while(dates.hasNext()) {	
				Entry<Date, Pair<AtomicInteger,AtomicInteger>> next = dates.next();
				if(next.getKey().getTime() < maturity.getTime())
					update.accept(next);
				else break;
			}

	}
	
	private static Pair<AtomicInteger,AtomicInteger> updatePosition(
		boolean isLongDelta, boolean isLendAnBorrow, 
		Pair<AtomicInteger,AtomicInteger> positions, Integer delta) {
		
		AtomicInteger current = isLendAnBorrow ? positions.getSecond() : positions.getFirst() ;
		
		if(isLongDelta)	current.getAndAdd(delta);
		else current.getAndAccumulate(delta, (c, d) -> {
				if(c < d)
					if(isLendAnBorrow) {
						updatePosition(isLongDelta, false, positions, d-c); //2nd chance for borrowing money -> if no position in lending -> go in stock
						return 0;
					}else
						throw new IllegalArgumentException("short selling");
				return c - d;
			}
		);
		return positions;
	}
	
	public Map<String, TreeMap<Date, Pair<AtomicInteger,AtomicInteger>>> getPositionCopiedMap(){
		throw new RuntimeException("not implementd");
	}
}
