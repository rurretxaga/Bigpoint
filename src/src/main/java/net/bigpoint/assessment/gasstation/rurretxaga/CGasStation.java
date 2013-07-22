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
			return this.gasPumps;
	}

	@Override
	public double buyGas(GasType type, double amountInLiters,
			double maxPricePerLiter) throws NotEnoughGasException,
			GasTooExpensiveException {
		synchronized(this){
			boolean gasPumped = false;
			boolean foundGasType = false;
			Vector<GasPump> auxPumpList = new Vector<GasPump>();
			double gasCounter = 0;		
			for (int i = 0; i < gasPumps.size(); i++)
			{
				GasPump pump = gasPumps.get(i);
				// Checking if gas type of the pumper is the same as requested
				if (pump.getGasType() != type)
				{
					continue;
				}
				else
				{
					foundGasType = true;
					// Checking if the remaining amount of gas from pumper satisfies what customer is asking
					gasCounter += pump.getRemainingAmount();
					if((pump.getRemainingAmount() < amountInLiters) && (gasCounter < amountInLiters))
					{
						auxPumpList.add(pump);
						continue;
					}
					else 
					{
						if (pump.getRemainingAmount() >= amountInLiters)
						{
							auxPumpList.clear();
							auxPumpList.add(pump);
							gasCounter = 0;
							gasPumped = true;
						}
						else if (gasCounter >= amountInLiters)
						{
							auxPumpList.add(pump);
							gasPumped = true;
						}
						break;
					}

				}

			}

			// There is no pump with that gas type
			if(!gasPumped && !foundGasType)
			{
				return 0;
			}
			// There is one or more pumps with that gas type but not enough amount.
			// In this case, we must increase the counter of the number of failed sales due to not enough gas and throw the exception
			else if(!gasPumped && foundGasType)
				{
					numberOfCancellationsNoGas++;
					throw new NotEnoughGasException();
				}
				// There is one or more pumps with that gas type and enough amount to pump.
				// In this case, we must continue checking.
				else if (gasPumped && foundGasType)
					{
						// There is no price set for that gas pump
						if(this.getPrice(type) == 0)
						{
							return 0;
						}

						// The price per liter of our gas station is more expensive than the price the customer is willing to pay.
						// In this case, we must increase the counter of the number of failed sales due to too expensive gas and throw the exception					
						if(maxPricePerLiter < this.getPrice(type))
						{
							numberOfCancellationsTooExpensive++;
							throw new GasTooExpensiveException();
						}
						// The price is suitable for the customer and there is enough gas to pump.
						// In this case, we increase the number of successful sales, we calculate the total revenue, we call the pumpGas method from the gas pump(s) and we return the total price
						else
						{
							numberOfSales++;
							double finalPrice = (this.getPrice(type) * amountInLiters);
							totalRevenue += finalPrice;							
							double remainingAmountToBePumped = amountInLiters;
							for (int i = 0; i < auxPumpList.size(); i++)
							{
								GasPump pump = auxPumpList.get(i);
								if(remainingAmountToBePumped <= pump.getRemainingAmount())
								{
									pump.pumpGas(remainingAmountToBePumped);
									break;
								}
								else
								{
									remainingAmountToBePumped -= pump.getRemainingAmount();
									pump.pumpGas(pump.getRemainingAmount());
									continue;
								}
							}
							return finalPrice;						
						}
					}
		} // synchronized
		return 0;
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
	public double getPrice(GasType type) {
		if(priceMap.get(type)!=null)
		{
			return (double)priceMap.get(type);
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void setPrice(GasType type, double price) {
			priceMap.put(type, (Double)price);
	}

}
