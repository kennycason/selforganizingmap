package som;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import som.features.WeightVectorND;

public class WeightVectorTest {

	private static final double DELTA = 0.01;

	
	@Test
	public void Test1D() {
		WeightVectorND vector = new WeightVectorND(0.5);
		
		assertEquals(0.5, vector.get(0), DELTA);
		
		WeightVectorND temp = vector.add(2.0);
		
		assertEquals(2.5, temp.get(0), DELTA);
		
	}
	
	@Test
	public void Test2D() {
		WeightVectorND vector = new WeightVectorND(0.5, 0.6);
		
		assertEquals(0.5, vector.get(0), DELTA);
		assertEquals(0.6, vector.get(1), DELTA);
		
		WeightVectorND temp = vector.add(2.0);
		
		assertEquals(2.5, temp.get(0), DELTA);
		assertEquals(2.6, temp.get(1), DELTA);
		
	}

	@Test
	public void Test3D() {
		WeightVectorND vector = new WeightVectorND(0.5, 0.6, 0.7);
		
		assertEquals(0.5, vector.get(0), DELTA);
		assertEquals(0.6, vector.get(1), DELTA);
		assertEquals(0.7, vector.get(2), DELTA);
		
		WeightVectorND temp = vector.add(2.0);
		
		assertEquals(2.5, temp.get(0), DELTA);
		assertEquals(2.6, temp.get(1), DELTA);
		assertEquals(2.7, temp.get(2), DELTA);
		
	}
	
}
