import java.util.*;

public class FloorClerk extends Thread {
	// global variables
	public static long time = System.currentTimeMillis();
	public static Boolean free;
	public static Boolean storeClosed;
	public int clerkID;
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"]"+getName()+":"+m);
	}
	
	public FloorClerk(int id) {
		setFree(true);
		setClerkID(id);
	}

	public void run() {
		openingTime();
		while(Main.emptyStore == true) {} //busy wait till customers arrive
        try{
            Thread.sleep(100000);
        } catch(InterruptedException e){
            msg("["+(System.currentTimeMillis() - time)+"]"+" Floor clerk " + getClerkID() + " has woken up and is getting ready to leave");
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
	public synchronized void openingTime() {
		msg("["+(System.currentTimeMillis() - time)+"]" +" Floor clerk " + clerkID 
				+" is waiting for customers");
		storeClosed = false;
	}
	
	public synchronized void setClerkID(int id) {
		this.clerkID = id;
	}
	
	public synchronized int getClerkID() {
		return clerkID;
	}
	
	public synchronized void setFree(Boolean free) {
		this.free = free;
	}
	public synchronized Boolean getFree() {
		return free;
	}
}