package com.example.gameinterface;

import android.view.SurfaceView;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

public class CustomSurfaceView extends SurfaceView {

    public CustomSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);
    }

    protected void onDraw(Canvas canvas){
        Paint red = new Paint();
        red.setColor(Color.RED);
        canvas.drawRect(0, 0, 100.0f, 100.0f, red);
    }

}
