package physics;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;









import model.actors.Human;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

import config.WorldConfigurationInfo;

public class PhysicalContext {
	
	private CollisionConfiguration collisionConfiguration;
	private Dispatcher dispatcher;
	private SequentialImpulseConstraintSolver seqImpulseConstraintSolver;
	private DbvtBroadphase broadphase;
	private DiscreteDynamicsWorld world;
	
	public PhysicalContext(WorldConfigurationInfo worldConfig) {
		this.collisionConfiguration = new DefaultCollisionConfiguration();
		this.dispatcher = new CollisionDispatcher(collisionConfiguration);
		this.seqImpulseConstraintSolver = new SequentialImpulseConstraintSolver();
		
		this.broadphase = new DbvtBroadphase();
		this.world = new DiscreteDynamicsWorld(dispatcher, broadphase, seqImpulseConstraintSolver, collisionConfiguration);
		
		this.world.setGravity(new Vector3f(0, -9.8f,0));
		
		
		CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0,1,0), 1);
		
		CollisionShape fallShape = new SphereShape(1);



		
		DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1),new Vector3f(0,-1,0),1.0f)));
		
		RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape,new Vector3f(0,0,0));
		
		RigidBody groundRigidBody = new RigidBody(groundRigidBodyCI);
		
		this.world.addRigidBody(groundRigidBody);
		
		
		DefaultMotionState fallMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0,0,0,1),new Vector3f(0,50,0),1.0f)));
		
		float mass = 1;
		
		Vector3f fallInertia = new Vector3f(0,0,0);
		fallShape.calculateLocalInertia(mass, fallInertia);
		
		RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(mass, fallMotionState, fallShape);
		RigidBody fallRigidBody = new RigidBody(fallRigidBodyCI);
		
		
		
	}
	
	public DiscreteDynamicsWorld getWorld(){
		return this.world;
	}
	
	public void addHuman(Human human){
		
	}
	
}
