package com.example.prince.camruler;

/**
 * Created by prince on 2017-11-29.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class DrawingOnImage extends SurfaceView {
    Button btnTest;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    List<Point> circlePoints;
    private Context context;
    private ViewGroup mainLayout;
    //private HashMap<Point,Integer> mCircles;
    private SparseArray<Point> mCirclePointer;
    int nums;


    private static int REFERENCE_POINT_COLOR = Color.YELLOW;
    private static int MEASURE_POINT_COLOR = Color.RED;
    private static int MEASURE_POINT_COLOR2 = Color.BLUE;

    public DrawingOnImage(Context context, int num) {
        super(context);
        nums = num;
        // mCircles = new HashMap<Point,Integer>();
        mCirclePointer = new SparseArray<Point>();
        this.context = context;
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        circlePoints = new ArrayList<>();
        setWillNotDraw(false);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }



    @Override
    protected void onDraw(Canvas canvas){
        int size = circlePoints.size();
        for(int i = 0; i < size; i++){
            //Set color based on order. First 2 points are the reference points.
            if(i < 2){
                paint.setColor(REFERENCE_POINT_COLOR);


            }
            else if (i>1 && i<4) {
                paint.setColor(MEASURE_POINT_COLOR);

            }
            else{
                paint.setColor(MEASURE_POINT_COLOR2);
            }
            Point p = circlePoints.get(i);
            canvas.drawCircle(p.x, p.y, 60, paint);

            if(i == 1){
                canvas.drawLine(circlePoints.get(0).x, circlePoints.get(0).y, circlePoints.get(1).x, circlePoints.get(1).y, paint);
            }
            if(i == 3){
                canvas.drawLine(circlePoints.get(2).x, circlePoints.get(2).y, circlePoints.get(3).x, circlePoints.get(3).y, paint);
            }
            if(i == 5){
                canvas.drawLine(circlePoints.get(4).x, circlePoints.get(4).y, circlePoints.get(5).x, circlePoints.get(5).y, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionIndex = event.getActionIndex();
        int pointerId;
        int xTouch = 0;
        int yTouch = 0;
        Point touchedCircle;
        Point touchedPoint;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            xTouch = (int) event.getX(0);
            yTouch = (int) event.getY(0);
            pointerId = event.getPointerId(actionIndex);
            if (getTouchedCircle(xTouch,yTouch) == null) {

                if (circlePoints.size() < nums) {
                    Point first = new Point(Math.round(event.getX()), Math.round(event.getY()));
                    circlePoints.add(first);
                    //mCircles.put(first,pointerId);
                    xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);

                    //check if we've touched inside some circle
                    touchedCircle = obtainTouchedCircle(xTouch, yTouch);
                    touchedCircle.x = xTouch;
                    touchedCircle.y = yTouch;
                    mCirclePointer.put(event.getPointerId(0), touchedCircle);

                    invalidate();
                    if (circlePoints.size() == 2) {
                        //((TextView) ((Activity)context).findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setMeasurePoints));
                    }
                    if (circlePoints.size() == 4) {
                        // ((TextView) ((Activity)context).findViewById(R.id.info_lbl)).setText(getResources().getString(R.string.setScaleValue));
                    }
                }
            }
            else{

            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final int pointerCount = event.getPointerCount();
            Point pointinscreen = getTouchedCircle(xTouch,yTouch);

//            if (pointinscreen != null) {
//                xTouch = (int) pointinscreen.x;
//                yTouch = (int) pointinscreen.y;
//
//                touchedCircle = mCirclePointer.get(mCircles.get(pointinscreen));
//
//                if (null != touchedCircle) {
//                    touchedCircle.x = xTouch;
//                    touchedCircle.y = yTouch;
//                }
//
//            }

            for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                // Some pointer has moved, search it by pointer id
                pointerId = event.getPointerId(actionIndex);

                xTouch = (int) event.getX(actionIndex);
                yTouch = (int) event.getY(actionIndex);

                touchedCircle = mCirclePointer.get(pointerId);

                if (null != touchedCircle) {
                    touchedCircle.x = xTouch;
                    touchedCircle.y = yTouch;
                }
            }
            invalidate();

        }
//        if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
//            // It secondary pointers, so obtain their ids and check circles
//            pointerId = event.getPointerId(actionIndex);
//
//            xTouch = (int) event.getX(actionIndex);
//            yTouch = (int) event.getY(actionIndex);
//
//            // check if we've touched inside some circle
//            touchedCircle = obtainTouchedCircle(xTouch, yTouch);
//
//            mCirclePointer.put(pointerId, touchedCircle);
//            touchedCircle.x = xTouch;
//            touchedCircle.y = yTouch;
//            invalidate();
//        }



        if (event.getAction() == MotionEvent.ACTION_POINTER_UP)

        {
            // not general pointer was up
            pointerId = event.getPointerId(actionIndex);

            mCirclePointer.remove(pointerId);
            invalidate();

        }
        return true;
    }
    /**
     * Search and creates new (if needed) circle based on touch area
     *
     * @param xTouch int x of touch
     * @param yTouch int y of touch
     *
     * @return obtained {@link Point}
     */
    private Point obtainTouchedCircle(final int xTouch, final int yTouch) {
        Point touchedCircle = getTouchedCircle(xTouch, yTouch);

        if (null == touchedCircle) {
            touchedCircle = new Point(xTouch, yTouch);

//            if (mCircles.size() == 3) {
//              //  Log.w(TAG, "Clear all circles, size is " + mCircles.size());
//                // remove first circle
//                mCircles.clear();
//            }

            //Log.w(TAG, "Added circle " + touchedCircle);
            circlePoints.add(touchedCircle);
        }

        return touchedCircle;
    }
    /**
     * Determines touched circle
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     *
     * @return {@link Point} touched circle or null if no circle has been touched
     */
    private Point getTouchedCircle(final int xTouch, final int yTouch) {
        Point touched = null;

        for (Point circle : circlePoints) {
            if ((circle.x - xTouch) * (circle.x - xTouch) + (circle.y - yTouch) * (circle.y - yTouch) <= 10*10) {
                touched = circle;
                break;
            }
        }

        return touched;
    }

    /**
     * Clears all drawn points and shapes
     */
    public void clearCanvas(){
        if (circlePoints.size() <= 2){
            circlePoints.remove(circlePoints.size()-1);
            nums=2;
        } else if (2 < circlePoints.size() & circlePoints.size() <= 4 ){
            circlePoints.remove(circlePoints.size()-1);
            nums=4;
        }
        else if (4 < circlePoints.size() & circlePoints.size() <= 6 ){
            circlePoints.remove(circlePoints.size()-1);
            nums=6;
        }

//        for (int i = 0; i < circlePoints.size(); i++){
//            circlePoints.remove(i);
//        }
        //((TextView) ((Activity)context).findViewById(R.id.textView2)).setText(getResources().getString(R.string.setPicture));
        invalidate();
    }


    /**
     * Calculates the measurement
     * @param reference The reference size
     * @param inputUnitIndex The input length unit index
     * @param outputUnitIndex The output length unit index
     * @return The value of the measurement, converted to outputUnitIndex
     */
    public double [] calculate(double reference, int inputUnitIndex, int outputUnitIndex){
        if(circlePoints.size() != 4 && MainActivity.objectsCount.equals("1")){
            Toast.makeText(context, "Select points for desired object", Toast.LENGTH_SHORT).show();
            return null;
        }
        if(circlePoints.size() != 6 && MainActivity.objectsCount.equals("2")){
            Toast.makeText(context, "Select points for desired object", Toast.LENGTH_SHORT).show();
            return null;
        }
        return Calculate.compute(circlePoints, reference, inputUnitIndex, outputUnitIndex);
    }


}