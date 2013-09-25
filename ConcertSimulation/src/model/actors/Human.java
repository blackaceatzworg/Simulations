package model.actors;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.bulletphysics.dynamics.RigidBody;

import model.environment.Area;
import movement.MovementSpeed;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import util.Units;

public class Human {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	MovementSpeed walkingSpeed;
	NdPoint destination;
	
	RigidBody rigidBody;
	
	
	
	public Human(ContinuousSpace<Object> space, Grid<Object> grid) {
		this.space = space;
		this.grid = grid;
		this.walkingSpeed = new MovementSpeed(3, 5, 4);
		this.lastPerformCall = new Date();
		
	}
	
	
	private Date now;
	private Date lastPerformCall;
	
	private long getElapsedTime(){
		return now.getTime()-lastPerformCall.getTime();
	}
	
	
	public void setRigidBody(RigidBody rigidBody) {
		this.rigidBody = rigidBody;
	}
	public RigidBody getRigidBody() {
		return rigidBody;
	}
	
	
	@ScheduledMethod(start=1,interval=1)
	public void perform(){
		this.now = new Date();
		
		if(this.destination == null){
			boolean isAccessible = true;
			
			
			
			//Looking for an unaccessible area in the point going, if this is the case another point is taken.
			do{
				this.destination = new NdPoint(RandomHelper.nextDoubleFromTo(0, this.space.getDimensions().getWidth()),RandomHelper.nextDoubleFromTo(0, this.space.getDimensions().getHeight()));
				for(Object o:this.grid.getObjectsAt((int)this.destination.getX(),(int)this.destination.getY())){
					if(o instanceof Area && !((Area)o).isAccessible()){
						isAccessible = true;
						break;
					}
				}
				
			}while(!isAccessible);
			
		}
		this.moveTowards(this.destination);
		
		if(this.destination.getX()==this.space.getLocation(this).getX()&&this.destination.getY()==this.space.getLocation(this).getY()){
			this.destination = null;
		}
		
		
		
		lastPerformCall = now;
	}
	
	public void moveTowards(NdPoint pt){
		if(!pt.equals(this.grid.getLocation(this))){
			NdPoint myPoint = this.space.getLocation(this);
			NdPoint otherPoint = new NdPoint(pt.getX(),pt.getY());
			
			double angle = SpatialMath.calcAngleFor2DMovement(this.space, myPoint, otherPoint);
			
			double distance =walkingSpeed.getDistance(getElapsedTime(), Units.Time.Milliseconds, Units.Distance.Meter);
			
			double distanceToPoint = Math.sqrt(Math.pow(otherPoint.getX()-myPoint.getX(), 2)+Math.pow(otherPoint.getY()-myPoint.getY(),2));
			
			if(distance > distanceToPoint){
				this.space.moveTo(this, otherPoint.getX(),otherPoint.getY());
			}else{
				this.space.moveByVector(this, distance*0.1 , angle,0);//Because we need to scale the area down by factor 10
			}
			
			
			
			myPoint = this.space.getLocation(this);
			this.grid.moveTo(this, (int)myPoint.getX(),(int)myPoint.getY());
		}
	}
	
	public void getPosition(){
		this.space.getLocation(this);
	}
	
}
