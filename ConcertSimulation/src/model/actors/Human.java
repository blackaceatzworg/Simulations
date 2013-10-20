package model.actors;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

import config.Configuration;
import logging.Logging;
import model.environment.Area;
import model.environment.World;
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
	
	
	private World world;
	private RigidBody rigidBody;
	
	
	public Human(ContinuousSpace<Object> space, Grid<Object> grid,World world) {
		this.space = space;
		this.grid = grid;
		this.walkingSpeed = new MovementSpeed(3, 5, 4);
		this.world = world;
		
	}
	
	
	private Date now;
	private Date lastPerformCall;
	
	private long getElapsedTime(){
		return (now.getTime()-lastPerformCall.getTime())*(this.world.getFps()/Configuration.Simulation.standardFPS);
	}
	
	
	public void setRigidBody(RigidBody rigidBody) {
		this.rigidBody = rigidBody;
	}
	public RigidBody getRigidBody() {
		return rigidBody;
	}
	
	
	@ScheduledMethod(start=2,interval=10)
	public void monitorState(){
		Transform transform = new Transform();
		this.rigidBody.getMotionState().getWorldTransform(transform);
		
		Logging.getLogger().info(transform.origin);
		
	}
	
	@ScheduledMethod(start=2,interval=10)
	public void perform(){
		if(this.lastPerformCall ==null){
			this.lastPerformCall = new Date();
		}
		this.now = new Date();
		
		/*if(this.destination == null){
			
			this.destination = new NdPoint(RandomHelper.nextDoubleFromTo(0, this.space.getDimensions().getWidth()),RandomHelper.nextDoubleFromTo(0, this.space.getDimensions().getHeight()));

			
			//Logging.getLogger().info("New Destination: "+this.destination);
			
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
			jump();
			this.destination = null;
		}
		
		
		
		lastPerformCall = now;*/
	}
	
	
	private void jump() {
		this.rigidBody.activate();
		//this.rigidBody.applyCentralForce(new  Vector3f(0, arg1, arg2));
		
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
				this.space.moveByVector(this, distance , angle,0);
			}
			
			
			
			myPoint = this.space.getLocation(this);
			
			this.grid.moveTo(this, (int)(myPoint.getX()*Configuration.World.getCellUnits()),(int)(myPoint.getY()*Configuration.World.getCellUnits()));
		}
	}
	
	public void getPosition(){
		this.space.getLocation(this);
	}
	
}
