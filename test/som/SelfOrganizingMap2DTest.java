package som;


import org.junit.Test;

import som.features.AbstractWeightVector;
import som.features.WeightVector;
import som.map.AbstractMapLocation;

public class SelfOrganizingMap2DTest {

	@Test
	public void Test() {
		SelfOrganizingMapConfig config = new SelfOrganizingMapConfig();
		config.dimX = 10;
		config.dimY = 10;
		config.weightVectorDimension = 2;
		config.samples = new AbstractWeightVector[]{
				new WeightVector(0.0, 0.0),
				new WeightVector(0.5, 0.5),
				new WeightVector(0.9, 0.9),
				};

		AbstractSelfOrganizingMap som = new SelfOrganizingMap2D(config);
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
		
		System.out.println("Input (0.8, 0.8)");
		WeightVector sample = new WeightVector(0.8, 0.8);
		double[] processed = som.processInput(sample);
		for(int i = 0; i < config.samples.length; i++) {
			System.out.println(config.samples[i] + " => " + processed[i]);
		}
		
		System.out.println("Input (0.9, 0.9)");
		sample = new WeightVector(0.9, 0.9);
		processed = som.processInput(sample);
		for(int i = 0; i < config.samples.length; i++) {
			System.out.println(config.samples[i] + " => " + processed[i]);
		}
		
		System.out.println("Input (0.3, 0.5)");
		sample = new WeightVector(0.3, 0.5);
		processed = som.processInput(sample);
		for(int i = 0; i < config.samples.length; i++) {
			System.out.println(config.samples[i] + " => " + processed[i]);
		}
	}

}
