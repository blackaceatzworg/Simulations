package at.ac.tuwien.motioncapture.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import android.os.AsyncTask;

public class SendOSCMessageTask extends AsyncTask<Void, Void, Void> {

	private OSCPortOut sender;
	private ConcurrentLinkedQueue<OSCMessage> messageQueue;
	
	public SendOSCMessageTask(String ipAddr) throws SocketException, UnknownHostException {
		this.sender = new OSCPortOut(InetAddress.getByName(ipAddr),8000);
		this.messageQueue = new ConcurrentLinkedQueue<OSCMessage>();
	}
	
	public void appendMessage(OSCMessage message){
		messageQueue.add(message);
	}
	
	
	private boolean isRunning = true;
	public void stop(boolean cancel){
		if(cancel){
			this.cancel(true);
		}else{
			this.isRunning = false;
		}
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		if(this.sender== null){
			this.isRunning = false;
		}
		
		while(isRunning){
			while(this.messageQueue.size() == 0){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				sender.send(this.messageQueue.poll());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		// TODO Auto-generated method stub
		return null;
	}
}
