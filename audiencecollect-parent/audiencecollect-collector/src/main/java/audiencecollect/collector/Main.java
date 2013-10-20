package audiencecollect.collector;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		
		
		Thread receiver = new Thread(new CollectorEndpoint());
		receiver.start();
	}

}
