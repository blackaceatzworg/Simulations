package at.ac.tuwien.motioncapture.listeners;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import at.ac.tuwien.motioncapture.model.Velocity;
import at.ac.tuwien.motioncapture.util.Filter;
import at.ac.tuwien.motioncapture.util.MathUtil;

public class VelocitySensorListener implements Runnable, SensorEventListener {

	private float[] gravity = new float[4];
	private float[] linearAcceleration = new float[4];
	private float[] magneticField = new float[4];
	private float[] accInWorldCoordinates = new float[4];

	private float[] rotationMatrix = new float[16];
	private float[] inclinationMatrix = new float[16];
	private float[] inverseRotationMatrix = new float[16];;

	private static final float SMOOTHING_GRAVITY = 0.5f;
	private static final float SMOOTHING_LINACCELEARTION = 0.5f;
	private static final float SMOOTHING_MAGNETICFIELD = 0.5f;

	private SensorManager sensorManager;
	private Sensor linAccSensor;
	private Sensor gravitySensor;
	private Sensor magneticFieldSensor;
	
	private ConcurrentLinkedQueue<Velocity> bufferQueue;
	private ConcurrentLinkedQueue<Velocity> messageQueue;

	private boolean running;
	
	public VelocitySensorListener(SensorManager sensorManager, ConcurrentLinkedQueue<Velocity> messageQueue) {
		this.sensorManager = sensorManager;
		this.linAccSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		this.gravitySensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_GRAVITY);
		this.magneticFieldSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		this.messageQueue = messageQueue;
		this.bufferQueue = new ConcurrentLinkedQueue<Velocity>();
		
		this.running =false;
	}

	public void start() {
		Thread t =  new Thread(this);
		t.start();
		t.setPriority(Thread.MAX_PRIORITY);
		this.running = true;
		
		
		sensorManager.registerListener(this, linAccSensor,
				SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, gravitySensor,
				SensorManager.SENSOR_DELAY_UI);
		sensorManager.registerListener(this, magneticFieldSensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	public void stop() {
		this.running = false;
		sensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Adjust smoothing

	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			this.linearAcceleration = Filter.lowPass(event.values.clone(),
					this.linearAcceleration, SMOOTHING_LINACCELEARTION);
			processOrientedLinearAcceleration();
		} else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
			this.gravity = Filter.lowPass(event.values.clone(), this.gravity,
					SMOOTHING_GRAVITY);
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			this.magneticField = Filter.lowPass(event.values.clone(),
					this.magneticField, SMOOTHING_MAGNETICFIELD);
		}
	}

	private void processOrientedLinearAcceleration() {
		SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix,
				this.gravity, this.magneticField);
		Matrix.invertM(this.inverseRotationMatrix, 0, this.rotationMatrix, 0);
		Matrix.multiplyMV(accInWorldCoordinates, 0, this.inverseRotationMatrix,
				0, this.linearAcceleration, 0);

		Velocity acc = new Velocity();
		
		acc.setX(MathUtil.round(accInWorldCoordinates[0], 2));
		acc.setY(MathUtil.round(accInWorldCoordinates[1], 2));
		acc.setZ(MathUtil.round(accInWorldCoordinates[2], 2));

		this.bufferQueue.add(acc);
		//this.messageQueue.add(acc);
	}

	public void run() {
		while(this.running){
			while(this.bufferQueue.isEmpty()){
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.messageQueue.add(this.bufferQueue.poll());
		}
		
	}
}
