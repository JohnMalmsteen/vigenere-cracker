package ie.gmit.sw.server.queue;

public interface InQueue {

	boolean add(QueueMessage e);

	boolean isEmpty();

	QueueMessage poll();

}