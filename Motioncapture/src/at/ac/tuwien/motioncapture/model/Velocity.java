package at.ac.tuwien.motioncapture.model;

import java.util.ArrayList;
import java.util.List;

public class Velocity extends DeviceData {
	float x, y, z;

	public Velocity() {
		super();
	}

	public Velocity(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public List<Object> getAsObjectList() {
		List<Object> list = new ArrayList<Object>(4);
		list.add(0, x);
		list.add(1, y);
		list.add(2, z);
		list.add(3, macAddress);
		return list;
	}

}
