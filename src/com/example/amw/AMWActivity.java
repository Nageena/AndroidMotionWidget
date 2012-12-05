package com.example.amw;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AMWActivity extends Activity  implements SensorEventListener {
	private SensorManager sensorManager;
	private boolean color = false;
	private boolean initialize; // New
	private Sensor accel; // 
	private final float NOISE = (float) 2.0;
	private View view;
	private long lastChanged;
	private float lastX,lastY,lastZ;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amw);
		view = findViewById(R.id.textView);
		view.setBackgroundColor(Color.GREEN);
		
		initialize = false; // new
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel , SensorManager.SENSOR_DELAY_NORMAL);
		
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		lastChanged = System.currentTimeMillis();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void onResume(){
		super.onResume();
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause(){
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
public void onSensorChanged(SensorEvent event){
		if (event.sensor.getType()== Sensor.TYPE_ACCELEROMETER){
		
			
			//
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			TextView X= (TextView)findViewById(R.id.x_axis);
			TextView Y= (TextView)findViewById(R.id.y_axis);
			TextView Z= (TextView)findViewById(R.id.z_axis);
		//	ImageView iv = (ImageView)findViewById(R.id.image);
					
			if (!initialize) {
				lastX = x;
				lastY = y;
				lastZ = z;
				X.setText("0.0");
				Y.setText("0.0");
				Z.setText("0.0");
			initialize = true;
			} else {
				float dX = Math.abs(lastX - x);
				float dY = Math.abs(lastY - y);
				float dZ = Math.abs(lastZ - z);
				//if (dX < NOISE) dX = (float)0.0;
				//if (dY < NOISE) dY = (float)0.0;
				//if (dZ < NOISE) dZ = (float)0.0;
				lastX = x;
				lastY = y;
				lastZ = z;
				X.setText(Float.toString(dX));
				Y.setText(Float.toString(dY));
				Z.setText(Float.toString(dZ));
				
				showShakeColors(dX,dY,dZ);
				
			/*	if (dX > dY) {
					iv.setImageResource(R.drawable.Red);
				} else if (dY > dX) {
					iv.setImageResource(R.drawable.Green);
				} else {
					iv.setVisibility(View.INVISIBLE);
				}*/
			}
		}
	}
	private void showShakeColors(float dX, float dY, float dZ) {
		// TODO Auto-generated method stub
		
		long actualTime = System.currentTimeMillis();
		if (actualTime - lastChanged <200){
			return;
		}
		lastChanged = actualTime;
		
		boolean isShaken = false;
		
		if(Math.abs(dX) > Math.abs(dY) && Math.abs(dX) > Math.abs(dZ) && Math.abs(dX) > NOISE){
			view.setBackgroundColor(Color.RED);
		}else if (Math.abs(dY)>Math.abs(dZ) && Math.abs(dY)>Math.abs(dX)&& Math.abs(dY)>NOISE){
			view.setBackgroundColor(Color.GREEN);
		}else if (Math.abs(dZ)>Math.abs(dX)&& Math.abs(dZ)>Math.abs(dY)&& Math.abs(dZ)>NOISE){
			view.setBackgroundColor(Color.BLUE);
		}else{
			view.setBackgroundColor(Color.GRAY);			
		}
	//	color = !color;
		
		if (isShaken) {
			Toast.makeText(this, "device is shaked", Toast.LENGTH_SHORT).show();
		}
		
	}
	
}
