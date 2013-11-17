package at.ac.tuwien.motioncollector.osc;

import java.io.IOException;
import java.net.SocketException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import at.ac.tuwien.motioncollector.Settings;
import at.ac.tuwien.motioncollector.model.DeviceData;
import at.ac.tuwien.motioncollector.model.Velocity;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class DeviceDataListener{

	private Set<DeviceDataHandler> handlers;
	private OSCPortIn receiver;

	public DeviceDataListener() throws IOException {

		this.handlers = new HashSet<DeviceDataHandler>();
		
		try {
			receiver = new OSCPortIn(Integer.parseInt(Settings
					.getValue("oscportin")));

			// Register Listener for Velocity Data
			receiver.addListener("/devicedata", new OSCListener() {

				@Override
				public void acceptMessage(Date time, OSCMessage message) {

					Object[] parameter = message.getArguments();

					DeviceData data = new DeviceData();

					data.setReceiveDate(new Date());
					if (parameter.length == 4
							&& parameter[0].getClass() == Float.class
							&& parameter[1].getClass() == Float.class
							&& parameter[2].getClass() == Float.class
							&& parameter[3].getClass() == String.class) {
						data.setVelocity(new Velocity((Float) parameter[0],
								(Float) parameter[1], (Float) parameter[2]));
						data.setMacAddress(parameter[3].toString());
						
							
							for(DeviceDataHandler handler:  DeviceDataListener.this.handlers){
								handler.queue(data);
							}
					}
				}
			});

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}


	public void registerHandler(DeviceDataHandler handler) throws IOException {
		this.handlers.add(handler);
		new Thread(handler).start();
	}

	public void unregisterHandler(DeviceDataHandler handler) throws IOException {
		handler.close();
		this.handlers.remove(handler);
		
	}

	public void close() throws IOException {
		
		for(DeviceDataHandler handler : this.handlers){
			handler.close();
		}
		this.handlers = null;
		this.receiver.close();

	}

	public void stopListening() throws IOException {
		for(DeviceDataHandler handler : this.handlers){
			handler.stop();
		}
		this.receiver.stopListening();
	}

	public void startListening()throws IOException {	
		for(DeviceDataHandler handler : this.handlers){
			handler.start();
		}
		this.receiver.startListening();
	}
}
