package ie.gmit.sw.server.threads;

import org.omg.CosNaming.*;

import ie.gmit.sw.breaker.CORBAVigenereBreaker;
import ie.gmit.sw.breaker.CORBAVigenereBreakerHelper;
import ie.gmit.sw.server.queue.OutQueue;
import ie.gmit.sw.server.queue.QueueMessage;

import java.util.Properties;

import org.omg.CORBA.*;

public class CORBAWorker implements Runnable {

	private QueueMessage message;
	private OutQueue outQueue;
	private String remoteHost;
	
	public CORBAWorker(QueueMessage mess, OutQueue out, String host){
		this.message = mess;
		this.outQueue = out;
		this.remoteHost = host;
	}
	@Override
	public void run() {
		CORBAVigenereBreaker vigenereImpl = null;
		String result = null;
		try{
			Properties aProperties = new Properties();
			aProperties.put("org.omg.CORBA.ORBInitialHost", this.remoteHost);
			aProperties.put("org.omg.CORBA.ORBInitialPort", "1050");
			ORB orb = ORB.init((String[]) null, aProperties);
			//Get a handle on the CORBA Naming Service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			
			//Use NamingContextExt instead of NamingContext. This is part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			//Resolve the Object Reference in Naming
			String name = "vigenere-service";
			vigenereImpl = CORBAVigenereBreakerHelper.narrow(ncRef.resolve_str(name));  //We also downcast the Naming.lookup in RMI
			result = vigenereImpl.decrypt(message.getCypherText(), message.getMaxKeyLength());
			message.setCypherText(result);
			outQueue.put(message.getJobNumber(), message);
		}catch (Exception e) {
			System.out.println("ERROR : " + e) ;
			e.printStackTrace(System.out);
		}
	}

}
