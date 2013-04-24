package com.mcliang.seismograph;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Main extends Activity implements SensorEventListener {
	
	private SensorManager sensorManager;
    private Sensor accelerometer;
    DrawView drawView;
    float lastX=0, lastY=0, lastZ=0;
    float curX=0, curY=0, curZ=0;
    float deltaX=0, deltaY=0, deltaZ=0;
    final float NOISE = (float) 0.04;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer , SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this
	}

	public void onSensorChanged(SensorEvent event) {
        curX = event.values[0];
        curY = event.values[1];
        curZ = event.values[2];
        deltaX = lastX - curX;
        deltaY = lastY - curY;
        deltaZ = lastZ - curZ;
        if (Math.abs(deltaX) < NOISE) deltaX = (float)0.0;
        if (Math.abs(deltaY) < NOISE) deltaY = (float)0.0;
        if (Math.abs(deltaZ) < NOISE) deltaZ = (float)0.0;
        lastX = curX;
        lastY = curY;
        lastZ = curZ;
	    DrawView drawView = (DrawView) findViewById(R.id.drawView);
	    drawView.updateView(deltaX, deltaY, deltaZ);
	}
}



