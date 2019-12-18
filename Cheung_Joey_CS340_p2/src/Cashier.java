import java.util.*;
import java.util.concurrent.*;

public class Cashier extends Thread {
	// Global variables
	public static long time = System.currentTimeMillis();
	public static Boolean free;
	public static Boolean cash;
	public int cashierID;
	public static int i = 0;
	
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"]"+getName()+":"+m);
	}
	
	public Cashier(Boolean cash, int cashierID) {
		setFree(true);
		setCash(cash);
		setCashierID(cashierID);
	}
	
	public void run() {
		while(true) {
			Customer.cardCashier.release();
			Customer.cashCashier.release();
			if(i < Main.customerLength) {	
				i++;
			}
			else {
				break;
			}
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
	
	public void setFree(Boolean free) {
		this.free = free;
	}
	
	public Boolean getFree() {
		return free;
	}
	
	public void setCash(Boolean cash) {
		this.cash = cash;
	}
	
	public Boolean getCash() {
		return cash;
	}
	
	public int getCashierID() {
		return cashierID;
	}
	
	public void setCashierID(int cashierID) {
		this.cashierID = cashierID;
	}
	public void left() {
		msg("["+(System.currentTimeMillis() - time)+"]"+" Cashier " + getCashierID() + " left");
	}
}
