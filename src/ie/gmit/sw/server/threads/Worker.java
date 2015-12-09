package ie.gmit.sw.server.threads;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import ie.gmit.sw.breaker.VigenereBreaker;
import ie.gmit.sw.server.queue.InQueue;
import ie.gmit.sw.server.queue.OutQueue;
import ie.gmit.sw.server.queue.QueueMessage;

public class Worker implements Runnable {
	private QueueMessage message;
	private OutQueue outQueue;
	private String remoteHost;
	private InQueue inQueue;
	
	public Worker(QueueMessage mess, OutQueue out, InQueue in, String host){
		this.message = mess;
		this.outQueue = out;
		this.remoteHost = host;
		this.inQueue = in;
	}
	
	public Worker(OutQueue out, InQueue in, String host){
		this.outQueue = out;
		this.inQueue = in;
		this.remoteHost = host;
	}
	
	public QueueMessage getMessage() {
		return message;
	}


	public void setMessage(QueueMessage message) {
		this.message = message;
	}


	public OutQueue getOutQueue() {
		return outQueue;
	}


	public void setOutQueue(OutQueue outQueue) {
		this.outQueue = outQueue;
	}


	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	@Override
	public void run() {
		while(message == null){
			if(!inQueue.isEmpty()){
				message = inQueue.poll();
				VigenereBreaker vb;
				String result = null;
				try {
					vb = (VigenereBreaker)Naming.lookup("//" + remoteHost + "/cypher-service");
					result = vb.decrypt(message.getCypherText(), message.getMaxKeyLength());
					message.setCypherText(result);
					outQueue.put(message.getJobNumber(), message);
					message =  null;
				} catch (MalformedURLException | RemoteException | NotBoundException e) {
					e.printStackTrace();
				}
			}else{
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
