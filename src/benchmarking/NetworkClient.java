package benchmarking;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class NetworkClient {
	 
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		int i=1;int thread=1;
		if(args[1].equals("1B"))i=1;
		if(args[1].equals("1KB"))i=1024;
		if(args[1].equals("64KB"))i=1024*63;
		if(args[2].equals("1")) thread=1;
		if(args[2].equals("2")) thread=2;
		if(args[0].equals("tcp")){
			//buffersize=Integer.parseInt(args[1]);	
			new NetworkClient().startuptcp(i,thread);// TODO Auto-generated method stub
			}
			else if(args[0].equals("udp")){
			new NetworkClient().startupudp(i,thread);	
			
			}// TODO Auto-generated method stub

	}
	public void startuptcp(int size,int th) throws UnknownHostException, IOException, InterruptedException{
		int threadnum=th;
		Thread[] threads=new Thread[threadnum];	
		for(int i=0;i<threadnum;i++){
			 TCPClientThread tcpClientThread=new TCPClientThread(size);
			threads[i]=new Thread(tcpClientThread);
			threads[i].start();
	}
		for(int i=0;i<threadnum;i++){
			threads[i].join();
		}
	}
	public void startupudp(int size,int th){
		int threadnum=th;
		try {
			Thread[] threads=new Thread[threadnum];	
			for(int i=0;i<threadnum;i++){
				UDPClientThread udpClientThread=new UDPClientThread(size);
				threads[i]=new Thread(udpClientThread);
				threads[i].start();
				System.out.println("aaa");
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
class TCPClientThread implements Runnable{
	private int size;
	public TCPClientThread(int size){
		this.size=size;
	}
	@Override
	public void run() {
		int buffersize=size;
		double datasize=30*buffersize*1024;
		int num=(int)datasize/buffersize;
		byte toSend[] = new byte[buffersize];
		byte toReceive[] = new byte[buffersize];
		long start=0;long end=0;long dur=0;
		for (int i=0; i<buffersize; i++){
			toSend[i] = (byte)1;
		}
		
		try {
			Socket clientSocket=new Socket("localhost",8081);
			start=System.currentTimeMillis();
			
			for(int i=0;i<num;i++){
				DataOutputStream OutToServer=new DataOutputStream(clientSocket.getOutputStream());
				DataInputStream inFromServer=new DataInputStream(clientSocket.getInputStream());
				
				OutToServer.write(toSend, 0, buffersize);
				OutToServer.flush();
				
				//System.out.println(i);
				inFromServer.read(toReceive);
				//System.out.println("test");
			}
			
			clientSocket.close();
			end=System.currentTimeMillis();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dur=end-start;
		float latency=(float) (0.5*(float)dur/(float)datasize);
		System.out.println("The Time is:"+dur);
		if(buffersize==1){
			System.out.println("The Latency is:"+latency+"ms");
		}
		double throuput=2000*(double)datasize/(double)(1024*1024);
		throuput=throuput/(dur);
		
		System.out.println("The Throughput is"+throuput+"M/s");
		
	}
	
}
class UDPClientThread implements Runnable{
	private int size;
	public UDPClientThread(int size){
		this.size=size;
	}
	@Override
	public void run() {
		int buffersize=size;
		double datasize=30*buffersize*1024;
		System.out.println(datasize+"");
		int num=(int)datasize/buffersize;
		byte toSend[] = new byte[buffersize];
		byte toReceive[] = new byte[buffersize];		
		for (int i=0; i<buffersize; i++){
			toSend[i] = (byte)2;
		}
		long start=System.currentTimeMillis();// TODO Auto-generated method stub
		try {
			DatagramSocket clientSocket=new DatagramSocket();
			InetAddress ipaddress=InetAddress.getByName("localhost");
			
			for(int i=0;i<num;i++){
				
				DatagramPacket sendPacket=new DatagramPacket(toSend, buffersize,ipaddress,8082);
				clientSocket.send(sendPacket);
				DatagramPacket receivePacket=new DatagramPacket(toReceive, buffersize);
				
				clientSocket.receive(receivePacket);
				//System.out.println(i);
			}
			
			clientSocket.close();
			
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end=System.currentTimeMillis();
		long dur=end-start;
		float latency=(float) (0.5*(float)dur/(float)datasize);
		System.out.println("The Time is:"+dur);
		if(buffersize==1){
			System.out.println("The Latency is:"+latency+"ms");
		}
		double throuput=4000*(double)datasize/(double)(1024*1024);
		throuput=throuput/(dur);
		System.out.println("The Throughput is"+throuput+"M/s");
		
	}
	
}

