package notecoin.inventory.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.data.util.Pair;
import org.springframework.lang.Nullable;

import notecoin.inventory.domain.model.Instruction;

/**
 * @author camille.walim
 * 
 *"to update Inventory [...]"	
 */
public interface AbstractInventoryUpdater{
	
	@Deprecated // Mk-I feature (inventory non-event based)
	Instruction update(String name, int quantity);
	
	Instruction instruction(String name, OrderType type, int quantity, Date date, @Nullable Date until);
	
	/**
	 * a copy of the current positions -> none of the objects present there should be in common with the core one used for writing
	 * (to avoid deadlock issues)
	 */
	PositionMemory getPositionCopiedMap();
	
	public static enum OrderType{
		GIVE, TAKE, LEND, BORROW
	}

	public static class PositionMemory{
		
		private ReentrantLock  write = new ReentrantLock ();
		private ReentrantReadWriteLock replicate = new ReentrantReadWriteLock();  
		ExecutorService replicateExecutor = Executors.newSingleThreadExecutor(run -> new Thread(run, "replicatePositions"));
		
		private Map<String, TreeMap<Date, Pair<AtomicInteger,AtomicInteger>>> written  = new HashMap<>();
		private Map<String, TreeMap<Date, Pair<AtomicInteger,AtomicInteger>>> read = new HashMap<>();
		
		/**
		 * writing process : one by one synchronization from end to end (writing and replication in the read)
		 * reading replication : using a RRWL to lock the resource only during the replication process.
		 * expected behavior : 
		 * 	- writing process is synchronous (one write at a time), but focus on writing, so the replication process is render to another thread, and the status could be immediately returned to client
		 *  - reading process is multi-threaded in a safe way, as it would "catch-up" what the writing process is doing.
		 */
		void write(Consumer<Map<String, TreeMap<Date, Pair<AtomicInteger,AtomicInteger>>>> action){
			write.lock();
			action.accept(written);
			write.unlock(); // would return immediately
			replicateExecutor.execute(() ->{
				replicate.writeLock().lock();
				action.accept(read);
				replicate.writeLock().unlock();	
			});
		}
		/**
		 * could perform multiple reading at same time (as long as no writing.reading-replication process is not performed.
		 */
		<T> T reader(Function<Map<String, TreeMap<Date, Pair<AtomicInteger,AtomicInteger>>>, T> map){
			replicate.readLock().lock();
			T result =  map.apply(read);
			replicate.readLock().unlock();
			return result;
		}
	}
}
