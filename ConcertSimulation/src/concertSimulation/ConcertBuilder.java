package concertSimulation;



import model.actors.Sex;
import model.environment.World;
import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.NdPoint;
import config.Configuration;
import config.HumanConfigurationInfo;
import config.WorldConfigurationInfo;
import config.random.HumanRandomUtil;

public class ConcertBuilder implements ContextBuilder<Object> {

	
	@Override
	public Context<Object> build(Context<Object> context) {
		context.setId("ConcertSimulation");
	
		
		WorldConfigurationInfo wci = new WorldConfigurationInfo(Configuration.World.X_WIDTH, Configuration.World.Z_WIDTH, Configuration.World.getCellUnits(), Configuration.Physics.getGravity(), 30);
		
		
		World w = new World(wci, context);
		
		
		HumanConfigurationInfo hci = HumanRandomUtil.getRandomMaleConfiguration();
		hci.setPosition(new NdPoint(10,10));
		w.createHuman(hci,context);
		
		
		context.add(w);
		
		
		
		
		return context;
	}

	
}
