package secondTry;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class RunThisClass {

	public static void main(String args[]) throws NumberFormatException, IOException, InterruptedException{
		
		int myNodeId, myPortNo, clockVal, destNode, destPortNo, delayTime;
		String currEvent, destIpAdd, msg;
		ReadFromFile lists;
		Clock clock;
		HashMap<Integer, Socket> currentConn;
		Server server;
		Socket client;
		Relations relations; //Tree implementation
		boolean printOnce = true;
		
		DataOutputStream outToClient;
		
		//Get the node from the keyboard
		System.out.println("Enter the nodeId: ");
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));
		myNodeId = Integer.parseInt(kb.readLine());
		System.out.println("Node Id: "+myNodeId);
		kb.close();
		
		//Initializing info of all ipAddresses and ports of all other nodes
		lists = new ReadFromFile();
		//Initializing clock
		clock = new Clock();
		
		//Get my port no
		myPortNo = lists.getPortNo(myNodeId);
		System.out.println("\nMy port no.: "+myPortNo);
		
		//Populate lists according to node Id
		lists.scanEventFile(myNodeId);
		
		//HashMap to store incoming connections
		currentConn = new HashMap<Integer, Socket>();
		
		//Initializing the parent and child relations
		relations = new Relations();
				
		//Create server on this machine
		//System.out.println("Here");
		server = new Server(myNodeId, myPortNo, clock, relations, lists);
		server.start();
		Thread.sleep(1000*5);
		
		System.out.println("--------MAIN METHOD---------");
		//Main method to perform tasks according to the event file for this node
		while(true){
			//System.out.println("Inside the while(true) method.");
			clockVal = clock.getClockVal(); //Get current clock value
			
			if(lists.myEventsList.containsKey(clockVal)){
				//System.out.println("Current clock value: "+clockVal+" at node "+myNodeId);

				currEvent = lists.myEventsList.get(clockVal);
				
				
				if(currEvent.equals("INIT")){
					System.out.println("\n------------INIT EVENT--------------");
					System.out.println("Node "+myNodeId+" has started the termination detection.");
				
					relations.setParentNode(myNodeId); //Set parent to itself
					clock.tick(0, clockVal); //Increment clock value for the event
					System.out.println("After INIT event, updated clock value: "+clock.getClockVal()+" at node "+myNodeId);
					
					lists.myEventsList.remove(clockVal); //Remove the event from myEvents list
				}	
				else if(currEvent.equals("SEND")){
					System.out.println("\n------------SEND EVENT--------------");

					destNode = lists.sendList.get(clockVal);
					destIpAdd = lists.ipAddressList.get(destNode);
					destPortNo = lists.portList.get(destNode);
					
					//Get the socket for the receiver node if it exists in the hashMap
					if(relations.containsSocket(destNode))
						client = relations.getSocket(destNode);
					else{
						client = new Socket(destIpAdd, destPortNo);
						relations.addSocket(client.getInetAddress(), client);
					}
					
					clock.tick(0, clockVal); //Increment my clock value
					msg = "MSG "+myNodeId+" "+clock.getClockVal(); //Msg to send to the receiver
					outToClient = new DataOutputStream(client.getOutputStream());
					System.out.println("\nSending msg to node "+destNode);
					outToClient.writeBytes(msg+"\n");
					System.out.println("Sent msg: "+msg);
					
					System.out.println("After SEND event, updated clock value: "+clock.getClockVal()+" at node "+myNodeId);
													
					relations.addChild(destNode); //Add the receiver node as a child for this node
					System.out.println("*****List of children: ");
					relations.printChildren(); //Printing the children
					System.out.println("Added the reciever, node "+destNode+" as a child of this node "+myNodeId);
					lists.myEventsList.remove(clockVal); //Remove the event from myEvents list	
					lists.sendList.remove(clockVal); //Remove the even from the sendEvents list
				}	
				else if(currEvent.equals("TICK")){
					System.out.println("\n------------TICK EVENT--------------");

					delayTime = lists.tickList.get(clockVal);
					
					clock.tick(delayTime, clockVal); //Call the tick function as per the delayTime
					System.out.println("After TICK event, updated clock value: "+clock.getClockVal()+" at node "+myNodeId);
					
					lists.myEventsList.remove(clockVal); //Remove the event from myEvents list		
					lists.tickList.remove(clockVal); //Remove the even from the tickEvents list
				}	
				else if(currEvent.equals("IDLE")){
					
					while(true){
						//boolean printOnce = true;
						
						if(printOnce){
							System.out.println("Entered IDLE while loop");
							printOnce = false;
						}
						
						if(relations.hasChildren()){
							if(relations.getParentNode()!=myNodeId){
							System.out.println("\n------------IDLE EVENT--------------");
							
							clock.tick(0, clockVal); //Update clock for IDLE event
							System.out.println("After IDLE event, updated clock value: "+clock.getClockVal()+" at node "+myNodeId);
							
							System.out.println("Need to send the ACK msg to parentNode coz of the IDLE event.");
							clock.tick(0, clock.getClockVal()); //Update clock for IDLE event
							
							//client = relations.getParentConn();
							//Get the socket for the receiver node if it exists in the hashMap
							int parentNode = relations.getParentNode();
							System.out.println("Parent node is : "+parentNode);
							//if(relations.containsSocket(parentNode))
								//client = relations.getSocket(parentNode);
							//else{
							client = new Socket(lists.ipAddressList.get(parentNode), lists.portList.get(parentNode));
							//}
							
							System.out.println("\n********Parent connection: "+client);
							msg = "ACK "+myNodeId+" "+clock.getClockVal(); //ACK to send to the parentNode
							//inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
							outToClient = new DataOutputStream(client.getOutputStream());
							System.out.println("\nSending ACK msg to parentNode. Msg sent: "+msg);
							outToClient.writeBytes(msg+"\n");
							
							System.out.println("After sending ACK msg coz of the IDLE event, updated clock value: "+clock.getClockVal()+" at node "+myNodeId);
							
							//relations.setParentNode(-1);
							//if(relations.getParentNode()!=myNodeId)
								relations.setParentNode(-1); //Set parentNode to null
							}
							else
								System.out.println("\n\n**********SUCCESSFUL TERMINATION DETECTION***********");	
							lists.myEventsList.remove(clockVal); //Remove the event from myEvents list		
							break;
							
						}					
					}
				}		
				else{
						System.out.println("Unknown command");
				}
			}
		}
	}
}
