package chat.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatListener extends Remote{

	public void receiveMessage(String name, String message) throws RemoteException;


}
