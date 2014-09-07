package benchmarking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkClient {
	 private static final int threadnum=2;
	 static int buffersize=1;
	 private static Socket clientsocket=null;
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		if(args[0].equals("tcp")){
			//buffersize=Integer.parseInt(args[1]);	
			new NetworkClient().startuptcp();// TODO Auto-generated method stub
			}
			else if(args[0]=="udp"){
				
			}// TODO Auto-generated method stub
		long end=System.currentTimeMillis();
		String dur=end-start+"";
		System.out.println("time:"+dur);
	}
	public void startuptcp(){
		try {
			clientsocket=new Socket("localhost", 8885);
			//BufferedReader infromuser=new BufferedReader(new InputStreamReader(System.in),buffersize);
			DataOutputStream outtoserver = new DataOutputStream(clientsocket.getOutputStream());
			outtoserver.writeByte(65511);
			clientsocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}


