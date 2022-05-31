package chat.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
	
	public void sendMessage(String name, String message) throws RemoteException;
	
	public void addListener(ChatListener listener) throws RemoteException;
	
	public void removeListener(ChatListener listener) throws RemoteException;
}
