import java.util.*;
import java.util.concurrent.*;

public class FloorClerk extends Thread {
	// global variables
	public static long time = System.currentTimeMillis();
	public static Boolean free;
	public static Boolean storeClosed;
	public int clerkID;
	public static Semaphore mutex = new Semaphore(1);
	public static int i = 0;
	
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"]"+getName()+":"+m);
	}
	
	public FloorClerk(int id) {
		setFree(true);
		setClerkID(id);
	}

	public void run() {
		openingTime();
		while(true) {
			Customer.clerk.release();
			if(i < Main.customerLength) {
				i++;
			}
			else {
				break;
			}
		}
        
        for(Thread customer: Main.customerArray){ //Wait for all customers to leave
            try {
                customer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        msg("["+(System.currentTimeMillis() - time)+"]"+" Floor clerk " + getClerkID() + " clocks out");
	}
	
	// Simulate the floor clerk opening the store
	public void openingTime() {
		msg("["+(System.currentTimeMillis() - time)+"]" +" Floor clerk " + clerkID 
				+" is waiting for customers");
		storeClosed = false;
	}
	
	public void setClerkID(int id) {
		this.clerkID = id;
	}
	
	public int getClerkID() {
		return clerkID;
	}
	
	public void setFree(Boolean free) {
		this.free = free;
	}
	public Boolean getFree() {
		return free;
	}
}