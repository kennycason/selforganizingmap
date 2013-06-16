package som;


import org.junit.Test;

import som.features.AbstractWeightVector;
import som.features.WeightVectorND;
import som.map.AbstractMapLocation;

public class SelfOrganizingMap1DWeightVector5DTest {

	@Test
	public void Test() {
		SelfOrganizingMapConfig config = new SelfOrganizingMapConfig();
		config.dimX = 20;

		config.weightVectorDimension = 5;
		config.samples = new AbstractWeightVector[]{
				new WeightVectorND(new double[] {0.8, 0.8, 0.8, 0.8, 0.8}), 
				new WeightVectorND(new double[] {0.3, 0.3, 0.3, 0.3, 0.3}), 
				new WeightVectorND(new double[] {0.1, 0.1, 0.4, 0.4, 0.5}), 
				};

		AbstractSelfOrganizingMap som = new SelfOrganizingMap1D(config);
		System.out.println("start state:");
		System.out.println(som);
		System.out.println();

		int MAX_ITER = 200;
		double t = 0.0;
		double T_INC = 1.0 / MAX_ITER;

		int iter = 1;
		System.out.println("start learning:");
		while (true) {
			if (t < 1.0) {
				System.out.println("iteration: " + iter);
				for(int i = 0; i < som.getSampleSize(); i++) {
					// get a sample
					AbstractWeightVector sample = som.getSample(i);
					
					// find it's best matching unit
					AbstractMapLocation loc = som.calculateBestMatchingUnit(sample);
	
					// scale the neighbors according to t
					som.scaleNeighbors(loc, sample, t);
				}

				// increase t to decrease the number of neighbors and the amount
				// each weight can learn
				t += T_INC;

				iter++;
			} else {
				break;
			}
		}
		System.out.println("end state:");
		System.out.println(som);
		System.out.println();
		
		System.out.println("Input (0.8, 0.8, 0.8, 0.8, 0.8)");
		WeightVectorND sample = new WeightVectorND(new double[] {0.8, 0.8, 0.8, 0.8, 0.8});
		double[] processed = som.processInput(sample);
		for(int i = 0; i < config.samples.length; i++) {
			System.out.println(config.samples[i] + " => " + processed[i]);
		}
		

	}

}
