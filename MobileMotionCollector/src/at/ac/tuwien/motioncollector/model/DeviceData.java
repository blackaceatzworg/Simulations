package at.ac.tuwien.motioncollector.model;

import java.util.Date;

public class DeviceData {
	
	
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
	
}
