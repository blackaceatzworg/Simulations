package model.environment;

public abstract class Area {
	
	public Area(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	
	private boolean isAccessible;
	
	public boolean isAccessible() {
		return isAccessible;
	}
	
}
