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

    private float cardHeight = 240;
    private float cardWidth = 180;

    public CustomSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        setWillNotDraw(false);

        //makes the surface view background to transparent
        //code is adapted from https://stackoverflow.com/a/27959612
        this.setZOrderOnTop(true);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    protected void onDraw(Canvas canvas){
        drawFighterCard(canvas, 50, 400, "Orc", 2, 8,
                true);
        drawFighterCard(canvas, 50, 700, "Ghost", 5, 5,
                false);
        drawFighterCard(canvas, 50, 1000, "Dragon", 10, 3,
                true);
        drawFighterCard(canvas, 50, 1300, "Skeleton", 7, 3,
                false);
        drawFighterCard(canvas, 50, 1600, "Goblin", 1, 10,
                false);
        drawSpellCard(canvas, 300, 400, "Healing", 1, 1,+4,
                false, "", false);
        drawSpellCard(canvas, 300, 700, "Blizzard", 4, 1,-6,
                false, "", false);
        drawFaceDownCard(canvas, 300, 1000, Color.GRAY);

        drawSpellCard(canvas, 50, 2000, "Might", 4, 2,+5,
                false, "", false);
        //yes this is weird and there is some bug going on but it works for now
        drawSpellCard(canvas, 350, 2000, "Morph", 0, 3,0,
                true, "Discard targetfighter and        .draw a new     .one",
                true);
        drawSpellCard(canvas, 650, 2000, "Giantism", 10, 2,+12,
                false, "", true);
    }

    //draws a fighter card on the screen
    //x and y are the top left corner of the card
    protected void drawFighterCard(Canvas canvas, float x, float y, String fighterName,
                                   int power, int prizeGold, boolean hasBet){

        //draws a yellow translucent border around the card if you've placed a bet on it
        if(hasBet){
            Paint outlinePaint = new Paint();
            outlinePaint.setColor(0x70FFD700);
            outlinePaint.setStyle(Paint.Style.STROKE);
            outlinePaint.setStrokeWidth(10.0f);
            canvas.drawRect(x - 10.0f, y - 10.0f, x + cardWidth + 10.0f, y + cardHeight + 10.0f,
                    outlinePaint);
        }

        drawCardOutline(canvas, x, y);
        drawCardTitle(canvas, x, y, fighterName);

        //draws the circle for the fighter's power
        Paint red = new Paint();
        red.setColor(Color.RED);
        canvas.drawCircle(x + 40.0f, y + cardHeight - 40.0f, 25.0f, red);

        //draws the circle for the fighter's prize gold
        Paint gold = new Paint();
        gold.setColor(0xFFFFD700);
        canvas.drawCircle(x + cardWidth - 40.0f, y + cardHeight - 40.0f, 25.0f, gold);

        //draws the text for the fighter's stats
        Paint statText = new Paint();
        statText.setColor(Color.BLACK);
        statText.setTextSize(40);
        statText.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(Integer.toString(power), x + 40.0f, y + cardHeight - 25.0f, statText);
        canvas.drawText(Integer.toString(prizeGold), x + cardWidth - 40.0f, y + cardHeight - 25.0f, statText);

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
                spellTypeSymbolPaint.setColor(0xFFFFD700);
                break;
        }
        canvas.drawCircle(x + 25.0f, y + 75.0f, 12.5f, spellTypeSymbolPaint);

        //draws mana cost on the card
        if(mana != 0) {
            Paint manaTextPaint = new Paint();
            manaTextPaint.setColor(Color.BLACK);
            manaTextPaint.setTextAlign(Paint.Align.CENTER);
            manaTextPaint.setTextSize(40);
            manaTextPaint.setAntiAlias(true);
            canvas.drawText(Integer.toString(mana), x + cardWidth - 30.0f, y + 90.0f, manaTextPaint);
        }

        //draws a forbidden icon (green circle place holder for now) if the card is forbidden
        if(isForbidden){
            Paint forbiddenSymbolPaint = new Paint();
            forbiddenSymbolPaint.setColor(Color.GREEN);
            canvas.drawCircle(x + 25.0f, y + 95.0f, 12.5f, forbiddenSymbolPaint);
        }

        //draws the effect text at the bottom of the card if needed
        if(hasCardText){
            Paint effectTextPaint = new Paint();
            effectTextPaint.setColor(Color.BLACK);
            effectTextPaint.setTextSize(25);
            effectTextPaint.setAntiAlias(true);
            ArrayList<String> wrappedEffectText = textLineWrap(effectText, cardWidth - 30.0f,
                    effectTextPaint);
            for(int i = 0; i < wrappedEffectText.size(); i++){
                canvas.drawText(wrappedEffectText.get(i), x + 10.0f, y + cardHeight - 80.0f + i * 22.5f,
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
            String powerModText = "";
            if(powerMod > 0){
                powerModText = "+";
            }
            powerModText += Integer.toString(powerMod);

            canvas.drawText(powerModText, x + cardWidth/2, y + cardHeight - 25.0f,
                            powerModTextPaint);
        }

    }

    //draws a face down card on the screen
    protected void drawFaceDownCard(Canvas canvas, float x, float y, int cardBackColor){
        //draws the card back's base color first
        Paint cardBackFillPaint = new Paint();
        cardBackFillPaint.setColor(cardBackColor);
        canvas.drawRect(x, y, x + cardWidth, y + cardHeight, cardBackFillPaint);

        //draws the outline of the card
        drawCardOutline(canvas, x, y);

        //prints the "cheaty mages" across the card
        Paint cardBackTextPaint = new Paint();
        cardBackTextPaint.setColor(Color.BLACK);
        cardBackTextPaint.setTextAlign(Paint.Align.CENTER);
        cardBackTextPaint.setTextSize(35.0f);
        cardBackTextPaint.setAntiAlias(true);
        canvas.drawText("CHEATY", x + 75.0f, y + 75.0f, cardBackTextPaint);
        canvas.drawText("MAGES", x + 75.0f, y + 125.0f, cardBackTextPaint);
    }

    //draws the black border for the card outline
    protected void drawCardOutline(Canvas canvas, float x, float y){
        Paint outlinePaint = new Paint();
        outlinePaint.setColor(Color.BLACK);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(10.0f);
        canvas.drawRect(x, y, x + cardWidth, y + cardHeight, outlinePaint);
    }

    //draws the card name at the top of the card
    protected void drawCardTitle(Canvas canvas, float x, float y, String text){
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(35.0f);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
        canvas.drawText(text, x + cardWidth/2, y + 50.0f, titlePaint);
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
                splitText.add(text.substring(textStart, i));
                textStart = i;
            }
        }
        splitText.add(text.substring(textStart));
        return splitText;
    }
}
