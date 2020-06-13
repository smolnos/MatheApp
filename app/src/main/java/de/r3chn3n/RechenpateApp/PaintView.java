package de.r3chn3n.RechenpateApp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaintView extends View {

    private final int OFFSET = 30;
    private float mcNewPositionX = 30;
    private float mcNewPositionY = 30;
    private float mcOnTouchX;
    private float mcOnTouchY;
    private float moveX;
    private float moveY;
    int indexMyCircles = 0;
    boolean changed = false;
    private List<MyCircle> myCircles = new ArrayList<>();


    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintView(Context context) {
        super(context);
    }

    /**
     * Depending on the event, a new circle is added (if there was no circle before), the color is
     * changed (if there was a circle before) or the shape is changed (if the duration of the touch
     * is longer than 2 seconds), the circle is deleted (if this circle is no longer on the screen),
     * or the circle is moved
     * @param event touch event
     * @return boolean always return true
     */
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        long eventDuration = event.getEventTime() - event.getDownTime();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isCircleNotPresent(event)) {
                    addNewCircle(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (circleOutOfScreen()) {
                    deleteCircle();
                } else {
                    if (!myCircles.get(indexMyCircles).isShapeHasChanged()) {
                        changeColor(event);
                    } else {
                        myCircles.get(indexMyCircles).setShapeHasChanged(false);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mcNewPositionX = mcOnTouchX + event.getX() - moveX;
                mcNewPositionY = mcOnTouchY + event.getY() - moveY;
                if (isTouchStillInScreen()) {
                    if (eventDuration > 200) {
                        changeShape(event);
                    }
                } else {
                        moveCircle(event);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private boolean isTouchStillInScreen() {
        return mcNewPositionX <= mcOnTouchX + MyCircle.RADIUS * 2 / 3 && mcNewPositionX >= mcOnTouchX - MyCircle.RADIUS * 2 / 3
                && mcNewPositionY <= mcOnTouchY + MyCircle.RADIUS * 2 / 3 && mcNewPositionY >= mcOnTouchY - MyCircle.RADIUS * 2 / 3;
    }

    /**
     * if myCircle has been touched for more than 2 seconds, myVariable is changed to X if it was
     * number before or vice versa.
     * Therefore the shape is changed from circle to rectangle, if it was number first or from
     * rectangle to circle if it was X before.
     */
    private void changeShape(MotionEvent event) {
        if (!myCircles.get(indexMyCircles).isShapeHasChanged()) {
                if (myCircles.get(indexMyCircles).getMyVariable() == Variable.number) {
                    myCircles.get(indexMyCircles).setMyVariable(Variable.X);
                } else {
                    myCircles.get(indexMyCircles).setMyVariable(Variable.number);
                }
                myCircles.get(indexMyCircles).setShapeHasChanged(true);
        }
    }

    /**
     * check if current event touched a circle
     * @param myCircle instance of class Mycircle taken from list myCircles
     * @param event instance of class Motionevent
     * @return true if event with point (eventX, eventY) is within circle having center (myCircleX,
     * myCircleY) and radius RADIUS
     */
    private boolean isEventInCircle(MyCircle myCircle, MotionEvent event) {
        return event.getX() <= myCircle.getX() + MyCircle.RADIUS + OFFSET / 2. && event.getX() >= myCircle.getX()
                - MyCircle.RADIUS -  OFFSET / 2. && event.getY() <= myCircle.getY() + MyCircle.RADIUS  + OFFSET / 2. &&
                event.getY() >= myCircle.getY() - MyCircle.RADIUS -  OFFSET / 2.;
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
        if (isTouchStillInScreen()) {
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
    private void deleteCircle() {
        if (myCircles.isEmpty()) return;
        myCircles.remove(indexMyCircles);
        if (myCircles.isEmpty()) return;
        mcNewPositionX = myCircles.get(myCircles.size() - 1).getX();
        mcNewPositionY = myCircles.get(myCircles.size() - 1).getY();
    }

    /**
     * second draw all points twice with different radius to simulate border of point
     * if myVariable in myCircle has the value X, a rectangle is drawn instead of a circle
     * @param canvas canvas on which the rectangle and points are drawn
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (MyCircle myCircle : myCircles) {
            if (myCircle.getMyVariable() == Variable.X) {
                drawRectangle(canvas, myCircle);
//                drawStar(canvas, myCircle);
                drawText(canvas, myCircle);
            } else {
                canvas.drawCircle(myCircle.getX(), myCircle.getY(), MyCircle.RADIUS_BORDER, myCircle.getMyPaintBorder());
                canvas.drawCircle(myCircle.getX(), myCircle.getY(), MyCircle.RADIUS, myCircle.getMyPaint());
            }
        }
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawStar(Canvas canvas, MyCircle myCircle) {
        float left = myCircle.getX() - MyCircle.RADIUS_BORDER *  2;
        float top = myCircle.getY() - MyCircle.RADIUS_BORDER * 2;
        float right = myCircle.getX() + MyCircle.RADIUS_BORDER * 2;
        float bottom = myCircle.getY() + MyCircle.RADIUS_BORDER * 2;
        Drawable d = getResources().getDrawable(R.drawable.ic_star_24px, null);
        d.mutate().setColorFilter(myCircle.getMyPaint().getColor(), PorterDuff.Mode.SRC_IN);
        d.setBounds((int)left, (int)top, (int)right, (int)bottom);
        d.draw(canvas);
    }

    private void drawText(Canvas canvas, MyCircle myCircle) {
        // Calculate x and y for text so it's centered.
        float x = myCircle.getX() - myCircle.getMBounds().centerX();
        float y =  myCircle.getY() - myCircle.getMBounds().centerY();
        canvas.drawText(myCircle.getText(), x, y, myCircle.getMyText());
    }

    private void drawRectangle(Canvas canvas, MyCircle myCircle) {


        float left = myCircle.getX() - MyCircle.RADIUS_BORDER;
        float top = myCircle.getY() - MyCircle.RADIUS_BORDER;
        float right = myCircle.getX() + MyCircle.RADIUS_BORDER;
        float bottom = myCircle.getY() + MyCircle.RADIUS_BORDER;
        canvas.drawRect(left, top, right, bottom, myCircle.getMyPaintBorder());

        left = myCircle.getX() - MyCircle.RADIUS;
        top = myCircle.getY() - MyCircle.RADIUS;
        right = myCircle.getX() + MyCircle.RADIUS ;
        bottom = myCircle.getY() + MyCircle.RADIUS;
        canvas.drawRect(left, top, right, bottom, myCircle.getMyPaint());
    }

    @Override
    protected void onSizeChanged(int width, int height,
                                 int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        float tmp;
        for (MyCircle myCircle : myCircles) {
            tmp = myCircle.getX();
            myCircle.setX(myCircle.getY());
            myCircle.setY(tmp);
        }

    }

    public void deleteCircles() {
        indexMyCircles = 0;
        myCircles.clear();
    }
}