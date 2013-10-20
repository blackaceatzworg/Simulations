package at.ac.tuwien.motioncollector.storage;

import java.sql.SQLException;

public class FileDBStorageService extends StorageService {
	private FileDBStorageService(String db, String user, String password) throws ClassNotFoundException, SQLException{
		
		super(db,user,password,"org.h2.Driver","jdbc:h2:");
	}
	
	public static StorageService createInstance(String db, String user, String password) throws ClassNotFoundException, SQLException {
		return new FileDBStorageService(db, user, password);
	}
}
