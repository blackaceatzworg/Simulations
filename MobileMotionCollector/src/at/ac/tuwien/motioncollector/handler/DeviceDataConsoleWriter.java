package at.ac.tuwien.motioncollector.handler;

import at.ac.tuwien.motioncollector.model.DeviceData;

public class DeviceDataConsoleWriter extends AbstractQueuedDeviceDataHandler {

	@Override
	public void perform(DeviceData data) {
		System.out.println(data.toString());
		
	}

	@Override
	public void closeChildren() {
		
	}



}
