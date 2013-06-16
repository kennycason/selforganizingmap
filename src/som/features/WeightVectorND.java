package som.features;



public class WeightVectorND extends AbstractWeightVector {

	public WeightVectorND (double ... values) {
		super(values);
	}
	
	public WeightVectorND add(double m) {
		WeightVectorND ret = new WeightVectorND(new double[size()]);
		for(int i = 0; i < size(); i++) {
			ret.set(i, get(i) + m);
		}
		return ret;
	}
	
	public WeightVectorND sub(double m) {
		WeightVectorND ret = new WeightVectorND(new double[size()]);
		for(int i = 0; i < size(); i++) {
			ret.set(i, get(i) - m);
		}
		return ret;
	}	
	
	public WeightVectorND mult(double m) {
		WeightVectorND ret = new WeightVectorND(new double[size()]);
		for(int i = 0; i < size(); i++) {
			ret.set(i, get(i) * m);
		}
		return ret;
	}
	
	public WeightVectorND div(double m) {
		WeightVectorND ret = new WeightVectorND(new double[size()]);
		if(m != 0.0) {
			for(int i = 0; i < size(); i++) {
				ret.set(i, get(i) / m);
			}
		} else {
			for(int i = 0; i < size(); i++) {
				ret.set(i, 0);
			}
		}
		return ret;
	}
	
	public WeightVectorND add(AbstractWeightVector m) {
		WeightVectorND ret = new WeightVectorND(new double[size()]);
		int maxSize = max(size(), m.size());
		for(int i = 0; i < maxSize; i++) {
			ret.set(i, get(i) + ((WeightVectorND)m).get(i));
		}
		return ret;
	}
	
	public WeightVectorND sub(AbstractWeightVector m) {
		WeightVectorND ret = new WeightVectorND(new double[size()]);
		int maxSize = max(size(), m.size());
		for(int i = 0; i < maxSize; i++) {
			ret.set(i, get(i) - ((WeightVectorND)m).get(i));
		}
		return ret;
	}	
	
	public WeightVectorND mult(AbstractWeightVector m) {
		WeightVectorND ret = new WeightVectorND(new double[size()]);
		int maxSize = max(size(), m.size());
		for(int i = 0; i < maxSize; i++) {
			ret.set(i, get(i) * ((WeightVectorND)m).get(i));
		}
		return ret;
	}
	
	public WeightVectorND div(AbstractWeightVector m) {
		WeightVectorND ret = new WeightVectorND(new double[size()]);
		boolean nonZero = true;
		int maxSize = max(size(), m.size());
		for(int i = 0; i < maxSize; i++) {
			nonZero &= (((WeightVectorND)m).get(i) != 0);
		}
		if(nonZero) {
			for(int i = 0; i < size(); i++) {
				ret.set(i, get(i) / ((WeightVectorND)m).get(i));
			}
		} else {
			for(int i = 0; i < size(); i++) {
				ret.set(i, 0);
			}
		}
		return ret;
	}
	
	public WeightVectorND unit() {
		WeightVectorND v = new WeightVectorND();
		double m = magnitude();
		for(int i = 0; i < size(); i++) {
			v.set(i, get(i) / m);
		}
		return v;
	}
	
	private int max(int a, int b) {
		return a > b ? a : b;
	}
	

}
