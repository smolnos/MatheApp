package com.example.mathapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PaintView extends View {

    private static final float RADIUS = 40;
    private float x = 30;
    private float y = 30;
    private float initialX;
    private float initialY;
    private float offsetX;
    private float offsetY;
    private Paint myCircle;
    private Paint backgroundPaint;
    private List<Point> points = new ArrayList<>();

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        myCircle = new Paint();
        myCircle.setColor(Color.BLUE);
        myCircle.setAntiAlias(true);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initialX = x;
                initialY = y;
                offsetX = event.getX();
                offsetY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x = initialX + event.getX() - offsetX;
                y = initialY + event.getY() - offsetY;
                if (x <= initialX + RADIUS && y <= initialY + RADIUS) {
                    if (myCircle.getColor() == Color.BLUE) {
                        myCircle.setColor(Color.RED);
                    } else {
                        myCircle.setColor(Color.BLUE);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                x = initialX + event.getX() - offsetX;
                y = initialY + event.getY() - offsetY;
                break;
        }
        return (true);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        canvas.drawCircle(x, y, RADIUS, myCircle);
        invalidate();
    }
}