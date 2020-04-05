package com.example.mathapp;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PaintView extends View {

    private final int OFFSET = 30;
    private float mcNewPositionX = 30;
    private float mcNewPositionY = 30;
    private float mcOnTouchX;
    private float mcOnTouchY;
    private float moveX;
    private float moveY;
    int indexMyCircles = 0;
    private List<MyCircle> myCircles = new ArrayList<>();


    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isCircleNotPresent(event)) {
                    addNewCircle(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (circleOutOfScreen()) {
                    deleteCircle(event);
                } else {
                    changeColor(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveCircle(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /**
     * check if current event touched a circle
     * @param myCircle instance of class Mycircle taken from list myCircles
     * @param event instance of class Motionevent
     * @return true if event with point (eventX, eventY) is within circle having center (myCircleX,
     * myCircleY) and radius RADIUS
     */
    private boolean isEventInCircle(MyCircle myCircle, MotionEvent event) {
        return event.getX() <= myCircle.getX() + MyCircle.RADIUS && event.getX() >= myCircle.getX()
                - MyCircle.RADIUS && event.getY() <= myCircle.getY() + MyCircle.RADIUS &&
                event.getY() >= myCircle.getY() - MyCircle.RADIUS;
    }

    /**
     * check if new touch is whithin a circle. If so, set myIdCircle to this circle
     * and return that circle is already present, i.e. false. If no, just return true that circle is
     * not present
     * @return boolean true if circle is not in list myCircles
     */
    private boolean isCircleNotPresent(MotionEvent event) {
        moveX = event.getX();
        moveY = event.getY();

        boolean circleNotPresent = true;
        int i = 0;
        for (MyCircle mc : myCircles) {
            if (isEventInCircle(mc, event)) {
                circleNotPresent = false;
                mcOnTouchX = mc.getX();
                mcOnTouchY =  mc.getY();
                indexMyCircles = i; //index of myCircles of that instance myCircle which should be
                                // changed later (position, color, deletion)
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
    private void addNewCircle(MotionEvent event) {
        MyCircle newCircle = new MyCircle();
        newCircle.setX(moveX);
        newCircle.setY(moveY);
        myCircles.add(newCircle);
        mcOnTouchX = moveX;
        mcOnTouchY = moveY;
        indexMyCircles = myCircles.size() - 1;
        mcNewPositionX = mcOnTouchX + event.getX() - moveX;
        mcNewPositionY = mcOnTouchY + event.getY() - moveY;
    }

    /**
     * change color of this circle if the event just touched this circle and does not move in
     * another direction
     * @param event parameter of the new event needed to compute new position of this circle
     */
    private void changeColor(MotionEvent event) {
        mcNewPositionX = mcOnTouchX + event.getX() - moveX;
        mcNewPositionY = mcOnTouchY + event.getY() - moveY;
        if (mcNewPositionX <= mcOnTouchX +  MyCircle.RADIUS && mcNewPositionX >= mcOnTouchX -  MyCircle.RADIUS
                && mcNewPositionY <= mcOnTouchY +  MyCircle.RADIUS && mcNewPositionY >= mcOnTouchY -  MyCircle.RADIUS) {
            if (myCircles.get(indexMyCircles).getColor() == MyCircle.BLUE) {
                myCircles.get(indexMyCircles).setColor(MyCircle.RED);
                myCircles.get(indexMyCircles).setColorBorder(MyCircle.RED_BORDER);
            } else {
                myCircles.get(indexMyCircles).setColor(MyCircle.BLUE);
                myCircles.get(indexMyCircles).setColorBorder(MyCircle.BLUE_BORDER);
            }
        }
    }

    /**
     * set new position due to the new event to this circle
     * @param event paramer of the new event needed to compute new position of this circle
     */
    private void moveCircle(MotionEvent event) {
        mcNewPositionX = mcOnTouchX + event.getX() - moveX;
        mcNewPositionY = mcOnTouchY + event.getY() - moveY;
        myCircles.get(indexMyCircles).setX(mcNewPositionX);
        myCircles.get(indexMyCircles).setY(mcNewPositionY);
    }

    /**
     * check if the given point is out of the canvas
     * @return true if x value or y value are below offset or greater than width/ height
     */
    private boolean circleOutOfScreen() {
        return mcNewPositionX <= OFFSET || mcNewPositionX >= getWidth() - OFFSET
                || mcNewPositionY <= OFFSET || mcNewPositionY >= getHeight() - OFFSET;
    }

    /**
     * delete circles if center is outside the screen
     */
    private void deleteCircle(MotionEvent event) {
        if (myCircles.isEmpty()) return;
        myCircles.remove(indexMyCircles);
    }

    /**
     * fist draw rectangle that represents the canvas
     * second draw all points twice with different radius to simulate border of point
     * @param canvas canvas on which the rectangle and points are drawn
     */
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (MyCircle myCircle : myCircles) {
            canvas.drawCircle(myCircle.getX(), myCircle.getY(), MyCircle.RADIUS_BORDER, myCircle.getMyPaintBorder());
            canvas.drawCircle(myCircle.getX(), myCircle.getY(), MyCircle.RADIUS, myCircle.getMyPaint());
        }
        invalidate();
    }
}