package secondTry;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Relations {

	private int parentNode;
	private List<Integer> children;
	private Socket parentConn;
	private HashMap<InetAddress, Socket> connSockets;
	
	public Relations(){
		setParentNode(-1);
		children = new ArrayList<Integer>();
		connSockets = new HashMap<InetAddress, Socket>();
	}
	
	public void printChildren(){
		if(!hasChildren()){
			Iterator<Integer> iterator = children.iterator();
			while(iterator.hasNext())
				System.out.print(iterator.next()+" ");
		}
		else
			System.out.println("No children to iterate over");
	}
	
	public void addSocket(InetAddress nodeIp, Socket socket){
		getConnSockets().put(nodeIp, socket);
	}
	
	public boolean containsSocket(int nodeId){
		return getConnSockets().containsKey(nodeId);
	}
	
	public Socket getSocket(int nodeId){
		return getConnSockets().get(nodeId);
	}
	
	public void addChild(int nodeId){
		System.out.println("\n*****Node received to be added as a child: "+ nodeId);
		boolean childAdded = children.add(nodeId);
		System.out.println(childAdded);
	}
	
	public void removeChild(int nodeId){
		if(children.contains(nodeId)){
			children.remove(children.indexOf(nodeId));
			System.out.println("Node "+nodeId+" has been removed from the children");
		}
		else
			System.out.println("-------------No child to remove-------------");
		
		if(hasChildren())
			System.out.println("-----------NODE HAS NO CHILDREN ----------------");
			
	}
	
	public boolean hasChildren(){
		return children.isEmpty();
	}
	
	//Generated getters and setters
	public int getParentNode() {
		return parentNode;
	}
	public void setParentNode(int parentNode) {
		this.parentNode = parentNode;
	}
	public List<Integer> getChildren() {
		return children;
	}
	public void setChildren(List<Integer> children) {
		this.children = children;
	}

	public Socket getParentConn() {
		return parentConn;
	}

	public void setParentConn(Socket parentConn) {
		this.parentConn = parentConn;
		System.out.println("******Parent connection socket to: "+parentConn.getInetAddress());
	}

	public HashMap<InetAddress, Socket> getConnSockets() {
		return connSockets;
	}

	public void setConnSockets(HashMap<InetAddress, Socket> connSockets) {
		this.connSockets = connSockets;
	}
	
}
