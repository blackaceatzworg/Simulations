package model.environment;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import model.actors.Human;

import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import config.Configuration;
import config.HumanConfigurationInfo;
import config.WorldConfigurationInfo;

public class World {
	
	
	/*Repast Simphony Components*/
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;
	
	
	/*JBullet Components*/
	
	private DiscreteDynamicsWorld dynamicsWorld;
	private RigidBody ground;
	
	
	
	public World(WorldConfigurationInfo worldCI,Context<Object> context) {
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		this.space = spaceFactory.createContinuousSpace("location_space", context, new SimpleCartesianAdder<Object>(), new repast.simphony.space.continuous.StrictBorders(), worldCI.getWidth(),worldCI.getHeight());
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		this.grid = gridFactory.createGrid("location_grid", context,
				new GridBuilderParameters <Object >(new repast.simphony.space.grid.StickyBorders(), new SimpleGridAdder <Object >(),
				true, (int)Math.ceil((worldCI.getWidth()/worldCI.getCellUnits())), (int)Math.ceil((worldCI.getHeight()/worldCI.getCellUnits()))));
		
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
	
	public Human createHuman(HumanConfigurationInfo humanCI){
		Human human = new Human(this.space, this.grid);
		
		
		DefaultMotionState motionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1),new Vector3f(0,0,0),1.0f)));
		CollisionShape shape = new CylinderShape(new Vector3f(0.4f, humanCI.getSize()/100, 1));
		Vector3f inertia = new Vector3f(0,0,0);
		shape.calculateLocalInertia(humanCI.getWeight(), inertia);
		RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(humanCI.getWeight(),motionState,shape,inertia);
		RigidBody rigidBody = new RigidBody(fallRigidBodyCI);
		this.dynamicsWorld.addRigidBody(rigidBody);
		
		human.setRigidBody(rigidBody);
		
		
		
		return human;
		
	}
	
	
	
}
