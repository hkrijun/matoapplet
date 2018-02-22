/*
 * Has a bug in location / collision checking. Temp fixed by drawing the apple to a false position.
 */

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

@SuppressWarnings("unused")
public class Fruits implements Runnable {
	private int screenWidth, screenHeight;
	
	public ArrayList<Fruit> fruits = new ArrayList<Fruit>();
	private Image[] images = new Image[Settings.differentFruits];
	
	private Thread fruitSpawner;
	private boolean fruitSpawning = true;
	private Random random = new Random(9482948);
	
	public Fruits(int pScreenWidth, int pScreenHeight) {
		screenWidth = pScreenWidth;
		screenHeight = pScreenHeight;

		images[Settings.fruitApple] = getImage("apple.png");
		images[Settings.fruitParon] = getImage("paron.png");
		images[Settings.fruitBanana] = getImage("banana.png");
		
		fruitSpawner = new Thread(this);
		fruitSpawner.start();
	}
	
	public int count() { return fruits.size(); }
	
	public Image image(int index) { return images[ fruits.get(index).fruit() ]; }
	
	public int x(int index) { return fruits.get(index).x(); }
	public int y(int index) { return fruits.get(index).y(); }
	
	public void add(int fruitType, int x, int y) { fruits.add( new Fruit(fruitType, x, y) ); }
	public int eat(int index) { fruits.remove(index); return 25; }
	
	public Fruit fruit(int index) { return fruits.get(index); }

	@Override
	public void run() {
		while( fruitSpawning ) {
			boolean coolToSpawn = false;
			int posX = 0, posY = 0;
			
			int nextType = random.nextInt(Settings.differentFruits);
			
			do {
				posX = random.nextInt(screenWidth / Settings.gridSize) * Settings.gridSize;
				posY = random.nextInt(screenHeight / Settings.gridSize) * Settings.gridSize;

				coolToSpawn = true;
				
				for( Fruit fruit : fruits ) {
					if( fruit.positionCheck(posX, posY) ) {
						coolToSpawn = false;
						break;
					}
				}
			} while( !coolToSpawn );
			
			add(nextType, posX, posY);
			
			try
	         {
	            Thread.sleep( Settings.fruitSpawnTime );  
	         }
	         catch ( InterruptedException  caught_exception ) { }
		}
	}
	
	public void stop() {
		fruitSpawning = false;
	}
	
	public void draw(Graphics g) {
		for( int i = 0 ; i < count() ; i++ )
   			g.drawImage(image(i), x(i) - Settings.gridSize, y(i) - Settings.gridSize, null);
	}
	
	public Image getImage(String imageFileName) {
		URL imageURL = getClass().getResource(imageFileName);

		if (imageURL == null) {
			// or otherwise handle the error
			System.out.println("No image named " + imageFileName);
			return null;
		}
		
		Toolkit tk = Toolkit.getDefaultToolkit();

		Image img = null;
		
		try {
			img = tk.createImage((java.awt.image.ImageProducer) imageURL.getContent());
		} catch (java.io.IOException ex) {
			System.out.println(ex);
		}
		
		System.out.println("Image ready: " + imageFileName);
		
		return img;
	}
}
