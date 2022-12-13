package tests;

import java.math.BigDecimal;

import cern.jet.random.engine.MersenneTwister;
import it.imtlucca.util.RandomEngineFacilities;

public class TestRandomEngine {
	
	public static void main(String[] args) {
		RandomEngineFacilities randomGenerator = new RandomEngineFacilities(new MersenneTwister((int)System.currentTimeMillis()));
		BigDecimal avg = BigDecimal.ZERO;
		int n=100;
		for(int i=0;i<n;i++){
			double sample=randomGenerator.nextDouble();
			avg=avg.add(BigDecimal.valueOf(sample));
			//System.out.println(sample);
		}
		System.out.println("\n\n");
		double avgDouble = avg.doubleValue()/n;
		System.out.println("The avg is: "+avgDouble);
	}
}
