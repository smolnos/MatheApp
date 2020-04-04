package com.example.mathapp;

import android.graphics.Color;
import android.graphics.Paint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyCircle {
    private float x;
    private float y;
    private Paint myPaint;

    public MyCircle() {
        myPaint = new Paint();
        myPaint.setColor(Color.BLUE);
        myPaint.setAntiAlias(true);
        myPaint.setStrokeWidth(30F);
    }

    void setColor(int color) {
        this.myPaint.setColor(color);
    }

    int getColor() {
        return this.myPaint.getColor();
    }
}
