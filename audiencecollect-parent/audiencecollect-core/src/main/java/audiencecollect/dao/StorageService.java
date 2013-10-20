package audiencecollect.dao;

import java.io.File;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import audiencecollect.model.Position;


public abstract class StorageService {

	protected Connection connection;
	
	protected StorageService(String db, String user, String password,String driver, String protocol) throws ClassNotFoundException, SQLException{
		Class.forName(driver);
		
		Properties properties = new Properties();
		properties.put("user", user);
		properties.put("password", password);
		
		this.connection = DriverManager.getConnection(protocol+db, properties);
	}
	
	public void createDatabase() throws SQLException, FileNotFoundException{

		for(String s:SQLScriptHelper.getStatements(new File("../audiencecollect-core/src/main/resources/dbcreate.sql"))){
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate(s);
			stmt.close();
		}
		connection.commit();
		
		
	}
	
	public void SavePositionData(UUID userId,Date time, Position position) throws SQLException{
		
		PreparedStatement stmt = this.connection.prepareStatement("insert into " +
				"accelerometerUserData(captureTime,userId,xVal,yVal,zVal) " +
				"values(?,?,?,?,?)");
		
		stmt.setTimestamp(1, new Timestamp(time.getTime()));
		stmt.setString(2, userId.toString());
		stmt.setDouble(3, position.getX());
		stmt.setDouble(4, position.getY());
		stmt.setDouble(5, position.getZ());
		
		stmt.executeUpdate();
		stmt.close();
		this.connection.commit();
		
	}
	
}
