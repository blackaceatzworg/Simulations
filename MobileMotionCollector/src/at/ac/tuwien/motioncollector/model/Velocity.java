package at.ac.tuwien.motioncollector.model;

import java.io.Serializable;

public class Velocity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -440165483424429948L;
	private float x,y,z;
	
	public Velocity(float x, float y , float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public float getAbs(){
		return (float) Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
	}
	
}
