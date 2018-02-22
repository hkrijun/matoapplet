import java.awt.* ;

import javax.imageio.ImageIO;
import javax.swing.* ;
import java.awt.event.* ;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings({ "serial", "unused" })
class MatopeliPanel extends JPanel implements Runnable, KeyListener
{
	private int screenWidth, screenHeight;
	private static int statsPanelHeight = 40;

	// Graphics
	Image imageSplatter;
	Image imageBG;
	Image imagePanel;
	
	Font fontUi; // = new Font("Serif", Font.BOLD, 30);
	
	// Wormy stuff
	private ArrayList<PieceOfWorm> wormPiece = new ArrayList<PieceOfWorm>();
	private int wormDirection = Settings.directionRight;
	private int wormNextDirection = Settings.directionRight;
	private int wormLength = 10; // obsolete
	private boolean wormAlive = true, wormGotKilled = false, splat = false; // Splat ejected?
	private int wormSpeed = Settings.wormInitialSpeed;
	
	private int currentLevel = 1;
	private int score = 0;
	
	Thread wormThread;
	
	// Fruits
	private Fruits fruits;
	
	public MatopeliPanel(int pWidth, int pHeight)
	{
		screenWidth = pWidth;
		screenHeight = pHeight - statsPanelHeight;
		
		// Graphics    
		imageSplatter = getImage("splatter128.png");
		imageBG = getImage("bg.png");
		imagePanel = getImage("panel.png");
		
		fontUi = getFont("ka1.ttf");
		fontUi = fontUi.deriveFont(20f);
		
		// Setting up game
		fruits = new Fruits(screenWidth, screenHeight);
		
		for( int i = 0 ; i < wormLength ; i++ )
			wormPiece.add( new PieceOfWorm(screenWidth / 2 + Settings.gridSize * i, screenHeight / 2, wormDirection) );
		
		// Starting background operations
		addKeyListener(this);
		
		wormThread = new Thread(this);
		wormThread.start();
	}
	
	public void paint(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		super.paint( g ) ;
		
		// Background
		g.drawImage(imageBG, 0, 0, screenWidth, screenHeight, null);
		
		// Leave a pool of blood incase the worm DIES
		if( wormGotKilled || !splat ) {
			int offset = (Settings.gridSize - Settings.wormSize);
			g.drawImage(imageSplatter, wormPiece.get(wormLength-1).x() - 32 - offset, wormPiece.get(wormLength-1).y() - 32 - offset, 64, 64, null);
			splat = true;
		}
		
		// Fruits (fruits overlapping with the worm must be eaten! No scores for it though.)
   		for( int i = 0 ; i < fruits.count()-1 ; i++ )
   			for( int i2 = 0 ; i2 < wormLength-1 ; i2++ )
   				if( fruits.fruit(i).collisionCheck( wormPiece.get(i2) ) )
   					fruits.eat(i);
		
		fruits.draw(g);
		
		// Worm
		g.setColor( Settings.wormColor ) ;
   		
   		for( int i = 0 ; i < wormLength ; i++ ) {
   			float[] hsbValues = new float[3];
   			hsbValues = Color.RGBtoHSB(100 + (int)(138.0f * ( (float)i / (float)wormLength )), 0, 0, hsbValues);
   			
   			g.setColor( Color.getHSBColor( hsbValues[0] , hsbValues[1], hsbValues[2] ) );
   			
   			if( i == 0 )
   				wormPiece.get(i).draw(g, Settings.typeEnd, wormPiece.get(i+1).direction());
   			else if( i == wormLength - 1)   				
   				wormPiece.get(i).draw(g, Settings.typeStart, 0);
   			else
   				wormPiece.get(i).draw(g, Settings.typeMiddle, wormPiece.get(i+1).direction() );
   		}
   		
   		// Panel background
   		g.drawImage(imagePanel, 0, screenHeight, screenWidth, statsPanelHeight, null);
   		
   		// Score
   		String strScore = new String();
   		strScore = Integer.toString(score);
   		
   		while (strScore.length() <= 4 ) {
   			String strTmp = '0' + strScore;
   			strScore = strTmp;
   		}
   		
   		g.setColor(Color.white);
   		g.setFont(fontUi);
   		g.drawString(strScore, 136, screenHeight + statsPanelHeight / 2 + 6 );
   		
   		// Level
   		String strLevel = new String();
   		strLevel = Integer.toString( currentLevel() );
   		
   		while (strLevel.length() <= 4 ) {
   			String strTmp = '0' + strLevel;
   			strLevel = strTmp;
   		}
   		
   		g.drawString(strLevel, 429, screenHeight + statsPanelHeight / 2 + 6 );
   		
   		// Brings back ... focus?
   		if ( ! hasFocus() )
	   		requestFocus() ;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_UP && wormDirection != Settings.directionDown)
			wormNextDirection = Settings.directionUp;
		if( e.getKeyCode() == KeyEvent.VK_RIGHT && wormDirection != Settings.directionLeft )
			wormNextDirection = Settings.directionRight;
		if( e.getKeyCode() == KeyEvent.VK_DOWN && wormDirection != Settings.directionUp )
			wormNextDirection = Settings.directionDown;
		if( e.getKeyCode() == KeyEvent.VK_LEFT && wormDirection != Settings.directionRight )
			wormNextDirection = Settings.directionLeft;
	}

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void run() {
		int wormCounter = 0, fruitCounter = 0;
		
		while( wormAlive ) {
			wormDirection = wormNextDirection;
			
			for( wormCounter = 0 ; wormCounter < (wormPiece.size()-1) ; wormCounter++ ) {
				wormPiece.get(wormCounter).setDirection(wormPiece.get(wormCounter+1).direction());
				wormPiece.get(wormCounter).move();
			}
			// Head
			wormPiece.get(wormCounter).setDirection(wormDirection);
			wormPiece.get(wormCounter).move();

			// Fruit collision checking
			for( fruitCounter = 0 ; fruitCounter < (fruits.count()-1) ; fruitCounter++ )
				if( wormPiece.get(wormCounter).collisionCheck( fruits.fruit(fruitCounter) ) ) {
					//System.out.println( "Fruit: " + fruitCounter );
					int placeToAdd = 0, placeX = wormPiece.get(placeToAdd).x(), placeY = wormPiece.get(placeToAdd).y();
					
					if( wormPiece.get(placeToAdd).direction() == Settings.directionUp )
						placeY += Settings.gridSize;
					
					if( wormPiece.get(placeToAdd).direction() == Settings.directionDown )
						placeY -= Settings.gridSize;
					
					if( wormPiece.get(placeToAdd).direction() == Settings.directionLeft )
						placeX += Settings.gridSize;
					
					if( wormPiece.get(placeToAdd).direction() == Settings.directionRight )
						placeX -= Settings.gridSize;
					
					wormPiece.add(placeToAdd, new PieceOfWorm(placeX, placeY, wormPiece.get(placeToAdd).direction()) );
					wormLength ++;
					
					score += fruits.fruit(fruitCounter).score();
					fruits.eat(fruitCounter);
					
					if( score % Settings.scoreMilestones == 0 ) {
						currentLevel++;
						wormSpeed += speedIncrease();
						//System.out.println(currentLevel + ": speedy: " + wormSpeed + ", " + speedIncrease());
					}
					
					if( wormSpeed > Settings.wormMaxReachableSpeed )
						wormSpeed = Settings.wormMaxReachableSpeed;
				}
			
			// Self collision checking
			for( wormCounter = 0 ; wormCounter < (wormPiece.size()-1) ; wormCounter++ )
				if( wormPiece.get(wormPiece.size()-1).collisionCheck( wormPiece.get(wormCounter) ) ) {
					//System.out.println( "Hit: " + wormCounter );
					wormDies();
				}
			
			// Wall checking
			if( !wormPiece.get(wormPiece.size()-1).positionCheck(0, 0, screenWidth, screenHeight) ) {
				wormDies();
			}
			
			try
	         {
	            Thread.sleep( Settings.wormMaxSpeed - wormSpeed ) ;  
	         }
	         catch ( InterruptedException  caught_exception )
	         {
	         }
	         
	         repaint();
		}
	}
	
	private void wormDies() {
		wormAlive = false;
		wormGotKilled = true; // For one run(/event)
		
		fruits.stop();
	}
	
	private int currentLevel() { return currentLevel; }
	
	private int speedIncrease() {
		return 1 + Settings.wormSpeedIncreasePerLevel * (20 / (currentLevel));
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
	
	public Font getFont(String fontFileName) {
		URL imageURL = getClass().getResource(fontFileName);

		if (imageURL == null) {
			// or otherwise handle the error
			System.out.println("No font named " + fontFileName);
			return null;
		}
		
		Toolkit tk = Toolkit.getDefaultToolkit();

		Font fnt = null;

		try {
		     fnt = Font.createFont(Font.TRUETYPE_FONT, (InputStream) imageURL.getContent() );
		} catch (IOException ex) { System.out.println(ex.getMessage() ); } catch (FontFormatException e) {
			// TODO Auto-generated catch block
			System.out.println( e.getMessage() );
		}

		System.out.println("Font ready: " + fontFileName);
		
		return fnt;
	}
}

@SuppressWarnings("serial")
public class matoapplet extends JApplet {
	public void init()
	{
		System.out.println("SNAKE SPANK ON!");
		getContentPane().add( new MatopeliPanel(getSize().width, getSize().height) ) ; 
	}
}
