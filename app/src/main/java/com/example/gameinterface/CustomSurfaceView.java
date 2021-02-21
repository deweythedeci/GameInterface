package com.example.gameinterface;

import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

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
        drawFighterCard(canvas, 100, 200, "Skeleton", 7, 3,
                false);
        drawSpellCard(canvas, 400, 200, "Might", 2, 1,+3,
                false, "", true);
    }

    //draws a fighter card on the screen
    //x and y are the top left corner of the card
    protected void drawFighterCard(Canvas canvas, float x, float y, String fighterName,
                                   int power, int prizeGold, boolean hasBet){
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

        //draws a yellow translucent border around the card if hasBet is true

    }

    //draws a spell card on the screen
    //if hasCardText is true the card is printed with textEffect at the bottom
    //else if hasCardText is false the card only has a power modifier
    //spell types: 1 = direct, 2 = enchant, 3 = support
    protected void drawSpellCard(Canvas canvas, float x, float y, String spellName, int mana,
                                 int spellType, int powerMod, boolean hasCardText,
                                 String effectText, boolean isForbidden){
        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, spellName);

        //draws a symbol according to the spell's type
        //colored circles are used as stand ins for now
        Paint spellTypeSymbolPaint = new Paint();
        switch(spellType){
            case 1:
                spellTypeSymbolPaint.setColor(Color.RED);
                break;
            case 2:
                spellTypeSymbolPaint.setColor(Color.BLUE);
                break;
            case 3:
                spellTypeSymbolPaint.setColor(Color.YELLOW);
                break;
        }
        canvas.drawCircle(x + 30.0f, y + 90.0f, 15.0f, spellTypeSymbolPaint);

        //draws mana cost on the card
        Paint manaTextPaint = new Paint();
        manaTextPaint.setColor(Color.BLACK);
        manaTextPaint.setTextAlign(Paint.Align.CENTER);
        manaTextPaint.setTextSize(40);
        manaTextPaint.setAntiAlias(true);
        canvas.drawText(Integer.toString(mana), x + 195.0f, y + 105f, manaTextPaint);

        //draws a forbidden icon (green circle place holder for now) if the card is forbidden
        if(isForbidden){
            Paint forbiddenSymbolPaint = new Paint();
            forbiddenSymbolPaint.setColor(Color.GREEN);
            canvas.drawCircle(x + 30.0f, y + 125.0f, 15.0f, forbiddenSymbolPaint);
        }

        //draws the effect text at the bottom of the card if needed
        if(hasCardText){
            Paint effectTextPaint = new Paint();
            effectTextPaint.setColor(Color.BLACK);
            effectTextPaint.setTextSize(20);
            effectTextPaint.setAntiAlias(true);
            ArrayList<String> wrappedEffectText = textLineWrap(effectText, 205.0f,
                    effectTextPaint);
            for(int i = 0; i < wrappedEffectText.size(); i++){
                canvas.drawText(wrappedEffectText.get(i), x + 10.0f, y + 250.0f + i * 15.0f,
                                effectTextPaint);
            }
        }

        //draws the power modifier on the bottom of the card if no card text is present
        else{
            Paint powerModTextPaint = new Paint();
            powerModTextPaint.setColor(Color.BLACK);
            powerModTextPaint.setTextSize(40.0f);
            powerModTextPaint.setAntiAlias(true);
            powerModTextPaint.setTextAlign(Paint.Align.CENTER);

            //adds a plus or minus to the front of the modifier
            char powerModSign = '+';
            if(powerMod < 0){
                powerModSign = '-';
            }
            String powerModText = powerModSign + Integer.toString(powerMod);

            canvas.drawText(powerModText, x + 112.5f, y + 270.0f,
                            powerModTextPaint);
        }

    }

    //draws a face down card on the screen
    protected void drawFaceDownCard(Canvas canvas, float x, float y, Color cardBack){
        //draw a filled square with the color cardBack at the x y coordinate
        //reminder the origin refers to the top left of the card

        drawCardOutline(canvas, x, y);

        //prints the string cheaty mages across the card (diagonally bottom left to top right)
        //horizontally in two lines would also work but diagonally should be able to be done with
        //drawTextOnPath
    }

    //draws the black border for the card outline
    protected void drawCardOutline(Canvas canvas, float x, float y){
        Paint outlinePaint = new Paint();
        outlinePaint.setColor(Color.BLACK);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(10.0f);
        canvas.drawRect(x, y, x + 225.0f, y + 300.0f, outlinePaint);
    }

    //draws the card name at the top of the card
    protected void drawCardTitle(Canvas canvas, float x, float y, String text){
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(40);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
        canvas.drawText(text, x + 112.5f, y + 60.0f, titlePaint);
    }

    //converts a string into an array of smaller strings using a max pixel width
    //this is intended be used to wrap text within a certain width
    //this code is functional for now but will cut off words and will need to be altered
    protected ArrayList<String> textLineWrap(String text, float width, Paint textPaint){
        Rect bounds = new Rect();
        ArrayList<String> splitText = new ArrayList<String>();
        int textStart = 0;
        for (int i = 0; i <= text.length(); i++) {
            textPaint.getTextBounds(text, textStart, i, bounds);
            if(bounds.width() > width) {
                splitText.add(text.substring(textStart, i - 1));
                textStart = i;
            }
        }
        splitText.add(text.substring(textStart));
        return splitText;
    }
}


