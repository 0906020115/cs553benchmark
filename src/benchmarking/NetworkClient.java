package benchmarking;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkClient {
	 private static final int threadnum=2;
	 static int buffersize=0;
	 private static Socket clientsocket=null;
	public static void main(String[] args) {
		if(args[0].equals("tcp")){
			buffersize=Integer.parseInt(args[1]);	
			new NetworkClient().startuptcp();// TODO Auto-generated method stub
			}
			else if(args[0]=="udp"){
				
			}// TODO Auto-generated method stub

	}
	public void startuptcp(){
		try {
			clientsocket=new Socket("localhost", 8888);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}


