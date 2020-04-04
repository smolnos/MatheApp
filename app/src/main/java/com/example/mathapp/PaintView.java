package com.example.mathapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PaintView extends View {

    private static final float RADIUS = 80;
    private float x = -1;
    private float y = -1;
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
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isCircleNotPresent(event)) {
                    addNewCircle();
                }
                break;
            case MotionEvent.ACTION_UP:
                changeColorOfCircle(event);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                changePositionOfCircle(event);
                break;
        }
        return (true);
    }

    private boolean isEventInCircle(float offsetX, float x, float offsetY, float y) {
        return offsetX <= x + RADIUS && offsetX >= x - RADIUS
                && offsetY <= y + RADIUS && offsetY >= y - RADIUS;
    }

    /**
     * check if new touch is whithin a circle. If so, set myIdCircle to this circle
     * and return that circle already present, i.e. false. If no, just return true that circle is
     * not present
     * @return boolean true if circle is not in list myCircles
     */
    private boolean isCircleNotPresent(MotionEvent event) {
        initialX = x;
        initialY = y;
        offsetX = event.getX();
        offsetY = event.getY();
        boolean circleNotPresent = true;
        int i = 0;
        for (MyCircle mc : myCircles) {
            if (isEventInCircle(offsetX, mc.getX(), offsetY, mc.getY())) {
                circleNotPresent = false;
                initialX = mc.getX();
                initialY =  mc.getY();
                myIdCircle = i;
                break;
            }
            i++;
        }
        return circleNotPresent;
    }

    /**
     * create a new instance of the class MyCircle and add this with new paramter of the event to
     * the list myCircles. Set myIdCircle on the position of this new instantiated object
     */
    private void addNewCircle() {
        MyCircle initialCircle = new MyCircle();
        initialCircle.setX(offsetX);
        initialCircle.setY(offsetY);
        myCircles.add(initialCircle);
        initialX = offsetX;
        initialY = offsetY;
        myIdCircle = myCircles.size() - 1;
    }

    /**
     * change color of this circle if the event just touched this circle and does not move in
     * another direction
     * @param event paramer of the new event needed to compute new position of this circle
     */
    private void changeColorOfCircle(MotionEvent event) {
        x = initialX + event.getX() - offsetX;
        y = initialY + event.getY() - offsetY;
        if (isEventInCircle(x, initialX, y, initialY)) {
            if (myCircles.get(myIdCircle).getColor() == Color.BLUE) {
                myCircles.get(myIdCircle).setColor(Color.RED);
            } else {
                myCircles.get(myIdCircle).setColor(Color.BLUE);
            }
        }
    }

    /**
     * set new position due to the new event to this circle
     * @param event paramer of the new event needed to compute new position of this circle
     */
    private void changePositionOfCircle(MotionEvent event) {
        x = initialX + event.getX() - offsetX;
        y = initialY + event.getY() - offsetY;
        myCircles.get(myIdCircle).setX(x);
        myCircles.get(myIdCircle).setY(y);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (MyCircle myCircle : myCircles) {
            canvas.drawCircle(myCircle.getX(), myCircle.getY(), RADIUS, myCircle.getMyPaint());
        }
        invalidate();
    }
}