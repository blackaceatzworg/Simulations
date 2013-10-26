package at.ac.tuwien.motioncapture.tasks;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import android.util.Log;
import at.ac.tuwien.motioncapture.model.DeviceData;

public class DeviceDataSender<V extends DeviceData> implements Runnable {

	private OSCPortOut sender;
	private ConcurrentLinkedQueue<V> messageQueue;
	
	private String macAddress;
	private String oscKey;
	private InetAddress address;
	private int port;
	
	private static final String LOGGING_TAG = "Sender Thread";
	
	private boolean running;
	
	private Thread thread;
	
	public DeviceDataSender(ConcurrentLinkedQueue<V> messageQueue, InetAddress address, int port, String macAddress, String oscKey)  {
		this.messageQueue = messageQueue;
		this.address = address;
		this.port = port;
		this.macAddress = macAddress;
		this.oscKey = oscKey;
		this.running = false;
	}
	
	public void start() throws SocketException{
		this.running = true;
		this.sender = new OSCPortOut(address, port);
		this.thread = new Thread(this);
		this.thread.setPriority(Thread.MAX_PRIORITY);
		this.thread.start();
		
	}
	
	public void stop(){

		this.running = false;
	}
	
	public void run() {
		try{
			while(!running){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Log.i(LOGGING_TAG, "Thread interrupted while waiting for run.");
				}
			}
			while(running){
				while(this.messageQueue.size() == 0){
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						Log.e("Sender Thread", "Thread interrupted");
					}
				}
				try {
					DeviceData d = this.messageQueue.poll();
					d.setMacAddress(macAddress);
					OSCMessage message = new OSCMessage(this.oscKey,new ArrayList<Object>(d.getAsObjectList()));
					sender.send(message);
					
					
					
				} catch (IOException e) {
					Log.e("Sender Thread", "Message could not be sent",e);
				}
			}
		}finally{
			this.sender.close();
		}
		

	}

}
