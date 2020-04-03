package com.example.mathapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Pair;
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
    private Paint backgroundPaint;
    int myIdCircle = 0;
    private List<MyCircle> myCircles = new ArrayList<>();

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        MyCircle initialCircle = new MyCircle();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initialX = x;
                initialY = y;
                offsetX = event.getX();
                offsetY = event.getY();

                boolean circleNotPresent = true;
                int i = 0;
                for (MyCircle mc : myCircles) {
                    if (offsetX <= mc.getX() + RADIUS && offsetX >= mc.getX()  - RADIUS
                            && offsetY <= mc.getY()  + RADIUS && offsetY >= mc.getY() - RADIUS) {
                        circleNotPresent = false;
                        initialX = mc.getX();
                        initialY =  mc.getY();
                        myIdCircle = i;
                        break;
                    }
                    i++;
                }
                if (circleNotPresent) {
                    initialCircle.setX(offsetX);
                    initialCircle.setY(offsetY);
                    myCircles.add(initialCircle);
                    initialX = offsetX;
                    initialY = offsetY;
                    myIdCircle = i++;
                }
                break;
            case MotionEvent.ACTION_UP:
                x = initialX + event.getX() - offsetX;
                y = initialY + event.getY() - offsetY;
                if (x <= initialX + RADIUS && x >= initialX - RADIUS
                        && y <= initialY + RADIUS && y >= initialY - RADIUS) {
                    if (myCircles.get(myIdCircle).getMyPaint().getColor() == Color.BLUE) {
                        myCircles.get(myIdCircle).getMyPaint().setColor(Color.RED);
                    } else {
                        myCircles.get(myIdCircle).getMyPaint().setColor(Color.BLUE);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                x = initialX + event.getX() - offsetX;
                y = initialY + event.getY() - offsetY;
                myCircles.get(myIdCircle).setX(x);
                myCircles.get(myIdCircle).setY(y);
                break;
        }
        return (true);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (MyCircle myCircle : myCircles) {
            canvas.drawCircle(myCircle.getX(), myCircle.getY(), RADIUS, myCircle.getMyPaint());
        }
        invalidate();
    }
}