package ie.gmit.sw.server.queue;

import java.util.LinkedList;
import java.util.Queue;

public class InQueueImpl implements InQueue {
	private Queue<QueueMessage> inQueue = new LinkedList<QueueMessage>();

	public boolean add(QueueMessage e) {
		return inQueue.add(e);
	}

	public boolean isEmpty() {
		return inQueue.isEmpty();
	}
	
	public QueueMessage poll() {
		return inQueue.poll();
	}
	
}
