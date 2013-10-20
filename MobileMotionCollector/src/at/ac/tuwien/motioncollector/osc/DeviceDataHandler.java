package at.ac.tuwien.motioncollector.osc;

import at.ac.tuwien.motioncollector.model.DeviceData;

public interface DeviceDataHandler extends Runnable {

	public void handleData(DeviceData data);
	public void close();
	public void start();
	public void stop();
}
