package net.bigpoint.assessment.gasstation.rurretxaga;
import java.util.Collection;

import net.bigpoint.assessment.gasstation.*;
import net.bigpoint.assessment.gasstation.exceptions.*;

public class CGasStation implements GasStation {

	private Collection<GasPump> gasPumps;
	
	
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRevenue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfSales() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfCancellationsNoGas() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfCancellationsTooExpensive() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getPrice(GasType type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPrice(GasType type, double price) {
		// TODO Auto-generated method stub
		
	}

}
