package benchmarking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.Random;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class NetworkClient {
	 private static final int threadnum=2;
	 static int buffersize=1;
	 private static Socket clientsocket=null;
	 private BufferedReader s2c;
	public static void main(String[] args) throws UnknownHostException, IOException {
		
		if(args[0].equals("tcp")){
			//buffersize=Integer.parseInt(args[1]);	
			new NetworkClient().startuptcp();// TODO Auto-generated method stub
			}
			else if(args[0]=="udp"){
				
			}// TODO Auto-generated method stub

	}
	public void startuptcp() throws UnknownHostException, IOException{
		int buffersize=1;
		int datasize=64*1024*10;
		int num=datasize/buffersize;
		byte toSend[] = new byte[buffersize];
		byte toReceive[] = new byte[buffersize];
		long start=0;long end=0;long dur=0;
		for (int i=0; i<buffersize; i++){
			toSend[i] = (byte)1;
		}
		
		Socket clientSocket=new Socket("localhost",8081);
		start=System.currentTimeMillis();
		for(int i=0;i<num;i++){
			DataOutputStream OutToServer=new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream inFromServer=new DataInputStream(clientSocket.getInputStream());
			OutToServer.write(toSend, 0, buffersize);
			OutToServer.flush();
			inFromServer.read(toReceive);
		}
		end=System.currentTimeMillis();
		clientSocket.close();
		dur=end-start;
		float latency=2*datasize/dur;
		System.out.println("The Time is:"+dur);
		System.out.println("The Latency is:"+latency);
		float throuput=2*datasize/1024*1024;
		throuput=throuput/dur;
		throuput=throuput*1000;
		System.out.println("The Throughput is"+throuput);
		
		
		
	}
	public void startupudp(){
		try {
			DatagramSocket clientSocket=new DatagramSocket();
			InetAddress ipAddress=InetAddress.getByName("localhost");
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String randomString(int length) {
		Random randGen=null;
		char[] numbersAndLetters=null;
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
               randGen = new Random();
               numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
                  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();             
                }
        char [] randBuffer = new char[length];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
}
	
	
	
	
	

}


