package at.ac.tuwien.motioncapture.model;

import java.util.List;

public abstract class DeviceData {
	protected String macAddress;

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
	public abstract List<Object> getAsObjectList();
	
}
