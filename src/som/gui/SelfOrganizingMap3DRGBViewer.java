package som.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import som.SelfOrganizingMap3D;
import som.SelfOrganizingMapConfig;
import som.features.AbstractWeightVector;
import som.features.WeightVectorND;
import som.map.AbstractMapLocation;
import som.save.State;


public class SelfOrganizingMap3DRGBViewer extends Canvas {

	SelfOrganizingMap3D som;
	
	SelfOrganizingMapConfig config;
	
	private int squareSize = 10;
	
	private int dim = 40;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The stragey that allows us to use accelerate page flipping */
	
	private BufferStrategy strategy;
	
	public static void main(String argv[]) {
		SelfOrganizingMap3DRGBViewer somv =new SelfOrganizingMap3DRGBViewer();

		somv.init();
		
		somv.run();
	}
	
	public SelfOrganizingMap3DRGBViewer() {
		// create a frame 
		JFrame container = new JFrame("Self Organizing Map 3D Viewer");
				
		// get hold the content of the frame and set up the 
		// resolution
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(squareSize * dim, squareSize * dim));
		panel.setLayout(null);
				
		// setup our canvas size and put it into the content of the frame
		setBounds(0, 0, squareSize * dim, squareSize * dim);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		container.pack();
		container.setResizable(false);
		container.setVisible(true);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit 
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
	}
	
	public void init() {
		config = new SelfOrganizingMapConfig();
		config.weightVectorDimension = 3;
		config.radius = 75;
		config.dimX = dim;
		config.dimY = dim;
		config.dimZ = dim;
		
		config.samples = new AbstractWeightVector[]{
				new WeightVectorND(0.0, 0.0, 0.0), // black
				new WeightVectorND(1.0, 1.0, 1.0), // white
				};	
		
		config.samples = new AbstractWeightVector[]{
				new WeightVectorND(0.0, 0.0, 0.0), // black
				new WeightVectorND(1.0, 0.0, 1.0), // pink
				new WeightVectorND(1.0, 1.0, 1.0), // white
				new WeightVectorND(0.0, 0.0, 1.0), // blue
				new WeightVectorND(1.0, 0.0, 0.0), // red
				new WeightVectorND(0.0, 1.0, 0.0), // green
				};
		
	/*	config.samples = new AbstractWeightVector[]{
				new WeightVectorND(1.0, 1.0, 0.0), // yellow
				new WeightVectorND(0.0, 0.0, 1.0), // blue
				new WeightVectorND(1.0, 0.0, 0.0), // red
				new WeightVectorND(0.0, 1.0, 0.0), // green
				};*/
		
		config.samples = new AbstractWeightVector[]{
				new WeightVectorND(0.0, 0.0, 0.0), // black
				new WeightVectorND(0.0, 0.0, 1.0), // blue
				new WeightVectorND(0.0, 1.0, 0.0), // green
				new WeightVectorND(0.0, 1.0, 1.0), // 		
				new WeightVectorND(1.0, 0.0, 0.0), // red
				new WeightVectorND(1.0, 0.0, 1.0), // pink
				new WeightVectorND(1.0, 1.0, 0.0), // 
				new WeightVectorND(1.0, 1.0, 1.0), // white
				new WeightVectorND(0.2, 0.2, 0.2), // grey
				
				};

		som = new SelfOrganizingMap3D(config);
	}
	
	public void run() {
		
		int MAX_ITER = 100;
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
				
				draw(0);

			} else {
				break;
			}
		}
		State s = new State("save/3DRGB.som");
		s.save(som);	
		
		int z = 0;
		int inc = 1;
		while(true) {
			z += inc;
			if(z == config.dimZ - 1) {
				inc = -1;
			} else if (z == 0) {
				inc = 1;
			}
			
			draw(z);
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private void draw(int z) {
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, squareSize * dim, squareSize * dim);
			
			 // draw
			for(int y = 0; y < config.dimY; y++) {
				for(int x = 0; x < config.dimX; x++) {
					drawSquare(x, y, 
							som.getWeightVectors()[x][y][z].get(0), 
							som.getWeightVectors()[x][y][z].get(1), 
							som.getWeightVectors()[x][y][z].get(2), 
							g);
				}		
			}
			// and flip the buffer over
	
			g.dispose();
			strategy.show();
	}
	
	private void drawSquare(int x, int y, double r, double g, double b, Graphics2D graphics) {	
		graphics.setColor(new Color((float)r, (float)g, (float)b));
        Rectangle2D rectangle = new Rectangle2D.Float(x * squareSize, y * squareSize, squareSize, squareSize);
        graphics.fill(rectangle);
        
        graphics.draw(rectangle); 
	}
	
	
	private class KeyInputHandler extends KeyAdapter {

		public void keyPressed(KeyEvent e) {

		} 

		public void keyReleased(KeyEvent e) {

		}

		public void keyTyped(KeyEvent e) {
			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}
	
	
}
