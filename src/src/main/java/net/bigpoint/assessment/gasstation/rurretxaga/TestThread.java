package net.bigpoint.assessment.gasstation.rurretxaga;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

public class TestThread extends Thread {
	
	private  CGasStation gasStation = new CGasStation();
	private double amountLeft = 170;
	
	public TestThread() {
		GasPump gasPump = new GasPump(GasType.DIESEL, 170);
		gasStation.addGasPump(gasPump);
		gasPump = new GasPump(GasType.SUPER, 150);
		gasStation.addGasPump(gasPump);
		gasStation.setPrice(GasType.DIESEL, 20);
		gasStation.setPrice(GasType.SUPER, 30);
	}

	private void getGas(GasType type, double amountInLiters,
			double maxPricePerLiter) throws NotEnoughGasException,
			GasTooExpensiveException
	{
		 double price = gasStation.buyGas(type, amountInLiters, maxPricePerLiter);
		 System.out.println(" Se ha sacado gasofa desde el Thread: " + Thread.currentThread().getName() + ". Precio final: " + price);
		 amountLeft -= amountInLiters;
		 System.out.println(Thread.currentThread().getName() + ". Gasofa restante: " + amountLeft);
	}

	public void run()
	{
		try {
			System.out.println(" El Thread: " + Thread.currentThread().getName() + " quiere comprar gasofa ");
			for (int i=0; i<7; i++)
			{
				this.getGas(GasType.DIESEL, 20.00, 100.00);
			}
		} catch (NotEnoughGasException e) {
			System.out.println(" No hay suficiente gasofa en el Thread: " + Thread.currentThread().getName() + ".");
			e.printStackTrace();
		} catch (GasTooExpensiveException e) {
			System.out.println(" Demasiado caro para el Thread: " + Thread.currentThread().getName() + ".");
			e.printStackTrace();
		}
	}

}
