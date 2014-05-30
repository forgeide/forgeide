package ch.iserver.ace.util;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * Reference class used by the IsolatedThreadDomain to get notified whenever a
 * worker is no longer needed. That particular worker is then shut down.
 */
public class AsyncReference extends WeakReference {
	
	/**
	 * The worker for which this reference counts.
	 */
	private Worker worker;
		
	/**
	 * Creates a new AsyncReference referring to the given referent. The passed
	 * in worker is the Worker that needs to be shut down when the referent is
	 * only weakly-referenced.
	 * 
	 * @param referent the referenced object
	 * @param queue the reference queue
	 * @param worker the worker to be shut down
	 */
	public AsyncReference(Object referent, ReferenceQueue queue, Worker worker) {
		super(referent, queue);
		this.worker = worker;
	}
		
	/**
	 * @return the worker to be shut down by this application
	 */
	public Worker getWorker() {
		return worker;
	}
	
}
