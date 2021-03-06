package at.ac.tuwien.motioncapture.listeners;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import android.util.Log;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public final class HostAddressBroadcastListener implements OSCListener{

	private OSCPortIn receiver;
	private InetAddress address;
	
	private static final String LOGGING_TAG = "HostAddressBroadcastListener";
	
	public HostAddressBroadcastListener(int port,String key) throws SocketException {
		this.receiver = new OSCPortIn(port);
		this.receiver.addListener(key, this);
		this.receiver.startListening();
	}

	
	public void close(){
		if(this.receiver==null){
			return;
		}
		if(this.receiver.isListening()){
			this.receiver.stopListening();
		}
		this.receiver.close();
		this.receiver = null;
	}
	
	public boolean isRunning(){
		if(this.receiver==null){
			return false;
		}
		if(!this.receiver.isListening()){
			return false;
		}
		return true;
	}
	public InetAddress getAddress() {
		return address;
	}
	
	public void acceptMessage(Date time, OSCMessage message) {
		Object[] param = message.getArguments();

		try {
			if(param[0] instanceof String){
				address = Inet4Address.getByName((String)param[0]);
				if(address!= null){
					this.receiver.stopListening();
					this.receiver.close();
				}
			}
		} catch (UnknownHostException e) {
			Log.e(LOGGING_TAG, "Broadcasted address is wrong.", e);
		}
		
	}
	
}