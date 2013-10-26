package at.ac.tuwien.motioncollector.osc;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.ac.tuwien.motioncollector.Settings;
import at.ac.tuwien.motioncollector.SettingsKeys;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class AddressBroadcastSender implements Runnable {

	private OSCPortOut sender;
	private boolean running;
	private NetworkInterface networkInterface;

	public static AddressBroadcastSender startInstance(NetworkInterface inter,
			InetAddress broadcastAddress, int portOut) throws SocketException {
		AddressBroadcastSender task = new AddressBroadcastSender();

		task.networkInterface = inter;
		task.sender = new OSCPortOut(broadcastAddress, portOut);
		task.running = true;

		new Thread(task).start();
		return task;
	}

	private AddressBroadcastSender() {

	}

	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		try {
			while (this.running) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

				try {
					if (this.networkInterface != null
							&& this.networkInterface.isUp()
							&& !this.networkInterface.isLoopback()) {
						List<Object> obj = new ArrayList<>(1);

						for (InetAddress address : Collections
								.list(this.networkInterface.getInetAddresses())) {
							
							if (!address.isLoopbackAddress()
									&& address instanceof Inet4Address) {
								
								obj.add(address.getHostAddress());
								
								OSCMessage message = new OSCMessage(
										Settings.getValue(SettingsKeys.OSCBroadcastKey
												.key()), obj);
								
								this.sender.send(message);

								break;
							}
						}


					}
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} finally {
			if (this.sender != null) {
				this.sender.close();
				this.sender = null;
			}
		}

	}

}
