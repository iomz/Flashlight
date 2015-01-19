
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
        /*
        mEvaluator = new ArgbEvaluator();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mCurrentColor = getColor(x, y, z);
                postInvalidate();
                mHandler.sendEmptyMessageDelayed(0, 100);
            }
        };
        */
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float GAIN = 0.9f;
            x = (int)(12.8*(x * GAIN + e.values[0] * (1 - GAIN))+128);
            y = (int)(12.8*(y * GAIN + e.values[1] * (1 - GAIN))+128);
            z = (int)(12.8*(z * GAIN + e.values[2] * (1 - GAIN))+128);
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

    // Translate xyz to RGB
    private int getColor(int x, int y, int z) {
        return Color.argb(50, x, y, z);
    }

    /*
    public void startCycling() {
        mHandler.sendEmptyMessage(0);
    }

    public void stopCycling() {
        mHandler.removeMessages(0);
    }
    */
}
