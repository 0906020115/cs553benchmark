/**
 * 
 */
package benchmarking;

/**
 * @author Ping Liu
 *
 */
import java.io.*;
import java.util.*;

public class Disk {
	public static void main(String[] args){
		String tread_1="tread_1";
		String tread_2="tread_2";
		String tread_3="tread_3";
		String tread_4="tread_4";
		File file1=new File("d:\\a.txt");
		File file2=new File("d:\\b.txt");
		File file3=new File("d:\\c.txt");
		File file4=new File("d:\\d.txt");
		//第一个
		Thread_Disk treaddisk1=null;
		treaddisk1=new Thread_Disk(tread_1,file1);
		Thread test1=new Thread(treaddisk1);

		//第二个
		Thread_Disk treaddisk2=null;
		treaddisk2=new Thread_Disk(tread_2,file2);
		Thread test2=new Thread(treaddisk2);
		
		//第三个
		Thread_Disk treaddisk3=null;
		treaddisk3=new Thread_Disk(tread_3,file3);
		Thread test3=new Thread(treaddisk3);
		
		Thread_Disk treaddisk4=null;
		treaddisk4=new Thread_Disk(tread_4,file4);
		Thread test4=new Thread(treaddisk4);
		
		test1.start();
		test2.start();
		test3.start();
		test4.start();
	}

}

class Thread_Disk implements Runnable{
	
	String tread_name=null;
	File ff=null;
	public Random randGen = null;
	public char[] numbersAndLetters = null;

	public Thread_Disk(String tread_name,File ff){
		this.ff=ff;
		this.tread_name=tread_name;
		
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		this.WriteOnDisk();
		this.ReadOnDisk();
	}
	//写到硬盘上
	public String WriteOnDisk(){
		int length=1024*1024;
		String dur=null;
		String string_file=this.randomString(length);
		if(!ff.exists()){
			try {
				ff.createNewFile();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(ff);
			long start=System.currentTimeMillis();
			fos.write(string_file.getBytes());
			long end=System.currentTimeMillis();
			dur=end-start+"";
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally{
			try {
				fos.close();
//				System.out.println("For "+this.tread_name);
				System.out.println("The running time of "+this.tread_name+" which wrote on disk is"+" "+dur);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				
			}
		}					
		return dur;
		
	}
	@SuppressWarnings("finally")
	public String ReadOnDisk(){
		String dur=null;
		int n=0;
		byte ReadBytes[]=new byte[1024];
		FileInputStream fis=null;
		try {
			fis=new FileInputStream(ff);
			long start=System.currentTimeMillis();
//			System.out.println("The start time is "+start);
			while((n=fis.read(ReadBytes))!=-1){
				//把字节转成String
				String s=new String(ReadBytes,0,n);
			}
			long end=System.currentTimeMillis();
//			System.out.println("The end time is "+end);
			dur=end-start+"";
			

		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			try {
				
				fis.close();
				
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				System.out.println("The running time of "+this.tread_name+" which read on disk is"+" "+dur);
				return dur;
			}
			
		}

		
		
	}
	//产生随机字符串，通过length,定义长度
	public final String randomString(int length) {
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
