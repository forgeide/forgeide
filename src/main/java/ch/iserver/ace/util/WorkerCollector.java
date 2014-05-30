package ch.iserver.ace.util;

import java.lang.ref.ReferenceQueue;

import org.jboss.logging.Logger;

public class WorkerCollector implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(WorkerCollector.class);
	
	private final ReferenceQueue queue;
	
	public WorkerCollector(ReferenceQueue queue) {
		ParameterValidator.notNull("queue", queue);
		this.queue = queue;
	}
	
	public void run() {
		LOG.info("running worker collector ...");
		AsyncReference ref = (AsyncReference) queue.poll();
		while (ref != null) {
			Worker worker = ref.getWorker();
			worker.kill();
			ref = (AsyncReference) queue.poll();
		}
	}
	
}
