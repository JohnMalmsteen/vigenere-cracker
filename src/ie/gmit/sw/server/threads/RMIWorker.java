package ie.gmit.sw.server.threads;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import ie.gmit.sw.breaker.VigenereBreaker;
import ie.gmit.sw.server.queue.InQueue;
import ie.gmit.sw.server.queue.OutQueue;
import ie.gmit.sw.server.queue.QueueMessage;

public class RMIWorker implements Runnable {
	private QueueMessage message;
	private OutQueue outQueue;
	private String remoteHost;
	
	public RMIWorker(QueueMessage mess, OutQueue out, String host){
		this.message = mess;
		this.outQueue = out;
		this.remoteHost = host;
	}
	
	public RMIWorker(OutQueue out, InQueue in, String host){
		this.outQueue = out;
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
		VigenereBreaker vb;
		String result = null;
		try {
			vb = (VigenereBreaker)Naming.lookup("//" + remoteHost + "/cypher-service");
			result = vb.decrypt(message.getCypherText(), message.getMaxKeyLength());
			message.setCypherText(result);
			outQueue.put(message.getJobNumber(), message);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
