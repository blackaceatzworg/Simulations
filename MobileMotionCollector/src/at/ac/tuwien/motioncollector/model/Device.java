package at.ac.tuwien.motioncollector.model;

public class Device implements Comparable<Device> {
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((macAddress == null) ? 0 : macAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Device))
			return false;
		Device other = (Device) obj;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		return true;
	}

	@Override
	public int compareTo(Device o) {
		return this.macAddress.compareTo(o.getMacAddress());
	}
	
	
	
}
