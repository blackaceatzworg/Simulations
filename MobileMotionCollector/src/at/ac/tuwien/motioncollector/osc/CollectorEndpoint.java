package at.ac.tuwien.motioncollector.osc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import at.ac.tuwien.motioncollector.model.Position;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCMessage;

public class CollectorEndpoint implements Runnable {

	
	List<DeviceDataHandler> dataHandlers;
	
	BufferedWriter fileWriter;

	protected void createNewFile() throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("hh_mm_ss");
//		File csv = new File("/Users/daniel/temp/movement_"
//				+ sdf.format(new Date()) + ".csv");
		File csv = new File("/Users/daniel/temp/movement.csv");
		
		if(csv.exists()){
			csv.delete();
		}
		
		this.fileWriter = new BufferedWriter(new FileWriter(csv));
	}

	public void run() {
		
		try {

			// File db = new File("/Users/daniel/temp/db/");
			// for(File f:db.listFiles()){
			// f.delete();
			// }

			// final StorageService service = FileDBStorageService
			// .createInstance("/Users/daniel/temp/db/audience", "audience",
			// "audience");
			// service.createDatabase();

			
			OSCPortIn receiver = new OSCPortIn(8000);
			OSCListener listener = new OSCListener() {
				private UUID userid = UUID.randomUUID();
				private boolean isListening = false;

				public void acceptMessage(java.util.Date time,
						OSCMessage message) {
					Date t = new Date();

					Object[] parameter = message.getArguments();

					if (parameter.length == 1 && parameter[0] instanceof String) {
						try {
							if (((String) parameter[0]).equals("start")) {
								CollectorEndpoint.this.createNewFile();
								CollectorEndpoint.this.fileWriter
										.write("Date;x;y;z\n");
								this.isListening = true;
							} else if (((String) parameter[0]).equals("stop")) {
								if(CollectorEndpoint.this.fileWriter != null){
									CollectorEndpoint.this.fileWriter.close();
								}
								
								this.isListening = false;
							}
							return;

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					if(!isListening){
						return;
					}
					
					Position p = new Position((Float) parameter[0],
							(Float) parameter[1], (Float) parameter[2]);

					String macAddr = "no mac provided";
					if (parameter.length == 4) {
						macAddr = (String) parameter[3];
					}

					// try {
					// service.SavePositionData(this.userid, t, p);
					//
					//
					// } catch (SQLException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }

					SimpleDateFormat sdf2 = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss.SSS");

					try {
						CollectorEndpoint.this.fileWriter.write(sdf2
								.format(new Date())
								+ ";"
								+ String.valueOf(p.getX())
								+ ";"
								+ String.valueOf(p.getY())
								+ ";"
								+ String.valueOf(p.getZ()) + "\n");
						CollectorEndpoint.this.fileWriter.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss:SSS");

					System.out
							.println(String
									.format("%s - x: %f - y: %f - z: %f - time: %s - address: %s",
											message.getAddress(), p.getX(),
											p.getY(), p.getZ(),
											sdf.format(new Date()), macAddr));

				}
			};
			receiver.addListener("/accxyz", listener);
			receiver.startListening();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	

}
