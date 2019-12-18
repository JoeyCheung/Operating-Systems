import java.util.*;
import java.util.concurrent.*;
public class Customer extends Thread{

	public int customerID;  
	public static Boolean slipReceived;
	public static Boolean cash;
	public static long time = System.currentTimeMillis();
	public static Semaphore cashCashier = new Semaphore(1);
	public static Semaphore cardCashier = new Semaphore(1);
	public static Semaphore clerk = new Semaphore(1);
	public static Semaphore group = new Semaphore(0);
	public static int cashCount = 0;
	public static int cardCount = 0;
	public static int count = 0;
	public int mini_size = 4;
	public static int tracker = 0;
	
	public Customer(int id, Boolean cash) {
		customerID = id;
		setCustomerSlipReceived(false); //change this later
		setCash(cash);
	}

	@Override
	public void run() {
		browseStore();
		waitsInLine();
		// decide if customer is paying with card or cash
		if (getCash() == false) {
			try {
				cardCashier.acquire();
				cardCount++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				cashCashier.acquire();
				cashCount++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		} 
		else {
			if (tracker % mini_size == 0 || tracker == Main.customerLength) {
				group.release();
				getOnBus();
			}
			else {
				tracker++;
				try {
					group.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			count++;
		}
	}
	
	// Simulating the customer browsing the store
	public void browseStore() {
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
	public void left() {
		msg("["+(System.currentTimeMillis() - time)+"]"+" Customer " + customerID + " left");
		FloorClerk.storeClosed = true;
	}
	// simulates waiting in line
	public void waitsInLine() {
		try {
			clerk.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg("["+(System.currentTimeMillis() - time)+"]"+" Customer " + customerID + " is being helped");
		setCustomerSlipReceived(true);
	}
	// simulates going to cashier
	public void goesToCashier() {
		msg(" Customer " + customerID + " is rushing to cashier");
		if (this.getCash() == false) {
			try {
				cashCashier.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				msg(" Customer " + customerID + " is paying for item with card and is being helped by card cashier");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				cardCashier.acquire();
				msg(" Customer " + customerID + " is paying for item with cash and is being helped by cash cashier");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	// simulate going to the customer
	public void cafeteria() {
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
	
	public void getOnBus() {
		msg(" Customer " + customerID + " is going on the bus");
	}
	
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"]"+getName()+":"+m);
	}
	
	public int getCustomerID() {
		return customerID;
	}
	
	public void setCash(Boolean cash) {
		this.cash = cash;
	}
	
	public Boolean getCash() {
		return cash;
	}

	public void setCustomerSlipReceived(Boolean slipReceived) {
		this.slipReceived = slipReceived;
	}
	
	public Boolean getCustomerSlipReceived() {
		return slipReceived;
	}
}