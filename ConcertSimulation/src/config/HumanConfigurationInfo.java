package config;

import model.actors.Sex;
import repast.simphony.space.continuous.NdPoint;

public class HumanConfigurationInfo {
	private float size;
	private float weight;
	private Sex sex;
	private NdPoint position;
	
	
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	public NdPoint getPosition() {
		return position;
	}
	public void setPosition(NdPoint position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return String.format("size: %.2f; weight: %.2f; sex: %s", size, weight, sex);
	}
	
	
	
}
