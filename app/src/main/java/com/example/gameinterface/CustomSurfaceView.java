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
        drawFighterCard(canvas, 250, 1000, "Skeleton", 7, 3);
    }

    //draws a fighter card on the screen
    //x and y are the top left corner of the card
    protected void drawFighterCard(Canvas canvas, float x, float y, String fighterName,
                                   int power, int prizeGold){
        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, fighterName);

        //draws the circle for the fighter's power
        Paint red = new Paint();
        red.setColor(Color.RED);
        canvas.drawCircle(x + 50.0f, y + 250.0f, 30.0f, red);

        //draws the circle for the fighter's prize gold
        Paint gold = new Paint();
        gold.setColor(Color.YELLOW);
        canvas.drawCircle(x + 175.0f, y + 250.0f, 30.0f, gold);

        //draws the text for the fighter's stats
        Paint statText = new Paint();
        statText.setColor(Color.BLACK);
        statText.setTextSize(50);
        statText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(Integer.toString(power), x + 50.0f, y + 270.0f, statText);
        canvas.drawText(Integer.toString(prizeGold), x + 175.0f, y + 270.0f, statText);

    }

    //draws a spell card on the screen

    //draws a face down card on the screen

    //draws the black border for the card outline
    protected void drawCardOutline(Canvas canvas, float x, float y){
        Paint cardOutline = new Paint();
        cardOutline.setColor(Color.BLACK);
        cardOutline.setStyle(Paint.Style.STROKE);
        cardOutline.setStrokeWidth(10.0f);
        canvas.drawRect(x, y, x + 225.0f, y + 300.0f, cardOutline);
    }

    //draws the card name at the top of the card
    protected void drawCardTitle(Canvas canvas, float x, float y, String text){
        Paint titleText = new Paint();
        titleText.setColor(Color.BLACK);
        titleText.setTextSize(50);
        titleText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, x + 112.5f, y + 60.0f, titleText);
    }
}
