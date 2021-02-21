package com.example.gameinterface;

import android.graphics.PixelFormat;
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

        //makes the surface view background to transparent
        //code is adapted from https://stackoverflow.com/a/27959612
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    protected void onDraw(Canvas canvas){
        drawFighterCard(canvas, 25, 25, "Skeleton");
    }

    protected void drawFighterCard(Canvas canvas, float x, float y, String fighterName){
        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, fighterName);
    }

    //draws the black border for the card outline
    protected void drawCardOutline(Canvas canvas, float x, float y){
        Paint cardOutline = new Paint();
        cardOutline.setColor(Color.BLACK);
        cardOutline.setStyle(Paint.Style.STROKE);
        cardOutline.setStrokeWidth(10.0f);
        canvas.drawRect(x, y, x + 225.0f, y + 300.0f, cardOutline);
    }

    //draws the card name at the top of the card
    //x and y are the top left corner of the card
    protected void drawCardTitle(Canvas canvas, float x, float y, String text){
        Paint titleText = new Paint();
        titleText.setColor(Color.BLACK);
        titleText.setTextSize(40);
        titleText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, x + 112.5f, y + 50.0f, titleText);
    }

}
