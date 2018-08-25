package som.map;

import som.lib.vector.Vector;

/**
 * represents a physical location in the Self Organizing Map
 * @author kenny
 *
 */
public abstract class AbstractMapLocation extends Vector {

	protected AbstractMapLocation(int size) {
		super(Vector.buildDefaultValues(size, 0));
	}
	
	
}
