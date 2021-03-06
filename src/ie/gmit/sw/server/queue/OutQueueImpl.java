package ie.gmit.sw.server.queue;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ie.gmit.sw.server.threads.AbandonedMessageCollector;

public class OutQueueImpl implements OutQueue {
	private Map<String, QueueMessage> outQueue =  new TreeMap<String, QueueMessage>();
	private AbandonedMessageCollector gc;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
	
	public OutQueueImpl(){
		executor.scheduleAtFixedRate(new AbandonedMessageCollector(this), 60, 60, TimeUnit.SECONDS);
	}
	
	public Set<String> keySet() {
		return outQueue.keySet();
	}

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
