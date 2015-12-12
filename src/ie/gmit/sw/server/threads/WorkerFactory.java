package ie.gmit.sw.server.threads;

import ie.gmit.sw.server.queue.OutQueue;
import ie.gmit.sw.server.queue.QueueMessage;

public class WorkerFactory {
	private static WorkerFactory fac = new WorkerFactory();
	
	private WorkerFactory(){}
	
	public static WorkerFactory getInstance(){
		return fac;
	}
	
	public Runnable getWorker(WorkerType type, QueueMessage mess, OutQueue out, String host){
		Runnable runner = null;
		switch(type){
		case RMI:
			runner = new RMIWorker(mess, out, host);
			break;
		case CORBA:
			runner = new CORBAWorker(mess, out, host);
			break;
		}
		return runner;
	}
}
