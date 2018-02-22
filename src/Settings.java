import java.awt.Color;

public class Settings {
	public static int gridSize = 16;
	public static int wormSize = 10;
	public static Color wormColor = Color.getHSBColor(0, 1.0f, 0.93f);
	public static int wormArc = 8;
	public static int maxLength = 64 * 64;
	public static int wormMaxSpeed = 200;
	public static int wormMaxReachableSpeed = 180; // :F
	public static int wormInitialSpeed = 50;

	public static int typeStart = 0;
	public static int typeMiddle = 1;
	public static int typeEnd = 2;
	
	public static int directionUp = 0;
	public static int directionRight = 1;
	public static int directionDown = 2;
	public static int directionLeft = 3;
	
	public static int fruitApple = 0;
	public static int fruitParon = 1;
	public static int fruitBanana = 2;
	public static int differentFruits = 3;
	
	public static int fruitAppleScore = 5;
	public static int fruitParonScore = 10;
	public static int fruitBananaScore = 15;
	
	public static int fruitSpawnTime = 2000;
	public static int scoreMilestones = 20;
	public static int wormSpeedIncreasePerLevel = 1;
}
