package chat.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;

import chat.util.ChatListener;
import chat.util.ChatServer;

public class Server extends UnicastRemoteObject implements ChatServer{

	private List<ChatListener> remoteObservers;
	private CensorDOM censor;
	
	protected Server() throws RemoteException {
		super();
		remoteObservers = new ArrayList<ChatListener>();	
	}
	
	public static void main(String[] args) throws RemoteException {
		/*
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		*/
		
		LocateRegistry.createRegistry(1099);		
		String name = "//127.0.0.1:1099/chat";
		System.out.println("[DEBUG] Server name: " + name);
		try {
			Server server = new Server();
			Naming.rebind(name, server);
	        server.censor = new CensorDOM();
		} catch (RemoteException re) {
			System.err.println("Collector RemoteException: " + re.getMessage());
			re.printStackTrace();
			System.exit(-1);
		} catch (MalformedURLException murle) {
			System.err.println("Collector MalformedURLException: " + murle.getMessage());
			murle.printStackTrace();
			System.exit(-1);
		}	
	}
	
	private String censor(String message) {
		Set<String> naughtyWords = censor.getWords(); 
		String[] tokens = message.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			if (naughtyWords.contains(tokens[i].toLowerCase())) {				
				tokens[i] = "*****";
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < tokens.length; i++) {
			sb.append(tokens[i] + " " );
		}
		return sb.toString();
	}
	
	public String parseCommand(String name, String message) throws RemoteException {
		if (message.startsWith("/+/")){
			message = message.replace("/+/", "");
			censor.addRec(message, name);
			sendMessage("SERVER", "USPESNO DODATA REC");
			return null;
		}else if (message.startsWith("/-/")){
			message = message.replace("/-/", ""); //Treba replaceFirst ili nesto al da.
			boolean res = censor.removeRec(message, name);
			if(res) {
				sendMessage("SERVER", "USPESNO OBRISANA REC"); //Nije po specifikaciji. Trebalo bi da obavestim samo Posiljaoca komande.
			}else {
				sendMessage("SERVER", "NIJE USPELO BRISANJE");
			}
			return null;
		}else {
			return message;
		}			
	}
	
	public void sendMessage(String name, String message) throws RemoteException {
		message = parseCommand(name, message);
		if (message == null) return;
		message = censor(message);
		for(ChatListener observer: remoteObservers) {		
			observer.receiveMessage(name, message);
		}		
	}	
	
	public void addListener(ChatListener listener) throws RemoteException {
		remoteObservers.add(listener);
	}

	public void removeListener(ChatListener listener) throws RemoteException {	
		remoteObservers.remove(listener);		
	}

}
