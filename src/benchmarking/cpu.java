package benchmarking;

import java.util.Random;

import sun.awt.windows.ThemeReader;

public class cpu {
	
	private static final int Threads=8; 
	public static void main(String[] args) {
		TaskThread taskThread=new TaskThread();
		Thread[] threads=new Thread[Threads];
		for(int i=0;i<Threads;i++){
		threads[i]=new Thread(taskThread);
		threads[i].start();
		}
		
		//cThread.start();

	}
}
	class TaskThread implements Runnable{
		private static final int cnt=200;
		public TaskThread() {
			
			// TODO Auto-generated constructor stub
		} 
		@Override
		public void run() {
			try {
				String dur=null;
				long start=System.currentTimeMillis();
				int[][] A = new int[cnt][cnt],B=new int[cnt][cnt],C=new int[cnt][cnt];
				int_mat(A);
				int_mat(B);
				integer_operation(A, B, C);
				long end=System.currentTimeMillis();
				dur=end-start+"";
				System.out.println("running time is"+dur);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception");
			}
			// TODO Auto-generated method stub
			
		}
		void int_mat(int[][] matric){
			 for(int i=1;i<cnt;i++)
			  {
			    for(int j=1;j<cnt;j++)
			    {
			      matric[i][j] =(int)(Math.random()*50);
			    }
			  }
		}
		void float_mat(float[][] matric){
			for(int i=0;i<cnt;++i)
			  {
			    for(int j=0;j<cnt;++j)
			    {
			      matric[i][j] =(float) (Math.random()*50);
			    }
			  }
		}
		void integer_operation(int[][] A, int[][] B, int[][] C)
		{
		  for(int i=0;i<cnt;i++)
		  {
		    for(int j=0;j<cnt;j++)
		    {
		      C[i][j] = 0;
		      for(int k=0;k<cnt;++k)
		      {
		        C[i][j]+=A[i][k]*B[k][j];
		      }
		    }
		  }
		}
		void float_operation(float[][] A, float[][] B, float[][] C)
		{
		  for(int i=0;i<cnt;++i)
		  {
		    for(int j=0;j<cnt;++j)
		    {
		      C[i][j] = 0;
		      for(int k=0;k<cnt;++k)
		      {
		        C[i][j]+=A[i][k]*B[k][j];
		      }
		    }
		  }
		}
		
	}


