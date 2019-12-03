import java.util.*;
public class Customer extends Thread{

	public int customerID;  
	public static Boolean slipReceived;
	public static Boolean cash;
	public static long time = System.currentTimeMillis();
	
	public Customer(int id, Boolean cash) {
		customerID = id;
		setCustomerSlipReceived(false); //change this later
		setCash(cash);
	}

	@Override
	public void run() {
		// decide if customer is paying with card or cash
		if (getCash() == false) {
			Cashier.cardQueue.add(this);
		}
		else {
			Cashier.cashQueue.add(this);
		}
		browseStore();
		waitsInLine();
		goesToCashier();
		cafeteria();
		if(getId() == 1){
			FloorClerk.storeClosed = true;
			// Interrupt all of the threads that are alive in decreasing order
			for(Thread floorClerk: Main.clerkArray){	
                floorClerk.interrupt();
            }
            for (Thread cashier : Main.cashierArray) {
                cashier.interrupt();
            }
		}else{
			left();
		}
	}
	
	// Simulating the customer browsing the store
	public synchronized void browseStore() {
		// Use the sleep method and let the customer browse the store
		// until (s)he finds an item that (s)he likes
		Main.emptyStore = false;
		try {
			Thread.sleep((long) (Math.random()*2000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		msg("["+(System.currentTimeMillis() - time)+"]"+" Customer " + customerID 
				+ " is browsing the store");
		
		try {
			Thread.sleep((long) (Math.random()*2000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		msg("["+(System.currentTimeMillis() - time)+"]"+ "Customer " + customerID + " is deciding on purchasing item");
		
		Thread.yield();
		Thread.yield();
		
		msg("["+(System.currentTimeMillis() - time)+"]"+ "Customer " + customerID + " decided on buying the item, going to floor clerk");
	}
	// everyone leaves
	public synchronized void left() {

		msg("["+(System.currentTimeMillis() - time)+"]"+" Customer " + customerID + " left");
		FloorClerk.storeClosed = true;
	}
	// simulates waiting in line
	public synchronized void waitsInLine() {
		while(Main.clerkArray[0].getFree() != true && Main.clerkArray[1].getFree() != true && Main.clerkArray[0].getFree() != true){};
		msg("["+(System.currentTimeMillis() - time)+"]"+" Customer " + customerID + " is being helped");
		setCustomerSlipReceived(true);
	}
	// simulates going to cashier
	public synchronized void goesToCashier() {
		msg(" Customer " + customerID + " is rushing to cashier");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		if (this.getCash() == false) {
			while (Main.cashierArray[0].getFree() == false) {};
			try {
				msg(" Customer " + customerID + " is paying for item with card and is being helped by card cashier");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			while(Main.cashierArray[1].getFree() == false) {};
			try {
				msg(" Customer " + customerID + " is paying for item with cash and is being helped by cash cashier");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
	}
	// simulate going to the customer
	public synchronized void cafeteria() {
		msg(" Customer " + customerID + " is going to cafeteria to take a break");
		try {
			// for loop that will terminate all threads in decreasing order
			// based off of their ID
			for(int i = 0; i < Main.currentCustomers.length; i++) {
				Customer c = Main.currentCustomers[i];
				if(c!=null) {
					if (c.isAlive() && c.getCustomerID() > this.getCustomerID()) {
						c.join();	
						msg("["+(System.currentTimeMillis() - time)+"]"+" Customer " + customerID + "about to leave");
					}	
				}
			}
			
			// Simulate the customers taking a break
			Thread.sleep(10000);
			
		} catch (InterruptedException e) {
			// Catch the interrupt exception because it will occur
			// when the customer is interrupted
		}
	}
	
	public synchronized void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"]"+getName()+":"+m);
	}
	
	public synchronized int getCustomerID() {
		return customerID;
	}
	
	public synchronized void setCash(Boolean cash) {
		this.cash = cash;
	}
	
	public synchronized Boolean getCash() {
		return cash;
	}

	public synchronized void setCustomerSlipReceived(Boolean slipReceived) {
		this.slipReceived = slipReceived;
	}
	
	public synchronized Boolean getCustomerSlipReceived() {
		return slipReceived;
	}
}