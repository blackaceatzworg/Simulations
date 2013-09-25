package style;

import java.awt.Color;

import model.actors.Human;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class HumanStyle extends DefaultStyleOGL2D {
	@Override
	public void init(ShapeFactory2D factory) {
		this.shapeFactory = factory;
	}
	
	@Override
	public Color getColor(Object agent) {
		Human human = (Human) agent;
		return Color.GREEN;
	}
	
	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		Human human = (Human) agent;
		if(spatial==null){
			spatial = shapeFactory.createCircle(20, 10);
		}
		return spatial;
	}
}
