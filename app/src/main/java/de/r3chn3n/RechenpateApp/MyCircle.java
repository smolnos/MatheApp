package de.r3chn3n.RechenpateApp;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyCircle {
    public static final float RADIUS = 20;
    public static final float RADIUS_BORDER = 40;
    public static final int BLUE = Color.parseColor("#000ffa");
    public static final int BLUE_BORDER = Color.parseColor("#4287f5");
    public static final int RED = Color.parseColor("#ff0042");
    public static final int RED_BORDER = Color.parseColor("#ff9bb5");


    private float x;
    private float y;
    private Paint myPaint;
    private Paint myPaintBorder;
    private Paint myText;
    private String text = "x";
    private Rect mBounds = new Rect();
    private String color;
    private Variable myVariable;


    MyCircle() {
        myPaint = new Paint();
        myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myPaint.setStrokeWidth(30F);
        myPaint.setColor(BLUE);
        myPaint.setAlpha(250);
        myPaint.setAntiAlias(true);
        myPaintBorder = new Paint();
        myPaintBorder.setStyle(Paint.Style.FILL);
        myPaintBorder.setColor(BLUE_BORDER);
        myPaintBorder.setAlpha(250);
        myPaintBorder.setAntiAlias(true);
        myText = new Paint();
        myText.setColor(Color.WHITE);
        myText.setTextAlign(Paint.Align.LEFT);
        myText.setTextSize(70);
        myText.getTextBounds(text, 0, text.length(), mBounds);
        myVariable = Variable.number;
    }

    void setColor(int color) {
        this.myPaint.setColor(color);
    }

    int getColor() {
        return this.myPaint.getColor();
    }

    void setColorBorder(int parseColor) {
        this.myPaintBorder.setColor(parseColor);
    }

}
