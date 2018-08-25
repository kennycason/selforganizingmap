package som.gui;

import som.SelfOrganizingMap2D;
import som.SelfOrganizingMapConfig;
import som.features.AbstractWeightVector;
import som.features.WeightVector;
import som.map.AbstractMapLocation;
import som.save.State;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SelfOrganizingMapRGBViewerImageWeightVector3D extends Canvas {

	SelfOrganizingMap2D som;

	SelfOrganizingMapConfig config;

	private int squareSize = 10;

	private int dim = 40;

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/** The stragey that allows us to use accelerate page flipping */

	private BufferStrategy strategy;

	public static void main(String argv[]) throws IOException {
		SelfOrganizingMapRGBViewerImageWeightVector3D somv =new SelfOrganizingMapRGBViewerImageWeightVector3D();

		somv.init();

		somv.run();
	}

	public SelfOrganizingMapRGBViewerImageWeightVector3D() {
		// create a frame 
		JFrame container = new JFrame("Self Organizing Map 2D Viewer");
				
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
	
	public void init() throws IOException {
		config = new SelfOrganizingMapConfig();
		config.weightVectorDimension = 3;
		config.radius = 50;
		config.dimX = dim;
		config.dimY = dim;

		config.samples = loadFeatures();
		

		som = new SelfOrganizingMap2D(config);
	}

	private AbstractWeightVector[] loadFeatures() throws IOException {
		final List<AbstractWeightVector> features = new ArrayList<>();
		final BufferedImage bufferedImage = loadImage();
		for (int x = 0; x < bufferedImage.getWidth(); x += 5) {
			for (int y = 0; y < bufferedImage.getHeight(); y += 5) {
				final int rgb = bufferedImage.getRGB(x, y);
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;

				final WeightVector vector = new WeightVector(red / 255.0, green / 255.0, blue / 255.0);

				System.out.println(vector);
				features.add(vector);
			}
		}
		System.out.println("features size: " + features.size());
		return features.toArray(new AbstractWeightVector[features.size()]);
	}

	private BufferedImage loadImage() throws IOException {
		return ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("me.jpg"));
	}
	
	public void run() {
		
		int MAX_ITER = 500;
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
				
				draw();

			} else {
				break;
			}
		}
		State s = new State("save/2DRGB.som");
		s.save(som);
	//	System.exit(0);

	}
	
	private void draw() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, squareSize * dim, squareSize * dim);
		
		 // draw
		for(int y = 0; y < config.dimY; y++) {
			for(int x = 0; x < config.dimX; x++) {
				drawSquare(x, y,
						som.getWeightVectors()[x][y].get(0),
						som.getWeightVectors()[x][y].get(1),
						som.getWeightVectors()[x][y].get(2),
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
