/**
 * @(#)test.java
 *
 *
 * @author 
 * @version 1.00 2022/2/23
 */

import java.util.Timer;
import java.util.TimerTask;
public class test {

    public static void main(String args[]) {
    	test1 t=new test1(false);
    	test1 t2=new test1(true);
    	Thread thread=new Thread(t);
    	Thread thread2=new Thread(t2);
 
    		
    	thread.start();
    	thread2.start();
    	try{
    		Thread.sleep(1000);
    	}
    	catch(Exception e){}
    	t.setC(true);
 		t2.setC(false);

    }
    
    
}
class test1 implements Runnable{
	private boolean c;
	public test1(boolean c){
		this.c=c;
	}
	public void setC(boolean c){
		this.c=c;
	}
	@Override
	public void run(){
			
			if (c)
				System.out.println("hi");
			else
				System.out.println("bye");
			Timer timer = new Timer();
		
			timer.schedule( new TimerTask() {
  	  		public void run() { 
  	  			try{
    		Thread.sleep(1000);
    		System.out.println(c);	
    		}
    		catch(Exception e){}
  	  		}
			}, 0,1025);
	} 
	
}