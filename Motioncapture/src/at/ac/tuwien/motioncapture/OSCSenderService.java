package at.ac.tuwien.motioncapture;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import at.ac.tuwien.motioncapture.listeners.HostAddressBroadcastListener;
import at.ac.tuwien.motioncapture.listeners.VelocitySensorListener;
import at.ac.tuwien.motioncapture.model.Velocity;
import at.ac.tuwien.motioncapture.tasks.DeviceDataSender;

public class OSCSenderService extends Service  {
	static final String LOGGING_TAG = "OSCSenderService";

	private VelocitySensorListener velocitySensorListener;
	private HostAddressBroadcastListener broadcastListener;
	private DeviceDataSender<Velocity> sender;
	private ConcurrentLinkedQueue<Velocity> messageQueue;

	private static boolean running = false;

	private String macAddress;
	private InetAddress hostAddress;

	
	
	public static boolean isRunning() {
		return running;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {

		running = true;
		try {
			this.broadcastListener = new HostAddressBroadcastListener(
					getResources().getInteger(R.integer.portBroadcast),
					getResources().getString(R.string.oscBroadcastKey));

		} catch (SocketException ex) {
			Log.e(LOGGING_TAG,
					"Broadcastlistener could not be started. Service stopped.",
					ex);
			this.stopSelf();
		}
		setStillAliveNotification();
		

		this.messageQueue = new ConcurrentLinkedQueue<Velocity>();
		this.macAddress = this.getMacAddress();

	}

	private String getMacAddress() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

		WifiInfo info = wifiManager.getConnectionInfo();
		String address = info.getMacAddress();

		return address;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Thread thread = new Thread(new Runnable() {

			public void run() {
				while (running && (hostAddress= broadcastListener.getAddress() ) == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Log.i(LOGGING_TAG, "Waiting thread interrupted");
					}
				}
				if(running){
					start();
				}
				
			}
		});
		thread.start();

		return START_NOT_STICKY;
	}

	private void start() {
		try {

			this.broadcastListener.close();
			
			this.sender = new DeviceDataSender<Velocity>(messageQueue,
					hostAddress, getResources().getInteger(R.integer.port),
					macAddress, getResources().getString(R.string.oscSendKey));

			this.sender.start();

			this.velocitySensorListener = new VelocitySensorListener(
					(SensorManager) getSystemService(SENSOR_SERVICE),
					this.messageQueue);

			this.velocitySensorListener.start();

		} catch (SocketException e) {
			Log.e(LOGGING_TAG, "Ouput Port couldnt be obtained.", e);
		}
	}

	@Override
	public void onDestroy() {
		running = false;
		removeStillAliveNotification();
		
		if(this.velocitySensorListener!= null){
			this.velocitySensorListener.stop();
		}
		
		if(this.sender!=null){
			this.sender.stop();
		}

		if(this.broadcastListener!=null && this.broadcastListener.isRunning()){
			this.broadcastListener.close();
		}
		
		Log.i(LOGGING_TAG, "Service destroyed");

		
	}

	private void removeStillAliveNotification() {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(1);
	}

	private void setStillAliveNotification() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Capture running!")
				.setContentText("Turn it off to save Battery.");

		Intent resultIntent = new Intent(this, Main.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Main.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setProgress(1, 0, true);
		mBuilder.setOngoing(true);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());
	}
	

}
