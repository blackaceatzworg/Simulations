package at.ac.tuwien.motioncollector.model;

public class Device {
	
	public Device(String macAddress){
		this.macAddress = macAddress;
	}
	
	private String macAddress;
	public String getMacAddress() {
		return macAddress;
	}
	
	@Override
	public String toString() {
		return this.macAddress;
	}
}
