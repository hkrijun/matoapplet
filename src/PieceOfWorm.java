import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("unused")
public class PieceOfWorm extends CoordinationSystem {
	// direction 0 = up, 1 = right, 2 = down, 3 = left
	private int direction; 
	
	public PieceOfWorm(int pX, int pY, int pDirection) {
		set(pX, pY, pDirection);
	}
	
	public void set(int pX, int pY, int pDirection) {
		setX(pX);
		setY(pY);
		setDirection(pDirection);
	}

	public int direction() { return direction; }

	public void setDirection(int pDirection) { direction = pDirection; }
	
	public int width() {	
		if( direction == Settings.directionUp || direction == Settings.directionDown )
			return Settings.wormSize;
		
		return Settings.gridSize;
	}
	
	public int height() {
		if( direction == Settings.directionLeft || direction == Settings.directionRight )
			return Settings.wormSize;
		
		return Settings.gridSize;
	}
	
	public int screenX() {
		if( direction == Settings.directionUp || direction == Settings.directionDown )
			return x() - (Settings.gridSize / 2 + (Settings.gridSize - Settings.wormSize) / 2);
		
		return x() - Settings.gridSize / 2;
	}
	
	public int screenY() {
		if( direction == Settings.directionLeft || direction == Settings.directionRight )
			return y() - (Settings.gridSize / 2 + (Settings.gridSize - Settings.wormSize) / 2);
		
		return y() - Settings.gridSize / 2;
	}
	
	public void move() {
		if( direction == Settings.directionUp )
			setY( y() - Settings.gridSize );
		if( direction == Settings.directionDown )
			setY( y() + Settings.gridSize );
		if( direction == Settings.directionLeft )
			setX( x() - Settings.gridSize );
		if( direction == Settings.directionRight)
			setX( x() + Settings.gridSize );
	}
	
	public void draw(Graphics g, int type, int nextDirection) {
		if( type == Settings.typeEnd ) {
			int prevDirection = direction;
			
			if( nextDirection != direction )
				direction = nextDirection;
				
			g.fillRoundRect(screenX(), screenY(), width(), height(), Settings.wormArc, Settings.wormArc);
			
			if( direction == Settings.directionUp )
				g.fillRect(screenX(), screenY(), width(), height() - Settings.gridSize / 2);
			if( direction == Settings.directionDown  )
				g.fillRect(screenX(), screenY() + Settings.gridSize / 2, width(), height() - Settings.gridSize / 2);
			if( direction == Settings.directionRight  )
				g.fillRect(screenX() + Settings.gridSize / 2, screenY(), width() - Settings.gridSize / 2, height());
			if( direction == Settings.directionLeft  )
				g.fillRect(screenX(), screenY(), width() - Settings.gridSize / 2, height());
			
			if( prevDirection != direction ) direction = prevDirection;
		} else if( type == Settings.typeStart) {
			int offsetX = 0, offsetY = 0;
			
			if( direction == Settings.directionUp )
				offsetY = + (Settings.gridSize - Settings.wormSize);
			
			if( direction == Settings.directionDown )
				offsetY = - (Settings.gridSize - Settings.wormSize);
			
			if( direction == Settings.directionLeft )
				offsetX = + (Settings.gridSize - Settings.wormSize);
			
			if( direction == Settings.directionRight )
				offsetX = - (Settings.gridSize - Settings.wormSize);
			
			g.fillRoundRect(screenX() + offsetX, screenY() + offsetY, width(), height(), Settings.wormArc, Settings.wormArc);
			
			if( direction == Settings.directionUp )
				g.fillRect(screenX(), screenY() + Settings.gridSize / 2, width(), height() - Settings.gridSize / 2);
			if( direction == Settings.directionDown  )
				g.fillRect(screenX(), screenY(), width(), height() - Settings.gridSize / 2);
			if( direction == Settings.directionRight  )
				g.fillRect(screenX(), screenY(), width() - Settings.gridSize / 2, height());
			if( direction == Settings.directionLeft  )
				g.fillRect(screenX() + Settings.gridSize / 2, screenY(), width() - Settings.gridSize / 2, height());
		} else {
			if( nextDirection == direction )
				g.fillRect(screenX(), screenY(), width(), height());
			else {
				int turnSize = (Settings.gridSize - Settings.wormSize) / 2;
				
				// Left and right
				
				if( direction == Settings.directionLeft )
					g.fillRect(screenX() - turnSize, screenY(), width() + turnSize, height());
				
				if( direction == Settings.directionRight )
					g.fillRect(screenX(), screenY(), width() - turnSize * 3, height());
				
				// Up and down
				
				if( direction == Settings.directionUp )
					g.fillRect(screenX(), screenY() - turnSize, width(), height() + turnSize);
				
				if( direction == Settings.directionDown )
					g.fillRect(screenX(), screenY(), width(), height() - turnSize * 3);
				
				// Next dir
				
				if( direction == Settings.directionLeft || direction == Settings.directionRight ) {
					if( nextDirection == Settings.directionUp )
						g.fillRect(screenX(), screenY(), width() - turnSize * 3, height());
					if( nextDirection == Settings.directionDown )
						g.fillRect(screenX() - turnSize, screenY() + turnSize * 2, width() - turnSize * 2, height() + turnSize);
				} else {
					if( nextDirection == Settings.directionLeft )
						g.fillRect(screenX(), screenY(), width(), height() - turnSize * 3);
					if( nextDirection == Settings.directionRight )
						g.fillRect(screenX() + turnSize * 2, screenY() - turnSize, width() + turnSize, height() - turnSize * 2);
				}
			}
		}
	}
}
