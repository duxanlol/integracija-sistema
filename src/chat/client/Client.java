package chat.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.print.DocFlavor.URL;

import chat.server.Server;
import chat.util.ChatListener;

import chat.util.ChatServer;

public class Client extends UnicastRemoteObject implements ChatListener{

	private ChatServer server;
	private String nadimak;
	
	protected Client() throws RemoteException {
		super();
	}
	public static void main(String[] args) {
		try {
			Client client = new Client();
//			client.conectToServer(args); U realnom zadatku ovako
			String[] dummyArgs = {"127.0.0.1:1099"};
			client.connectToServer(dummyArgs);
			client.server.addListener(client);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			client.nadimak = br.readLine();
			while(true) {
				String poruka = br.readLine();
				if (poruka.equals("")) client.exit();
				client.server.sendMessage(client.nadimak, poruka);
			}						
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exit() throws RemoteException {
		server.removeListener(this);
		System.out.println("Goodbye!");
		System.exit(0);
	}
	
	private void connectToServer(String[] args) {
		/*
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		*/

		try {
			String URL = "//" + args[0] + "/chat";
			this.server = (ChatServer) Naming.lookup(URL);
		} catch (Exception e) {
			System.err.println("Error while locating the server: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}
	@Override
	public void receiveMessage(String name, String message) throws RemoteException {
		System.out.println("[" + name + "]says: " + message);
		
	}

}
