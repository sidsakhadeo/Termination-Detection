package secondTry;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadedClient extends Thread{
	int myNodeId, myPortNo;
	protected Socket clientSocket;
	Clock clock;
	ReadFromFile lists;
	InputStream inp = null;
   	BufferedReader inFromClient = null;
    DataOutputStream outToClient = null;
    Relations relations;
    boolean check;
	
	public ThreadedClient(int myNodeId, int myPortNo, Socket socket, Clock clock, Relations relations, ReadFromFile lists){
		this.myNodeId = myNodeId;
		this.myPortNo = myPortNo;
		clientSocket = socket;
		this.clock = clock;
		this.relations = relations;
		this.lists = lists;
		System.out.println("\nNew ThreadedClient has been started. Now run method will start");
		check = false;
	}
	
	@Override
	public synchronized void run(){
		//System.out.println("Run method");  
	    while(true){
		//System.out.println("while loop");
	    	try {
			//System.out.println("try block");
	            inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	            outToClient = new DataOutputStream(clientSocket.getOutputStream());
	            //System.out.println("Before reading line");

	            String msgFromClient = inFromClient.readLine();
	            //problem here
	            if(msgFromClient != null){
	            	System.out.println("\n-----------RECEIVE EVENT------------");	            		         
	            	
	            	System.out.println("Msg received: "+msgFromClient);
	            	String msgDetails[] = msgFromClient.split(" ");
	            	
	            	int connNode = Integer.parseInt(msgDetails[1]);
	            	int clockTime = Integer.parseInt(msgDetails[2]);
	            	
	            	try {
            			clock.tick(0, clockTime);
            			System.out.println("After RECV event, updated clock value: "+clock.getClockVal()+" at node "+myNodeId);
            		} catch (InterruptedException e) {
					// 	TODO Auto-generated catch block
            			e.printStackTrace();
            		}
	            	
	            	
	            	//If it is a SEND message and the parentNode is not set
	            	if(msgDetails[0].equals("MSG")){
	            		//Check if parentNode is set
	            		if(relations.getParentNode()==-1){
	            			relations.setParentNode(connNode); //Set parentNode
		            		relations.setParentConn(clientSocket); //Set parent connection	     
	            		}
	            		//If parentNode is already set, send ACK back
	            		else{	   
	            			//trying to create a new socket to send the ACK back
	            			clientSocket = new Socket(lists.ipAddressList.get(connNode), lists.portList.get(connNode));
	            			inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        	            outToClient = new DataOutputStream(clientSocket.getOutputStream());
	        	            
	            			System.out.println("Node is not free. Need to send ACK. Updating clock.");
	          	          
		            		try {
								clock.tick(0, clock.getClockVal()); //Increment my clock value							
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} 
		            		
		            		String ackMsg = "ACK "+myNodeId+" "+clock.getClockVal();	            			            			            	
		            		outToClient.writeBytes(ackMsg+"\n");
		            		System.out.println("Sent msg: "+ackMsg);
		            		System.out.println("After sending the ACK msg, updated clock value: "+clock.getClockVal()+" at node "+myNodeId);
		            		//Close thread here		 
		            		break;
	            		}
	            	}
	            	//If it is an ACK msg
	            	else if(msgDetails[0].equals("ACK")){
	            		System.out.println("Received ACK msg.");
	            		/*try {
		            		System.out.println("RECV event. Updating clock.");
	            			clock.tick(0, clockTime);            			
	            			System.out.println("Updated clock value: "+clock.getClockVal()+" at node "+myNodeId);
	            		} catch (InterruptedException e) {
						// 	TODO Auto-generated catch block
	            			e.printStackTrace();
	            		}*/
	            		     		
	            		relations.removeChild(connNode);	 
	            	}
	           
	            	//If it is an ACK message
	            	/*if(msgDetails[0].equals("ACK")){
	            		
	            	}*/
	            	
	            	//System.out.println("Clock value: "+clock.getClockVal());
	            	//inFromClient.close();
	            	//outToClient.close();
	           }  
	            else{
	            	if(!check){
	            		check = true;
	            		System.out.println("Recieved a null message from the sender");
	            	}	            			         
	            	continue;
	            }
	        	} catch (IOException e) {
	        		//return;
	        }
		
	    }
	}
}
