package ie.gmit.sw.server.threads;

import ie.gmit.sw.server.queue.OutQueue;

public class AbandonedMessageCollector implements Runnable {

	private OutQueue out;
	
	public AbandonedMessageCollector(OutQueue out) {
		this.out = out;
	}

	@Override
	public void run() {

		for(String key : out.keySet()){
			if((System.currentTimeMillis() - out.get(key).getTimeCompleted()) > 30000){
				out.remove(key);
			}
		}
		
	}

}
