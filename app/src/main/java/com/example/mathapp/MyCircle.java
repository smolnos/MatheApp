package com.example.mathapp;

import android.graphics.Color;
import android.graphics.Paint;

public class MyCircle {
    private float x;
    private float y;
    private Paint myPaint;

    public MyCircle() {
        myPaint = new Paint();
        myPaint.setColor(Color.BLUE);
        myPaint.setAntiAlias(true);
    }

    public MyCircle(float x, float y, Paint myPaint) {
        this.x = x;
        this.y = y;
        this.myPaint = myPaint;
    }

    public MyCircle(float x, float y) {
        this.x = x;
        this.y = y;
        myPaint = new Paint();
        myPaint.setColor(Color.BLUE);
        myPaint.setAntiAlias(true);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    public Paint getMyPaint() {
        return myPaint;
    }

    public void setMyPaint(Paint myPaint) {
        this.myPaint = myPaint;
    }

}
