
public abstract class CoordinationSystem {
	private int x, y;
	
	public void setPos(int pX, int pY) { setX(pX); setY(pY); }
	
	public void setX(int pX) { x = pX / Settings.gridSize; x *= Settings.gridSize; } //x += Settings.gridSize / 2; }
	public void setY(int pY) { y = pY / Settings.gridSize; y *= Settings.gridSize; } // y += Settings.gridSize / 2; }
	
	public int x() { return x; }
	public int y() { return y; }

	public boolean collisionCheck(CoordinationSystem otherObject) {
		if( otherObject.x() == x() && otherObject.y() == y() )
			return true;

		return false;
	}
	
	public boolean positionCheck(int startX, int startY, int endX, int endY) {
		if( x() <= startX || x() >= endX || y() <= startY || y() >= endX )
			return false;
		
		return true;
	}
	
	public boolean positionCheck(int pX, int pY) {
		pX = pX / Settings.gridSize; pX *= Settings.gridSize;
		pY = pY / Settings.gridSize; pY *= Settings.gridSize;
		
		if( x() == pX && y() == pY )
			return true;
		
		return false;
	}
}
