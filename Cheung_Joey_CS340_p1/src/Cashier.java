import java.util.*;

public class Cashier extends Thread {
	// Global variables
	public static long time = System.currentTimeMillis();
	public static Boolean free;
	public static Boolean cash;
	public int cashierID;
	public static Queue<Customer> cashQueue = new LinkedList<Customer>();
	public static Queue<Customer> cardQueue = new LinkedList<Customer>();
	
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"]"+getName()+":"+m);
	}
	
	public Cashier(Boolean cash, int cashierID) {
		setFree(true);
		setCash(cash);
		setCashierID(cashierID);
	}
	
	public void run() {
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            msg("["+(System.currentTimeMillis() - time)+"]"+" Cashier " + getCashierID() + " has been woken up and is getting ready to leave");
        }

        for(Thread  cashier: Main.customerArray){ //Wait for all customers to leave
            try {
                cashier.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        msg("["+(System.currentTimeMillis() - time)+"]"+" Cashier " + getCashierID() + " clocks out");
	}
	
	public synchronized void setFree(Boolean free) {
		this.free = free;
	}
	
	public synchronized Boolean getFree() {
		return free;
	}
	
	public synchronized void setCash(Boolean cash) {
		this.cash = cash;
	}
	
	public synchronized Boolean getCash() {
		return cash;
	}
	
	public synchronized int getCashierID() {
		return cashierID;
	}
	
	public synchronized void setCashierID(int cashierID) {
		this.cashierID = cashierID;
	}
	public synchronized void left() {

		msg("["+(System.currentTimeMillis() - time)+"]"+" Cashier " + getCashierID() + " left");
	}
}
