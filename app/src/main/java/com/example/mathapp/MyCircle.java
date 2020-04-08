package com.example.mathapp;

import android.graphics.Color;
import android.graphics.Paint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyCircle {
    public static final float RADIUS = 30;
    public static final float RADIUS_BORDER = 50;
    public static final int BLUE = Color.parseColor("#000ffa");
    public static final int BLUE_BORDER = Color.parseColor("#4287f5");
    public static final int RED = Color.parseColor("#ff0042");
    public static final int RED_BORDER = Color.parseColor("#ff9bb5");

    private float x;
    private float y;
    private Paint myPaint;
    private Paint myPaintBorder;
    private String color;

    MyCircle() {
        myPaint = new Paint();
        myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myPaint.setStrokeWidth(30F);
        myPaint.setColor(Color.parseColor("#000ffa"));
        myPaint.setAlpha(250);
        myPaint.setAntiAlias(true);
        myPaintBorder = new Paint();
        myPaintBorder.setStyle(Paint.Style.FILL);
        myPaintBorder.setColor(Color.parseColor("#4287f5"));
        myPaintBorder.setAlpha(250);
        myPaintBorder.setAntiAlias(true);
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
