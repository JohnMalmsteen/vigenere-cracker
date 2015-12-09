package ie.gmit.sw.breaker;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VigenereBreaker extends Remote {
	public String decrypt(String cypherText, int maxKeylength) throws RemoteException;
}
