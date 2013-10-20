package at.ac.tuwien.motioncollector.handler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.osc.DeviceDataHandler;

public abstract class AbstractQueuedDeviceDataHandler implements
		DeviceDataHandler {

	private Queue<DeviceData> queue;

	private boolean stop = false;
	private boolean run = false;

	public AbstractQueuedDeviceDataHandler() {
		this.queue = new ConcurrentLinkedQueue<DeviceData>();
	}

	@Override
	public void run() {
		while (!this.stop) {
			if (this.run) {
				DeviceData data;
				if ((data = this.queue.poll()) != null) {
					this.perform(data);
				} else {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	public void close() {
		this.stop = true;
		this.closeChildren();
	}

	@Override
	public void start() {
		this.run = true;

	}

	@Override
	public void stop() {
		this.run = false;

	}

	@Override
	public void handleData(DeviceData data) {
		this.queue.add(data);
	}

	public abstract void perform(DeviceData data);

	public abstract void closeChildren();
}
