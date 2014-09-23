/**
 * 
 */
package benchmarking;

import java.awt.Event;
import java.util.Arrays;

import org.jocl.*;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * @author ZHANG
 *
 */
public class GPU {
	private static String floatins =
			"__kernel void "+
			        "sampleKernel(__global const float *a,"+
			        "             __global const float *b,"+
			        "             __global float *c)"+
			        "{"+
			        "    int gid = get_global_id(0); "+
			        "for(int i=0;i<b[gid];i++){"+
			       
			        "c[gid] = a[gid] + 1;}"+
			        "    "+
			        "}";
	private static String intins =
			"__kernel void "+
			        "sampleKernel(__global const int *a,"+
			        "             __global const int *b,"+
			        "             __global int *c)"+
			        "{"+
			        "    int gid = get_global_id(0);int i;"+
			        ""+
			        "    c[gid] = a[gid] + b[gid];c[gid]=c[gid]+i;c[gid]=c[gid]-i"+
			        "}";
	private static cl_context context;
	private static cl_device_id device_id;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CL.setExceptionsEnabled(true);
		
		int numPlatformsArray[] =new int[1];
		int buffersize=0;
		int n=1024*1024*10;
		float srcArrayA[] = new float[n];
        float srcArrayB[] = new float[n];
        float dstArray[] = new float[n];
        for (int i=0; i<n; i++)
        {
            srcArrayA[i] = 1;
            srcArrayB[i] = 1000;
        }
        Pointer srcA = Pointer.to(srcArrayA);
        Pointer srcB = Pointer.to(srcArrayB);
        Pointer dst = Pointer.to(dstArray);
		 CL.clGetPlatformIDs(0, null, numPlatformsArray);
	     int numPlatforms = numPlatformsArray[0];
	     cl_platform_id platforms[]=new cl_platform_id[numPlatforms];  
		 CL.clGetPlatformIDs(platforms.length,platforms,null);
		 cl_platform_id platform=platforms[0];
		cl_context_properties contextproperties =new cl_context_properties();
		contextproperties.addProperty(CL.CL_CONTEXT_PLATFORM, platform);
		 int numDevicesArray[] = new int[1];
	     CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_GPU, 0, null, numDevicesArray);
	     int numDevices = numDevicesArray[0];
		 cl_device_id devices[] = new cl_device_id[numDevices];
	     CL.clGetDeviceIDs(platform, CL.CL_DEVICE_TYPE_GPU, numDevices, devices, null);
	     System.out.println("numDevices"+numDevices);
	     cl_device_id device = devices[0];
	     cl_context context =CL.clCreateContextFromType(contextproperties, CL.CL_DEVICE_TYPE_GPU, null, null, null);
	     cl_command_queue command_queue=CL.clCreateCommandQueue(context, device, CL.CL_QUEUE_PROFILING_ENABLE, null);
	     cl_mem memObject[]=new cl_mem[3];
	     if(args[0].equals("int")) buffersize=Sizeof.cl_int;
	     if(args[0].equals("float")) buffersize=Sizeof.cl_float*n;
	     memObject[0]=CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY| CL.CL_MEM_COPY_HOST_PTR, buffersize, srcA, null);
	     memObject[1]=CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY| CL.CL_MEM_COPY_HOST_PTR, buffersize, srcB, null);
	     memObject[2]=CL.clCreateBuffer(context, CL.CL_MEM_READ_WRITE, buffersize, null, null);
	    
	     cl_program program=CL.clCreateProgramWithSource(context, 1, new String[]{floatins}, null, null);
		 CL.clBuildProgram(program, 0, null,null, null, null);
		 cl_kernel kernel=CL.clCreateKernel(program, "sampleKernel", null);
		 CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memObject[0]));
		 CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memObject[1]));
		 CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memObject[2]));
		 long global_work_size[] = new long[]{n};
	     long local_work_size[] = new long[]{160};
	     System.out.println(Arrays.toString(local_work_size));
	     long startTime[] = new long[1];
         long endTime[] = new long[1];
	     cl_event kEvent=new cl_event();
	     for(int i=0;i<1;i++){
	    	 for(int j=0;j<1;j++){
	    		 for(int k=0;k<5;k++){
	    	
	             CL.clEnqueueNDRangeKernel(command_queue, kernel, 1, null, global_work_size, null, 0, null, kEvent);
	             CL.clWaitForEvents(1, new cl_event[]{kEvent});
	             System.out.println(kEvent.toString());
	    		 CL.clGetEventProfilingInfo(kEvent,CL.CL_PROFILING_COMMAND_START, Sizeof.cl_ulong, Pointer.to(startTime), null);
	    		 CL.clGetEventProfilingInfo(kEvent,CL.CL_PROFILING_COMMAND_END, Sizeof.cl_ulong, Pointer.to(endTime), null);
	    		 double dur=endTime[0]-startTime[0];
	    		 dur=dur/(double)1000000000;
	    		 long speed=(long) (n/dur);
	    		// speed=speed;
	    		 System.out.println("Time:"+dur+"   ");
	    		 System.out.println("Speed:"+speed+"Hz");
	    		 }
	    	 }
	     }
	     cl_event mEvent=new cl_event();
	     CL.clEnqueueReadBuffer(command_queue, memObject[2], CL.CL_TRUE, 0, 1, dst, 0, null, mEvent);
	     CL.clWaitForEvents(1, new cl_event[]{mEvent});
	     CL.clGetEventProfilingInfo(mEvent,CL.CL_PROFILING_COMMAND_START, Sizeof.cl_ulong, Pointer.to(startTime), null);
		 CL.clGetEventProfilingInfo(mEvent,CL.CL_PROFILING_COMMAND_END, Sizeof.cl_ulong, Pointer.to(endTime), null);
		 long dur=endTime[0]-startTime[0];
		 dur=dur/(long)1000000;
		 long speed=(n/dur);
		 System.out.println("MTime:"+dur+"   ");System.out.println("MSpeed:"+speed+"MHz");
	     
	     
	   // System.out.println("Result: "+java.util.Arrays.toString(dstArray));
	     
	     CL.clReleaseMemObject(memObject[0]);
	     CL.clReleaseMemObject(memObject[1]);
	     CL.clReleaseMemObject(memObject[2]);
	     CL.clReleaseKernel(kernel);
	     CL.clReleaseProgram(program);
         CL.clReleaseCommandQueue(command_queue);
	     CL.clReleaseContext(context);
	     
	}

}
