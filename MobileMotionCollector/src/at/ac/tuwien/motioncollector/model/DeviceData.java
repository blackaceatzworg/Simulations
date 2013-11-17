package at.ac.tuwien.motioncollector.model;

import java.io.Serializable;
import java.util.Date;

public class DeviceData implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4019230672434053628L;
	private Velocity velocity;
	
	public Velocity getVelocity() {
		return velocity;
	}
	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}
	
	private String macAddress;
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	private Date receiveDate;
	
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("x: %.2f y: %.2f z: %.2f MAC:%s", this.velocity.getX(),this.velocity.getY(),this.velocity.getZ(),this.macAddress);
	}
	
	
}
