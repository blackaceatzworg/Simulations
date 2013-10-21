package at.ac.tuwien.motioncollector.handler;

import java.text.SimpleDateFormat;

import at.ac.tuwien.motioncollector.model.DeviceData;

public class DeviceDataConsoleWriter extends AbstractQueuedDeviceDataHandler {

	private static SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");;
	
	
	@Override
	public void perform(DeviceData data) {
		System.out.println(sdf.format(data.getReceiveDate())+" "+data.toString());
		
	}

	@Override
	public void closeChildren() {
		
	}



}
