
public class Fruit extends CoordinationSystem {
	private int fruit;
	
	public Fruit(int pFruit, int pX, int pY) { setFruit(pFruit); setPos(pX, pY); }
	
	public void setFruit(int pFruit) { fruit = pFruit; };
	public int fruit() { return fruit; }
	
	public int score() {
		if( fruit == Settings.fruitApple ) return Settings.fruitAppleScore;
		if( fruit == Settings.fruitBanana ) return Settings.fruitBananaScore;
		
		// Must be a paron
		return Settings.fruitParonScore;
	}
}
