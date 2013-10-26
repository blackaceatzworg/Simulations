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

public class VelocitySensorListener implements SensorEventListener {

	private float[] gravity = new float[4];
	private float[] linearAcceleration = new float[4];
	private float[] magneticField = new float[4];
	private float[] accInWorldCoordinates = new float[4];

	private float[] rotationMatrix = new float[16];
	private float[] inclinationMatrix = new float[16];
	private float[] inverseRotationMatrix = new float[16];;

	private static final float SMOOTHING_GRAVITY = 0.05f;
	private static final float SMOOTHING_LINACCELEARTION = 0.05f;
	private static final float SMOOTHING_MAGNETICFIELD = 0.05f;

	private SensorManager sensorManager;
	private Sensor linAccSensor;
	private Sensor gravitySensor;
	private Sensor magneticFieldSensor;
	
	private ConcurrentLinkedQueue<Velocity> messageQueue;

	public VelocitySensorListener(SensorManager sensorManager, ConcurrentLinkedQueue<Velocity> messageQueue) {
		this.sensorManager = sensorManager;
		this.linAccSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		this.gravitySensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_GRAVITY);
		this.magneticFieldSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		this.messageQueue = messageQueue;
	}

	public void start() {
		sensorManager.registerListener(this, linAccSensor,
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this, gravitySensor,
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this, magneticFieldSensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void stop() {
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

		this.messageQueue.add(acc);
	}
}
