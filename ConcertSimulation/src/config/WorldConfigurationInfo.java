package config;

public class WorldConfigurationInfo {
	private float width;
	private float height;
	private float cellUnits;
	private float gravity;
	
	public WorldConfigurationInfo(float width, float height, float cellUnits, float gravity) {
		super();
		this.width = width;
		this.height = height;
		this.cellUnits = cellUnits;
		this.gravity = gravity;
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
	
	
	
	
}
