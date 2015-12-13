package ie.gmit.sw.server.queue;

import java.util.Set;

public interface OutQueue {

	int size();

	boolean isEmpty();

	boolean containsKey(String key);

	QueueMessage get(String key);

	QueueMessage put(String key, QueueMessage value);

	QueueMessage remove(String key);
	
	Set<String> keySet();

}