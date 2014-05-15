package secondTry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class ReadFromFile {

	HashMap<Integer, String> ipAddressList;
	HashMap<Integer, Integer> portList;
	HashMap<Integer, String> myEventsList;
	HashMap<Integer, Integer> sendList;
	HashMap<Integer, Integer> tickList;
	int lineCount=0;
	
	public ReadFromFile() throws IOException, FileNotFoundException{
		String sCurrentLine;
		
		ipAddressList = new HashMap<Integer, String>(); 
		BufferedReader br1 = new BufferedReader(new FileReader("./secondTry/ipAddresses.txt"));
		
		while ((sCurrentLine = br1.readLine()) != null) {
			String words[] = sCurrentLine.split(" ");
			ipAddressList.put(Integer.parseInt(words[0]), words[1]);
		}
		br1.close();
		
		portList = new HashMap<Integer, Integer>();
		
		BufferedReader br2 = new BufferedReader(new FileReader("./secondTry/ports.txt")); //./secondTry/
		while ((sCurrentLine = br2.readLine()) != null) {
			String words[] = sCurrentLine.split(" ");
			portList.put(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
		}
		br2.close();
		
		printIpAddressList();
		printPortList();
	}
	
	//Method to scan event file and store details of events according nodeId
	public void scanEventFile(int myNodeId) throws IOException{
		myEventsList = new HashMap<Integer, String>();
		sendList = new HashMap<Integer, Integer>();
		tickList = new HashMap<Integer, Integer>();
				
		String sCurrentLine;
		BufferedReader br3 = new BufferedReader(new FileReader("./secondTry/eventFile.txt")); //C:\\Users\\Siddharth Sakhadeo\\Google Drive\\workspace\\AOS\\src\\secondTry\\
		
		while ((sCurrentLine = br3.readLine()) != null) {
			//System.out.println(sCurrentLine);
			
			String words[] = sCurrentLine.split(" ");
			
			//System.out.println("My Node Id: "+myNodeId);
			if(lineCount>=1){
				int nodeId = Integer.parseInt(words[0]); //System.out.println("Node Id Read: "+nodeId);
				int clockTime = Integer.parseInt(words[1]); //System.out.println("Clock Time Read: "+clockTime);
				
				if(nodeId == myNodeId){
					myEventsList.put(clockTime, words[2]);
					
					if(words[2].equals("SEND")){
						int receiver = Integer.parseInt(words[3]);
						sendList.put(clockTime, receiver);
					}
					if(words[2].equals("TICK")){
						int delayTime = Integer.parseInt(words[3]);
						tickList.put(clockTime, delayTime);
					}
				}				
			}
			lineCount++;
		}
		
		//Print hashMaps to check
		printMyEvents();
		printSendEvents();
	    printTickEvents();    
	       
		br3.close();
	}

	public int getPortNo(int nodeId){
		int portNo = portList.get(nodeId);
		return portNo;
	}
	
	public void printIpAddressList(){
		System.out.println("---------IP ADDRESS LIST----------");
		Iterator<Integer> i = ipAddressList.keySet().iterator();
		while(i.hasNext())
	    {
			Integer keyval = (Integer) i.next();
	        String e = ipAddressList.get(keyval);
	        System.out.println(keyval+" " + e);
	    }
	}
	
	public void printPortList(){
		System.out.println("---------PORT LIST----------");
		Iterator<Integer> i = portList.keySet().iterator();
		while(i.hasNext())
	    {
			Integer keyval = (Integer) i.next();
	        Integer e = portList.get(keyval);
	        System.out.println(keyval+" "+ e);
	    }
	}
	
	public void printMyEvents() {
		System.out.println("---------MY EVENTS----------");
		Iterator<Integer> i = myEventsList.keySet().iterator();
		while(i.hasNext())
	    {
			Integer keyval = (Integer) i.next();
	        String e = myEventsList.get(keyval);
	        System.out.println(keyval+" "+ e);
	    }
	}
	
	public void printSendEvents(){
		System.out.println("---------SEND EVENTS----------");
        Iterator<Integer> j = sendList.keySet().iterator();
        while(j.hasNext())
        {
            //System.out.println("In Send");
            Integer keyval = (Integer) j.next();
            Integer dest = sendList.get(keyval);
            System.out.println(keyval+" "+ dest);            
        }
	}
	
	public void printTickEvents(){
		System.out.println("---------TICK EVENTS----------");
	    Iterator<Integer> k = tickList.keySet().iterator();
	    while(k.hasNext())
	    {
	    	Integer keyval = (Integer) k.next();
	        Integer del = tickList.get(keyval);
	        System.out.println(keyval+"  "+ del);
	    }
	}
}
