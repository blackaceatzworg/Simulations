package config;

public class WorldConfigurationInfo {
	private float width;
	private float height;
	private float cellUnits;
	private float gravity;
	private int fps;
	
	
	public WorldConfigurationInfo(float width, float height, float cellUnits, float gravity,int fps) {
		super();
		this.width = width;
		this.height = height;
		this.cellUnits = cellUnits;
		this.gravity = gravity;
		this.fps = fps;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getCellUnits() {
		return cellUnits;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getGravity() {
		return gravity;
	}

	public int getFps() {
		return fps;
	}

	
	
	
	
}
