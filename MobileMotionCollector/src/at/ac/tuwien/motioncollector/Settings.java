package at.ac.tuwien.motioncollector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
	private static Properties settings;
	
	public static String getValue(String key){
		if(settings == null){
			loadSettings();
		}
		
		if(settings != null){
			return settings.getProperty(key);
		}
		else{
			return null;
		}
		
		
		
	}
	private static void loadSettings() {
		Properties settings = new Properties();
		try {
			settings.loadFromXML(new FileInputStream(new File("settings/general.xml")));
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			Settings.settings = settings;
		}
	}
	
}
