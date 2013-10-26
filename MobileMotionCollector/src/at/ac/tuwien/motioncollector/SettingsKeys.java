package at.ac.tuwien.motioncollector;
public enum SettingsKeys {
	ApplicationName("applicationname"),
	Version("version")
	,OSCPortIn("oscportin")
	,OSCBroadcastPort("oscportbroadcast")
	,OSCBroadcastKey("osckeybroadcast");
	
	private final String key;
	
	SettingsKeys(String key){
		this.key = key;
	}
	
	
	public String key(){
		return this.key;
	}
}
