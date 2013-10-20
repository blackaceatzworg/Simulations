package at.ac.tuwien.motioncollector.osc;

import java.net.SocketException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.ac.tuwien.motioncollector.Settings;
import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.model.Velocity;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class DeviceDataListener implements Runnable {

	private List<DeviceDataHandler> handlers;
	private OSCPortIn receiver;

	private Queue<DeviceData> dataQueue;

	private boolean isRunning = false;
	private boolean stop = false;

	public DeviceDataListener() {
		this.handlers = new LinkedList<DeviceDataHandler>();
		this.dataQueue = new ConcurrentLinkedQueue<DeviceData>();

		try {
			receiver = new OSCPortIn(Integer.parseInt(Settings
					.getValue("oscportin")));

			// Register Listener for Velocity Data
			receiver.addListener("/devicedata", new OSCListener() {

				@Override
				public void acceptMessage(Date time, OSCMessage message) {

					Object[] parameter = message.getArguments();

					DeviceData data = new DeviceData();

					data.setReceiveDate(time);
					if (parameter[0].getClass() == Float.class
							&& parameter[1].getClass() == Float.class
							&& parameter[2].getClass() == Float.class
							&& parameter[3].getClass() == String.class) {
						data.setVelocity(new Velocity((Float) parameter[0],
								(Float) parameter[1], (Float) parameter[2]));
						data.setMacAddress(parameter[3].toString());
					}

					DeviceDataListener.this.receiveData(data);

				}
			});

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void registerHandler(DeviceDataHandler handler) {
		this.handlers.add(handler);
	}

	public void unregisterHandler(DeviceDataHandler handler) {
		this.handlers.remove(handler);
	}

	private void receiveData(DeviceData data) {
		for (DeviceDataHandler handler : this.handlers) {
			handler.handleData(data);
		}
	}

	public void close() {
		this.isRunning = false;
		for (DeviceDataHandler handler : this.handlers) {
			handler.close();
		}

	}

	public void stopListening() {
		this.isRunning = false;

		for (DeviceDataHandler handler : this.handlers) {
			handler.stop();
		}

		this.receiver.stopListening();
	}

	public void startListening() {

		this.receiver.startListening();
		for (DeviceDataHandler handler : this.handlers) {
			handler.stop();
		}
		this.isRunning = true;
	}

	@Override
	public void run() {
		while (!this.stop) {

			if (!this.isRunning) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (this.dataQueue.isEmpty()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					this.receiveData(this.dataQueue.poll());
				}
			}

		}

	}

}
