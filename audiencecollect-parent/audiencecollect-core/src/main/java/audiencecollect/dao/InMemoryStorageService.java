package audiencecollect.dao;

import java.sql.SQLException;

public class InMemoryStorageService extends StorageService {
	private InMemoryStorageService(String db, String user, String password) throws ClassNotFoundException, SQLException{
		super(db,user,password,"org.h2.Driver","jdbc:h2:mem:");
	}
	
	public static StorageService createInstance(String db, String user, String password) throws ClassNotFoundException, SQLException {
		return new InMemoryStorageService(db, user, password);
	}
	
}
