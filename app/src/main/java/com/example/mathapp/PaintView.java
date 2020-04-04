package com.example.mathapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PaintView extends View {

    private float previousX;
    private float previousY;
    private float currentX;
    private float currentY;
    private Paint backgroundPaint;
    int myIdCircle = 0;
    private List<MyCircle> myCircles = new ArrayList<>();

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
    }

    @Override
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

    /**
     * check if current event touched a circle
     * @param currentX x value of event
     * @param x x value of circle
     * @param currentY y value of event
     * @param y y value of circle
     * @return true if event with point (currentX, currentY) is within circle having center (x, y)
     *          and radius RADIUS
     */
    private boolean isEventInCircle(float currentX, float x, float currentY, float y) {
        return currentX <= x + MyCircle.RADIUS && currentX >= x - MyCircle.RADIUS
                && currentY <= y + MyCircle.RADIUS && currentY >= y - MyCircle.RADIUS;
    }

    /**
     * check if new touch is whithin a circle. If so, set myIdCircle to this circle
     * and return that circle already present, i.e. false. If no, just return true that circle is
     * not present
     * @return boolean true if circle is not in list myCircles
     */
    private boolean isCircleNotPresent(MotionEvent event) {
        currentX = event.getX();
        currentY = event.getY();
        boolean circleNotPresent = true;
        int i = 0;
        for (MyCircle mc : myCircles) {
            if (isEventInCircle(currentX, mc.getX(), currentY, mc.getY())) {
                circleNotPresent = false;
                previousX = mc.getX();
                previousY =  mc.getY();
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
        initialCircle.setX(currentX);
        initialCircle.setY(currentY);
        myCircles.add(initialCircle);
        previousX = currentX;
        previousY = currentY;
        myIdCircle = myCircles.size() - 1;
    }

    /**
     * change color of this circle if the event just touched this circle and does not move in
     * another direction
     * @param event paramer of the new event needed to compute new position of this circle
     */
    private void changeColorOfCircle(MotionEvent event) {

        float x = previousX + event.getX() - currentX;
        float y = previousY + event.getY() - currentY;
        if (isEventInCircle(x, previousX, y, previousY)) {
            if (myCircles.get(myIdCircle).getColor() == MyCircle.BLUE) {
                myCircles.get(myIdCircle).setColor(MyCircle.RED);
                myCircles.get(myIdCircle).setColorBorder(MyCircle.RED_BORDER);
            } else {
                myCircles.get(myIdCircle).setColor(MyCircle.BLUE);
                myCircles.get(myIdCircle).setColorBorder(MyCircle.BLUE_BORDER);
            }
        }
    }

    /**
     * set new position due to the new event to this circle
     * @param event paramer of the new event needed to compute new position of this circle
     */
    private void changePositionOfCircle(MotionEvent event) {
        float x = previousX + event.getX() - currentX;
        float y = previousY + event.getY() - currentY;
        myCircles.get(myIdCircle).setX(x);
        myCircles.get(myIdCircle).setY(y);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawRect(0, 0, width, height, backgroundPaint);
        for (MyCircle myCircle : myCircles) {
            canvas.drawCircle(myCircle.getX(), myCircle.getY(), MyCircle.RADIUS_BORDER, myCircle.getMyPaintBorder());
            canvas.drawCircle(myCircle.getX(), myCircle.getY(), MyCircle.RADIUS, myCircle.getMyPaint());

        }
        invalidate();
    }
}