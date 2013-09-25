package concertSimulation;


import model.actors.Human;
import model.environment.Stage;
import config.Configuration;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;

public class ConcertBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		context.setId("ConcertSimulation");
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("location_space", context, new RandomCartesianAdder<Object>(), new repast.simphony.space.continuous.StrictBorders(), 100,100);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("location_grid", context,
				new GridBuilderParameters <Object >(new repast.simphony.space.grid.StickyBorders(), new SimpleGridAdder <Object >(),
				true, 100, 100));
		
		for(int j=0;j<2;j++){
			for(int i=0;i <20;i++){
			Stage s = new Stage();
			context.add(s);
			grid.moveTo(s, 15+i,j);
			GridPoint pt = grid.getLocation(s);
			space.moveTo(s, pt.getX(),pt.getY());
			
		}
		}
		
		
		
		Human human = new Human(space, grid);
		context.add(human);
		NdPoint pt = space.getLocation(human);
		grid.moveTo(human, (int)pt.getX(),(int)pt.getY());
		
		
		
		return context;
	}

	
	private void buildPhysicalContext(){
		Configuration.Physics.getGravity();
		
		
	}
	
}
