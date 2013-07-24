package net.bigpoint.assessment.gasstation.rurretxaga;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import net.bigpoint.assessment.gasstation.GasPump;
import net.bigpoint.assessment.gasstation.GasStation;
import net.bigpoint.assessment.gasstation.GasType;
import net.bigpoint.assessment.gasstation.exceptions.GasTooExpensiveException;
import net.bigpoint.assessment.gasstation.exceptions.NotEnoughGasException;

public class CGasStation implements GasStation {

	// List of all the pumps
	private Vector<GasPump> gasPumps = new Vector<GasPump>();
	private double totalRevenue = 0;
	private int numberOfSales = 0;
	private int numberOfCancellationsNoGas = 0;
	private int numberOfCancellationsTooExpensive = 0;
	private Map<GasType, Double> priceMap = new HashMap<GasType, Double>();



	@Override
	public void addGasPump(GasPump pump) {
		this.gasPumps.add(pump);
	}

	@Override
	public Collection<GasPump> getGasPumps() {
		return new Vector<GasPump>(this.gasPumps);
	}

	@Override
	public synchronized double buyGas(GasType type, double amountInLiters,
			double maxPricePerLiter) throws NotEnoughGasException,
			GasTooExpensiveException {
		
			double price = getPrice(type) * amountInLiters;
			// The price per liter of our gas station is more expensive than the price the customer is willing to pay.
			// In this case, we must increase the counter of the number of failed sales due to too expensive gas and throw the exception
			if (price > maxPricePerLiter) {
				numberOfCancellationsTooExpensive++;
				throw new GasTooExpensiveException();
			}
		
			
			double gasCounter = 0.0;
			Vector<GasPump> auxPumpList = new Vector<GasPump>();
			for (GasPump pump : gasPumps) {
				if (pump.getGasType() != type)
					continue;
				
				// The price is suitable for the customer and there is enough gas to pump.
				// In this case, we increase the number of successful sales, we calculate the total revenue, we call the pumpGas method from the gas pump(s) and we return the total price
				if (pump.getRemainingAmount() >= amountInLiters) {
					pump.pumpGas(amountInLiters);
					numberOfSales++;
					totalRevenue += price;
					return price;
				}
				// There is one or more pumps with that gas type and enough amount to pump.
				// In this case, we must continue checking.
				auxPumpList.add(pump);
				gasCounter += pump.getRemainingAmount();
				if (gasCounter < amountInLiters)
					continue;
				// The gas counter of the remaining amount from the list of pumps is the same as the one the client is requesting.
				// In this case, an auxiliary method is called to start pumping gas for them.
				pumpGas(auxPumpList, amountInLiters);
				numberOfSales++;
				totalRevenue += price;
				return price;
			}
			// There is not enough amount to satisfy the client.
			// In this case, we must increase the counter of the number of failed sales due to not enough gas and throw the exception
			numberOfCancellationsNoGas++;
			throw new NotEnoughGasException();
			
	}

	
	/** Auxiliary method, used for pumping gas from the list of pumps so the final amount of gas satisfies the client */
	private void pumpGas(Vector<GasPump> gasPumps, double requestedLiters) {
		for (GasPump pump : gasPumps) {
			double remaining = pump.getRemainingAmount();
			if (requestedLiters <= remaining) {
				pump.pumpGas(requestedLiters);
				break;
			}
			else {
				pump.pumpGas(remaining);
				requestedLiters -= remaining;
			}
		}
	}
	
	@Override
	public double getRevenue() {
		synchronized(this){
			return this.totalRevenue;
		}

	}

	@Override
	public int getNumberOfSales() {
		return this.numberOfSales;

	}

	@Override
	public int getNumberOfCancellationsNoGas() {
		return this.numberOfCancellationsNoGas;

	}

	@Override
	public int getNumberOfCancellationsTooExpensive() {
		return this.numberOfCancellationsTooExpensive;
	}

	@Override
	public synchronized double getPrice(GasType type) {
		// If there is no price set up for that type of gas, a exception is thrown
		if (priceMap.get(type) == null)
			throw new RuntimeException("Price not specified for gas type.");
		return (double)priceMap.get(type);
	}

	@Override
	public synchronized void setPrice(GasType type, double price) {
			priceMap.put(type, (Double)price);
	}

}
