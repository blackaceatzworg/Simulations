package com.pfeiffer.motioncapture;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortOut;
import com.pfeiffer.motioncapture.util.Filter;
import com.pfeiffer.motioncapture.util.MathUtil;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	
	private SensorManager mSensorManager;
	private Sensor mLinAccSensor;
	private Sensor mGravitySensor;
	private Sensor mMagnecticFieldSensor;
	private boolean readSensorData = false;
	
	
	private float[] gravity;
	private float[] linAccelearation;
	private float[] magnecticField;
	
	
	private float[] rotationMatrix;
	private float[] inclinationMatrix;
	
	private float[] inverseRotationMatrix;
	
	private EditText editTextIPAddr;
	private SendOSCMessageTask senderTask;
	
	private float smoothing = 0.1f;
	
	private String mac;
	
	private void actualizeSmoothing(){
		EditText smo = (EditText) this.findViewById(R.id.editTextSmoothing);
		this.smoothing = Float.parseFloat(smo.getText().toString());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		this.gravity = new float[4];
		this.linAccelearation = new float[4];
		this.magnecticField = new float[4];
		this.rotationMatrix = new float[16];
		this.inverseRotationMatrix = new float[16];
		
		this.mLinAccSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		this.mGravitySensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		this.mMagnecticFieldSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		this.editTextIPAddr = (EditText) this.findViewById(R.id.editTextIPAdress);
		this.editTextIPAddr.setText("169.254.148.112");
		
		EditText smo = (EditText) this.findViewById(R.id.editTextSmoothing);
		smo.setText("0.05");
		
		this.mac = this.getMACAdress();
		
			
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void beginReadingAccelerometer(View view){
		
		if(!this.readSensorData ){
			this.readSensorData = true;
			mSensorManager.registerListener(this, mLinAccSensor, SensorManager.SENSOR_DELAY_GAME);
			mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_GAME);
			mSensorManager.registerListener(this, mMagnecticFieldSensor, SensorManager.SENSOR_DELAY_GAME);
			actualizeSmoothing();
			
			
			
			try {
				this.senderTask = new SendOSCMessageTask(this.editTextIPAddr.getText().toString());
				this.senderTask.execute();
				Object[] o = new Object[1];
				o[0] = "start";
				this.senderTask.appendMessage(new OSCMessage("/devicedata",o));
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else{
			this.readSensorData = false;
			mSensorManager.unregisterListener(this);
			
			Object[] o = new Object[1];
			o[0] = "stop";
			this.senderTask.appendMessage(new OSCMessage("/devicedata",o));
			
			this.senderTask.stop(false);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
			this.linAccelearation = Filter.lowPass(event.values.clone(), this.linAccelearation, this.smoothing);
//			System.arraycopy(event.values, 0, this.linAccelearation, 0,3);
		
			SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, this.gravity, this.magnecticField);
			
			
			Matrix.invertM(this.inverseRotationMatrix, 0, this.rotationMatrix, 0);
			
			float[] accInWorldCoordinates = new float[4];
			
			Matrix.multiplyMV(accInWorldCoordinates, 0, this.inverseRotationMatrix, 0, this.linAccelearation, 0);
			
			
			TextView x,y,z;
			x = (TextView) findViewById(R.id.textViewAccX);
			y = (TextView) findViewById(R.id.textViewAccY);
			z = (TextView) findViewById(R.id.textViewAccZ);
			
			x.setText(String.valueOf(MathUtil.round(accInWorldCoordinates[0],1)));
			y.setText(String.valueOf(MathUtil.round(accInWorldCoordinates[1],1)));
			z.setText(String.valueOf(MathUtil.round(accInWorldCoordinates[2],1)));
			
			
			Object[] args = new Object[4];
			args[0]=MathUtil.round(accInWorldCoordinates[0],2);
			args[1]=MathUtil.round(accInWorldCoordinates[1],2);
			args[2]=MathUtil.round(accInWorldCoordinates[2],2);
			args[3]=this.mac;
			OSCMessage message = new OSCMessage("/devicedata", args);
			
			this.senderTask.appendMessage(message);
		
		}else if(event.sensor.getType()== Sensor.TYPE_GRAVITY){
			this.gravity = Filter.lowPass(event.values.clone(), this.gravity, this.smoothing);
//			System.arraycopy(event.values, 0, this.gravity, 0,3);
		}else if(event.sensor.getType()== Sensor.TYPE_MAGNETIC_FIELD){
			this.magnecticField = Filter.lowPass(event.values.clone(), this.magnecticField, this.smoothing);
//			System.arraycopy(event.values, 0, this.magnecticField, 0,3);
		}
		
		
		
		
		
		// TODO Auto-generated method stub
		
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensorManager.unregisterListener(this);
	}
	
	
	
	private class SendOSCMessageTask extends AsyncTask<Void, Void, Void>{

		
		
	}
	
	
	
	

}
