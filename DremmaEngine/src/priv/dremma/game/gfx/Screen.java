package priv.dremma.game.gfx;

public class Screen {
	
	public float xOffset = 0;
	public float yOffset = 0;

	public int width;
	public int height;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setOffset(float xOffset, float yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

}
