
package benchmarking;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class NetworkServer {
	 private static ServerSocket ss=null;
	
	  static int buffersize=0;
	/**
	 * @param args
	 * @throws SocketException 
	 */
	public static void main(String[] args) throws SocketException {
		int i=1;int thread=1;
		if(args[1].equals("1B"))i=1;
		if(args[1].equals("1KB"))i=1024;
		if(args[1].equals("64KB"))i=1024*63;
		if(args[2].equals("1")) thread=1;
		if(args[2].equals("2")) thread=2;
		if(args[0].equals("tcp")){
			
		new NetworkServer().starttcp(i,thread);// TODO Auto-generated method stub
		}
		if(args[0].equals("udp")){
		
		new NetworkServer().startudp(i,thread);
		
		}

	}
	public void starttcp(int size,int th){
		int threadnum=th;
		
		try {
			ss=new ServerSocket(8081);
			Thread[] threads=new Thread[threadnum];	
				for(int i=0;i<threadnum;i++){
					 Socket socket=ss.accept();
					 TCPThread tcpthread =new TCPThread(socket,size);
					threads[i]=new Thread(tcpthread);
					threads[i].start();
			}
				for(int i=0;i<threadnum;i++){
					threads[i].join();
				}
		} catch (IOException e) {
			System.out.println("excpetion");// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void startudp(int size,int th) throws SocketException {
		int threadnum=th;
		try {
			
			DatagramSocket	socket = new DatagramSocket(8082);
			
			
			Thread[] threads=new Thread[threadnum];	
				for(int i=0;i<threadnum;i++){
					UDPThread udpThread=new UDPThread(socket,size);
					
					threads[i]=new Thread(udpThread);
					threads[i].start();
			}
				for(int i=0;i<threadnum;i++){
					threads[i].join();
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
	}

}
class TCPThread implements Runnable{
	private DataInputStream infromclient=null;
	private DataOutputStream outtoclient = null;
	private Socket clientsocket;
	private int size;
	public TCPThread(Socket clientSocket, int size){
		this.clientsocket=clientSocket;
		this.size=size;
	}

	@Override
	public void run() {
		int buffersize=size;
		double datasize=30*buffersize*1024;
		byte toSend[] = new byte[buffersize];
		byte toReceive[] = new byte[buffersize];
		Socket socket=clientsocket;
		for (int i=0; i<buffersize; i++){
			toSend[i] = (byte)1;
		}
		try {
			
			for(long i=0;i<datasize;){
				infromclient = new DataInputStream(socket.getInputStream());
				outtoclient = new DataOutputStream(socket.getOutputStream());
					
					outtoclient.write(toSend, 0, buffersize);
					infromclient.read(toReceive);
					outtoclient.flush();
					outtoclient.flush();
					i+= buffersize;
					
					//System.out.println(Thread.currentThread().getName());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
class UDPThread implements Runnable{
	private DatagramSocket  socket;
	private int size;
	public UDPThread(DatagramSocket socket,int size) {
		this.socket=socket;
		this.size=size;
	}
	@Override
	public void run() {
		
		int buffersize=size;
		double datasize=30*buffersize*1024;
		byte toSend[] = new byte[buffersize];
		byte toReceive[] = new byte[buffersize];
		
		
		
		for (int i=0; i<buffersize; i++){
			toSend[i] = (byte)2;
		}// TODO Auto-generated method stub
		DatagramPacket recivePacket;
		DatagramPacket sendPacket;
		InetAddress ipAddress;
		int port;
		for(int i=0;i<datasize;){
			recivePacket=new DatagramPacket(toReceive, buffersize);			
			try {
				socket.receive(recivePacket);				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			toReceive=recivePacket.getData();
			ipAddress=recivePacket.getAddress();
			port=recivePacket.getPort();
			
			sendPacket=new DatagramPacket(toSend, buffersize,ipAddress,port);
			
			try {
				socket.send(sendPacket);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i=i+buffersize;
			
			
		}
	}
	
}
