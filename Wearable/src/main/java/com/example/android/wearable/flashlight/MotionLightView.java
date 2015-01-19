
package com.example.android.wearable.flashlight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Flashing party lights!
 */
public class MotionLightView extends View {

    private int mCurrentColor;

    public MotionLightView(Context context) {
        super(context);
    }

    public MotionLightView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mCurrentColor);
        super.onDraw(canvas);
    }

    public void updateColor(int x, int y, int z){
        mCurrentColor = getColor(x, y, z);
        postInvalidate();
    }

    // Translate xyz to RGB
    private int getColor(int x, int y, int z) {
        return Color.argb(50, x, y, z);
    }
}
