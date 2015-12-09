package ie.gmit.sw.server.queue;
import java.util.*;

public class OutQueueImpl implements OutQueue {
	private Map<String, QueueMessage> outQueue =  new TreeMap<String, QueueMessage>();

	
	public int size() {
		return outQueue.size();
	}

	public boolean isEmpty() {
		return outQueue.isEmpty();
	}

	public boolean containsKey(String key) {
		return outQueue.containsKey(key);
	}

	public QueueMessage get(String key) {
		return outQueue.get(key);
	}

	public QueueMessage put(String key, QueueMessage value) {
		return outQueue.put(key, value);
	}

	public QueueMessage remove(String key) {
		return outQueue.remove(key);
	}
	
}
