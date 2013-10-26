package at.ac.tuwien.motioncollector.handler;

import java.util.LinkedList;
import java.util.List;

import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.ui.DeviceDataWindow;

public class DeviceDataWindowHandler extends AbstractQueuedDeviceDataHandler {

	private List<DeviceDataWindow> windows;
	
	public DeviceDataWindowHandler() {
		super();
		this.windows = new LinkedList<DeviceDataWindow>();
	}
	
	@Override
	public void perform(DeviceData data) {
		
		if(this.windows.size()==0){
			this.pause(500);
		}else{
			for(DeviceDataWindow ddw : this.windows){
			if(ddw.getDevice().getMacAddress().equals(data.getMacAddress())){
				ddw.addData(data);
			}
		}
		}
		
		

	}
	
	public void registerWindow(DeviceDataWindow window){
		this.windows.add(window);
	}

	public void unregisterWindow(DeviceDataWindow window){
		for(DeviceDataWindow w : this.windows){
			if(w.getDevice().equals(window.getDevice())){
				this.windows.remove(w);
				break;
			}
		}
	}
	
	@Override
	public void closeChildren() {
		// TODO Auto-generated method stub

	}

}
