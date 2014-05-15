package secondTry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread{
		
	int myPortNo;
	int myNodeId;
	ServerSocket ss;// = new ServerSocket(9998);
	Socket listener;// = ss.accept();
	Clock clock;
	ThreadedClient client;
	Relations relations;
	ReadFromFile lists;
	//System.out.println("Connection is established");
		
	public Server(int myId, int myPortNo, Clock clock, Relations relations, ReadFromFile lists) {
		this.myNodeId = myId;
		this.myPortNo = myPortNo;
		this.clock = clock;
		this.relations = relations;
		this.lists = lists;
	}

	@Override
	public void run() {
		try {
			ss = new ServerSocket(myPortNo);
			System.out.println("\nServer has started for node "+myNodeId+".");
			System.out.println("Now listening for connections...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			try {
				listener = ss.accept();
				System.out.println("\nListener InetAddress: "+listener.getInetAddress());
				
				/*if(quorum.getConnSockets().containsKey(listener.getInetAddress())){
					System.out.println("****************This is a message from the child*****************************");
					//listener = relations.getConnSockets().get(listener.getInetAddress());
					System.out.println("Socket has been changed to the one from the hashmap");
				}
				*/
				
				System.out.println("Connection is established");
			} catch (IOException e) {
			// 	TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			if(listener != null){
				client = new ThreadedClient(myNodeId, myPortNo, listener, clock, relations, lists);
				System.out.println("Creating a new client thread and starting communication with it...");
				client.start();
			}
		}
	}
	
	public void serverShutDown() throws IOException{
		listener.close();
		ss.close();
	}
}