package at.ac.tuwien.motioncapture.tasks;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.AsyncTask;
import android.util.Log;
import at.ac.tuwien.motioncapture.model.DeviceData;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;


public class SendOSCMessageTask<V extends DeviceData> extends AsyncTask<Void, Void, Void> {

	private OSCPortOut sender;
	private ConcurrentLinkedQueue<V> messageQueue;
	
	private String macAddress;
	private String oscKey;
	
	public SendOSCMessageTask(ConcurrentLinkedQueue<V> messageQueue, InetAddress address, int port, String macAddress, String oscKey) throws SocketException {
		this.messageQueue = messageQueue;
		this.sender = new OSCPortOut(address,port);
		this.macAddress = macAddress;
		this.oscKey = oscKey;
	}
	
	private boolean isRunning = true;
	public void stop(){
		this.isRunning = false;
		this.sender.close();
	}
	
	@Override
	protected Void doInBackground(Void...params) {	
		while(isRunning && this.messageQueue != null){
			while(this.messageQueue.size() == 0){
				try {
					Thread.sleep(10);
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
		return null;
	}

}
