package logging;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public abstract class Logging {
	
	
	private static Logger logger;
	
	public static Logger getLogger(){
		if(logger == null){
			logger = createLogger();
		}
		return logger;
	}
	
	private static Logger createLogger(){
		Logger logger = Logger.getRootLogger();
		try {
			//SimpleLayout layout = new SimpleLayout();
			PatternLayout layout = new PatternLayout( "%d{ISO8601} %-5p: %m%n" );
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			logger.addAppender(consoleAppender);
			
			FileAppender fileAppender = new FileAppender(layout, "logs/SimulationLog.log");
			logger.addAppender(fileAppender);
			
			logger.setLevel(Level.ALL);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return logger;
	}
	
	
}
