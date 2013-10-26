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
	
	public HostAddressBroadcastListener(int port,String key, InetAddress hostAddress) throws SocketException {
		this.address = hostAddress;
		this.receiver = new OSCPortIn(port);
		this.receiver.addListener(key, this);
		this.receiver.startListening();
	}

	
	public void close(){
		if(this.receiver.isListening()){
			this.receiver.stopListening();
		}
		this.receiver.close();
	}
	
	public void acceptMessage(Date time, OSCMessage message) {
		Object[] param = message.getArguments();

		try {
			if(param[0] instanceof String){
				address = Inet4Address.getByName((String)param[0]);
				if(address!= null){
					this.receiver.stopListening();
				}
			}
		} catch (UnknownHostException e) {
			Log.e(LOGGING_TAG, "Broadcasted address is wrong.", e);
		}
		
	}
	
}