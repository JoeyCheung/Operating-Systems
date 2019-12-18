import java.util.*;

public class Main {
	// global variables
	public static Customer[] currentCustomers;
	public static FloorClerk[] clerkArray;
	public static Cashier[] cashierArray;
	public static Customer[] customerArray;
	public static Boolean emptyStore;
	public static Random random = new Random();
	public static int customerLength;
	
	public static void main(String[] args) {
		// check if the store is empty
		emptyStore = true;
//		int nCustomers = customerLength;
		int nCustomers = Integer.parseInt(args[0]);
		customerLength = nCustomers;
		
		currentCustomers = new Customer[nCustomers];
		customerArray = new Customer[nCustomers];
		clerkArray = new FloorClerk[3];
		cashierArray = new Cashier[2];
		// number of clerks
		for(int i = 0; i < 3; i++){
			FloorClerk clerk = new FloorClerk(i+1);
			clerkArray[i] = clerk;
			clerk.start();
		}
		// number of cashiers
		Cashier cashier1 = new Cashier(true, 1);
		cashierArray[0] = cashier1;
		cashier1.start();
		Cashier cashier2 = new Cashier(false, 2);
		cashierArray[1] = cashier2;
		cashier2.start();
		// number of customers
		for (int i = 0; i < customerLength; i++) {
			Random rd = new Random();
			Customer customer = new Customer(i+1, rd.nextBoolean());
			customerArray[i] = customer;
			customer.start();
		}
		
	}
	
}
