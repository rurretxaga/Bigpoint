package net.bigpoint.assessment.gasstation.rurretxaga;

public class MainClass {

	public static void main(String[] args) {
		TestThread gasStation = new TestThread();
	    
		
        Thread customer1 = new Thread(gasStation, "Luis");
        Thread customer2 = new Thread(gasStation, "Manuel");
       
        customer1.start();
        customer2.start();

	}

}
