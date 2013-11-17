package at.ac.tuwien.motioncollector.handler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.osc.DeviceDataHandler;

public abstract class AbstractDeviceDataHandler implements
		DeviceDataHandler {

	private Queue<DeviceData> queue;
	
	private boolean paused = false;
	
	private boolean stop = false;
	private boolean run = false;

	public AbstractDeviceDataHandler() {
		this.queue = new ConcurrentLinkedQueue<DeviceData>();
	}

	@Override
	public void run() {
		while (!this.stop) {
			if (this.run) {

				DeviceData data = this.queue.poll();
				if(data!=null){
					perform(data);
				}else{
					this.pause(30, false);
				}
			}

		}

	}

	public void close() {
		this.stop = true;
		this.closeChildren();
	}

	@Override
	public void start(){
		this.run = true;
		
		
	}

	@Override
	public void stop() {
		this.run = false;

	}
	
	public void queue(DeviceData data){
		if(!this.paused){
			this.queue.add(data);
		}
		
	}

	public abstract void perform(DeviceData data);

	public abstract void closeChildren();
	
	public void pause(long millis, boolean delete){
		this.paused = true && delete;
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
		if(delete){
			this.queue.clear();
		}
		this.paused = false ;
		
	}

	public boolean isPaused() {
		return paused;
	}
	
}
