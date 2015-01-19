
package com.example.android.wearable.flashlight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Flashing motion lights!
 */
public class MotionLightView extends View implements SensorEventListener {

    private SensorManager mSensorManager;
    private int x, y, z;
    private int mCurrentColor;

    public MotionLightView(Context context) {
        super(context);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        init();
    }

    public MotionLightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        init();
    }

    private void init() {
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = (int)(12.8 * e.values[0] + 128);
            y = (int)(12.8 * e.values[1] + 128);
            z = (int)(12.8 * e.values[2] + 128);
        }
        mCurrentColor = getColor(x, y, z);
        postInvalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mCurrentColor);
        super.onDraw(canvas);
    }

    // Translate xyz to RGB at alpha 200
    private int getColor(int x, int y, int z) {
        return Color.argb(200, x, y, z);
    }

}
