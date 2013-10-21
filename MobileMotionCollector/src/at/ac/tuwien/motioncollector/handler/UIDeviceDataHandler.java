package at.ac.tuwien.motioncollector.handler;

import java.util.HashSet;

import at.ac.tuwien.motioncollector.model.Device;
import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.ui.ApplicationContainer;

public class UIDeviceDataHandler extends AbstractQueuedDeviceDataHandler {

	private ApplicationContainer container;
	private HashSet<String> macAddresses;
	
	public UIDeviceDataHandler() {
		super();
		container =  ApplicationContainer.getInstance();
		macAddresses = new HashSet<String>();
		
		for(Device d:container.getDevicesPanel().getDeviceList()){
			macAddresses.add(d.getMacAddress());
		}
		
		
		
	}
	
	@Override
	public void perform(DeviceData data) {
		
		if(data.getMacAddress() != null && data.getMacAddress().length() != 0 && !macAddresses.contains(data.getMacAddress())){
			
				macAddresses.add(data.getMacAddress());
				container.getDevicesPanel().addDevice(new Device(data.getMacAddress()));
			
		}
		
		 
		

	}

	@Override
	public void closeChildren() {

	}

}
