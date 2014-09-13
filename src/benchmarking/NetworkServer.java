
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
	 private static boolean serverlive=false;
	 private static final int threadnum=1;
	  static int buffersize=0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		if(args[0].equals("tcp")){
		buffersize=Integer.parseInt(args[1]);	
		System.out.println(buffersize);
		
		new NetworkServer().starttcp();// TODO Auto-generated method stub
		}
		else if(args[0]=="udp"){
			
		}
		long end=System.currentTimeMillis();
		String dur=end-start+"";
		System.out.println(dur);
	}
	public void starttcp(){
		try {
			ss=new ServerSocket(7766);
			serverlive=true;
			Thread[] threads=new Thread[threadnum];
			while(serverlive){
				 
				Socket socket=ss.accept();
				
				TCPThread tcpthread =new TCPThread(socket);
				
				for(int i=0;i<threadnum;i++){
					
					threads[i]=new Thread(tcpthread);
					threads[i].start();
					
				}
				//os.flush();
				//os.close();
				
			}
		} catch (IOException e) {
			System.out.println("excpetion");// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void startudp() throws IOException{
		
			DatagramSocket socket=new DatagramSocket(8082);
			serverlive=true;
			int buffersize=1024;
			byte[] receive=new byte[buffersize];
			byte[] send=new byte[buffersize];
			while(serverlive){
				DatagramPacket packet=new DatagramPacket(receive, receive.length);
				socket.receive(packet);
				String c2s=new String(packet.getData());
				System.out.println(c2s);
				InetAddress ipAddress= packet.getAddress();
				int port=packet.getPort();
				send=c2s.getBytes();
				DatagramPacket sendPacket=new DatagramPacket(send, send.length,ipAddress,port);
				socket.send(sendPacket);
			}
				
			
	}

}
class TCPThread implements Runnable{
	private static int buffersize = 0;
	private DataInputStream infromclient=null;
	private DataOutputStream outtoclient = null;
	private Socket clientsocket;
	
	public TCPThread(Socket clientSocket){
		this.clientsocket=clientSocket;
	}

	@Override
	public void run() {
		int buffersize=1;
		int datasize=64*1024*10;
		byte toSend[] = new byte[buffersize];
		byte toReceive[] = new byte[buffersize];
		Socket socket=clientsocket;
	}
	
}
