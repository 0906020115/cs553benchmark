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
		Thread_Disk treaddisk=null;
		treaddisk=new Thread_Disk();
		Thread test1=new Thread(treaddisk);
		test1.start();
	}

}

class Thread_Disk implements Runnable{
	public Random randGen = null;
	public char[] numbersAndLetters = null;
	
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
         //randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
        }
        return new String(randBuffer);
}
	public Thread_Disk(){
		
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		int length=1024*1024;
		String dur=null;
		long start=System.currentTimeMillis();
		String string_file=this.randomString(length);
		File ff=new File("d:\\a.txt");
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
			fos.write(string_file.getBytes());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally{
			try {
				fos.close();
				long end=System.currentTimeMillis();
				dur=end-start+"";
				System.out.println("running time is"+dur);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}				
	}
		
}
