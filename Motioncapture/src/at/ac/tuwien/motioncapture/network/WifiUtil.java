package at.ac.tuwien.motioncapture.network;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiUtil {
	
	private Context context;
	
	public WifiUtil(Context context) {
		this.context = context;
	}
	protected String getMACAdress(){
		WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);

		if(!wifiManager.isWifiEnabled()){
			wifiManager.setWifiEnabled(true);
		}
		
		WifiInfo info = wifiManager.getConnectionInfo();
	    String address = info.getMacAddress();
	    
	    return address;
		
	}
}
