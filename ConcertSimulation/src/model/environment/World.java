package model.environment;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import logging.Logging;
import model.actors.Human;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;

import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import config.Configuration;
import config.HumanConfigurationInfo;
import config.WorldConfigurationInfo;

public class World {
	
	
	/*Repast Simphony Components*/
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;
	
	
	/*JBullet Components*/
	
	private DiscreteDynamicsWorld dynamicsWorld;
	@SuppressWarnings("unused")
	private RigidBody ground;
	
	
	private int fps;
	
	private Date cycleStart;
	private Date lastWorldSimulation;
	
	private Date startOfFPSCount;
	private int framesCount;
	
	
	
	public World(WorldConfigurationInfo worldCI,Context<Object> context) {
		this.fps = worldCI.getFps();
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		this.space = spaceFactory.createContinuousSpace("location_space", context, new SimpleCartesianAdder<Object>(), new repast.simphony.space.continuous.StrictBorders(), worldCI.getWidth(),worldCI.getHeight());
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		this.grid = gridFactory.createGrid("location_grid", context,
				new GridBuilderParameters <Object >(new repast.simphony.space.grid.StickyBorders(), new SimpleGridAdder <Object >(),
				true, (int)(worldCI.getWidth()*worldCI.getCellUnits()), (int)(worldCI.getHeight()*worldCI.getCellUnits())));
		
		this.buildEnvironment(context);
		
		
		
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		Dispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
		SequentialImpulseConstraintSolver constraintSolver = new SequentialImpulseConstraintSolver();
		
		DbvtBroadphase broadphase = new DbvtBroadphase();
		
		this.dynamicsWorld = new DiscreteDynamicsWorld(dispatcher,broadphase,constraintSolver,collisionConfiguration);
		
		this.dynamicsWorld.setGravity(new  Vector3f(0,Configuration.Physics.getGravity(), 0));
		
		CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0,0,0),1);
		
		DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1),new Vector3f(0,0,0),1.0f)));
		RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape,new Vector3f(0,0,0));
		this.ground = new RigidBody(groundRigidBodyCI);
		
		
	}
	
	
	public int getFps() {
		return fps;
	}
	
	@ScheduledMethod(start=1,interval=10)
	public void startSimulationCycle(){
		if(startOfFPSCount == null){
			startOfFPSCount = new Date();
			framesCount = 0;
		}else if((new Date()).getTime()-startOfFPSCount.getTime()>1000){
			//Logging.getLogger().info("FPS - "+framesCount);
			startOfFPSCount = null;
		}
		framesCount++;
		
		
		this.cycleStart = new Date();
		float simulationTime = 0;
		if(this.lastWorldSimulation ==null){
			simulationTime = 1/this.fps;
		}else{
			simulationTime = ((new Date()).getTime()- this.lastWorldSimulation.getTime())/1000;
		}
		this.dynamicsWorld.stepSimulation(simulationTime, 7);
		
	}
	
	@ScheduledMethod(start=10,interval = 10)
	public void endSimulationCycle(){
		Date now = new Date();
		long millisecondsToWait = (1000/ this.fps)-(now.getTime()-this.cycleStart.getTime());
		if(millisecondsToWait>0){
			try {
				Thread.sleep(millisecondsToWait);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public Human createHuman(HumanConfigurationInfo humanCI,Context<Object> context){
		Human human = new Human(this.space, this.grid,this);
		
		
		
		
		/*DefaultMotionState motionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1),new Vector3f(0,0,0),1.0f)));
		CollisionShape shape = new CylinderShape(new Vector3f(0.4f, humanCI.getSize()/100, 1));
		Vector3f inertia = new Vector3f(0,0,0);
		shape.calculateLocalInertia(humanCI.getWeight(), inertia);
		RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(humanCI.getWeight(),motionState,shape,inertia);
		RigidBody rigidBody = new RigidBody(fallRigidBodyCI);
		this.dynamicsWorld.addRigidBody(rigidBody);
		
		human.setRigidBody(rigidBody);*/
		
		context.add(human);
		space.moveTo(human, humanCI.getPosition().getX(),humanCI.getPosition().getY());
		grid.moveTo(human, (int)humanCI.getPosition().getX(),(int)humanCI.getPosition().getY());
		
		Logging.getLogger().info("Human created: "+humanCI);
		
		
		return human;
		
	}
	
	private void buildEnvironment(Context<Object> context){
		/*for(int j=0;j<2;j++){
			for(int i=0;i <20;i++){
			Stage s = new Stage();
			context.add(s);
			grid.moveTo(s, 15+i,j);
			GridPoint pt = grid.getLocation(s);
			space.moveTo(s, pt.getX(),pt.getY());
			
		}
		}*/
	}
	
	
	
}
