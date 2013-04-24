package com.mcliang.seismograph;

import java.util.Stack;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Axis {

    private float axis;
    private float leftBound;
    private float rightBound;
    private SizedStack<Float> pts;
    private Paint paint;
    private int dashPhaser = 0;
    
    private static float parentViewHeight = 64;
    private static int brushLength = 50;
    private static int brushWidth = 8;
    private static int interval = 2;
    private static int boundOverflow = 20;
    private static float amplify;
    private static Paint penPaint;
    private static Paint graphPaint;

    public Axis(float axis, float leftBound, float rightBound) {
        setAxis(axis);
        setLeftBound(leftBound-boundOverflow);
        setRightBound(rightBound+boundOverflow);
        setPts(new SizedStack<Float>(getStackSize()));
        getPts().add((float) 0);
        setPaint(new Paint());
        setPaintColor(Color.DKGRAY);
        setPenPaint(new Paint());
        getPenPaint().setColor(Color.DKGRAY);
        getPenPaint().setStrokeWidth(2);
        setGraphPaint(new Paint());
        getGraphPaint().setColor(Color.LTGRAY);
        getGraphPaint().setStyle(Style.STROKE);
    }

    public Axis(float leftBound, float rightBound) {
        this((leftBound + rightBound) / 2, leftBound, rightBound);
    }

    public float[] getFloatArray() {
        SizedStack<Float> stackClone = (SizedStack<Float>)this.getPts().clone();
        float curY = brushLength;
        float pointA, pointB;
        float[] flArray = new float[(int) (((getParentViewHeight() + 8 - getBrushLength()) / getInterval())*4)];
        int i = 0;
        pointA = getNextPoint(stackClone);
        while (curY <= getParentViewHeight() && stackClone.size() >= 2) {
            flArray[i++] = pointA;
            flArray[i++] = curY;
            pointB = getNextPoint(stackClone);
            flArray[i++] = pointB;
            flArray[i++] = curY+=interval;
            flArray[i++] = pointB;
            flArray[i++] = curY;
            pointA = getNextPoint(stackClone);
            flArray[i++] = pointA;
            flArray[i++] = curY+=interval;
        }
        return flArray;
    }

    private float getNextPoint(Stack<Float> stackClone) {
        float point;
        point = this.getAxis() + (stackClone.isEmpty() || stackClone.peek() == null ? 0 : stackClone.pop() * Axis.getAmplify());
        if (point < this.getLeftBound()) point = this.getLeftBound();
        if (point > this.getRightBound()) point = this.getRightBound();
        return point;
    }

    public void plotLine(Canvas canvas) {
        this.drawPen(canvas);
        canvas.drawLines(getFloatArray(), getPaint());
        drawGraph(canvas);
    }

    private void drawGraph(Canvas canvas) {
        getGraphPaint().setPathEffect(new DashPathEffect(new float[] {10,10}, dashPhaser+=getInterval()));
        canvas.drawLine(getAxis() + 1*getAmplify(), getParentViewHeight(), getAxis() + 1*getAmplify(), 0, getGraphPaint());
        canvas.drawLine(getAxis() - 1*getAmplify(), getParentViewHeight(), getAxis() - 1*getAmplify(), 0, getGraphPaint());
    }

    public void drawPen(Canvas canvas) {
        canvas.drawLine(getPenLeftStartX(), 0, getPenEndX(), getBrushLength(), getPenPaint());
        canvas.drawLine(getPenRightStartX(), 0, getPenEndX(), getBrushLength(), getPenPaint());
    }

    private float getPenLeftStartX() {
        return (getAxis() - getBrushWidth() + getAxis() + getPts().peek() * getAmplify()) / 2;
    }

    private float getPenRightStartX() {
        return (getAxis() + getBrushWidth() + getAxis() + getPts().peek() * getAmplify()) / 2;
    }

    private float getPenEndX() {
        float point = getAxis() + getPts().peek() * getAmplify();
        if (point < this.getLeftBound()) point = this.getLeftBound();
        if (point > this.getRightBound()) point = this.getRightBound();
        return point;
    }

    /********************************************************************
     * getters & setters
     ********************************************************************/
        
    public static Paint getPenPaint() {
        return penPaint;
    }

    public static void setPenPaint(Paint penPaint) {
        Axis.penPaint = penPaint;
    }

    private static int getStackSize() {
        return (int) (((getParentViewHeight() + 8 - getBrushLength()) / getInterval()));
    }

    private static void updatePtsSize() {
        SizedStack.setMaxSize(getStackSize());
    }

    public SizedStack<Float> getPts() {
        return (SizedStack<Float>)pts;
    }

    public void setPts(SizedStack<Float> sizedStack) {
        this.pts = sizedStack;
    }

    public int getDashPhaser() {
        return dashPhaser;
    }

    public void setDashPhaser(int dashPhaser) {
        this.dashPhaser = dashPhaser;
    }

    public static int getBoundOverflow() {
        return boundOverflow;
    }

    public static void setBoundOverflow(int boundOverflow) {
        Axis.boundOverflow = boundOverflow;
    }

    public float getAxis() {
        return axis;
    }

    public void setAxis(float axis) {
        this.axis = axis;
    }

    public float getLeftBound() {
        return leftBound;
    }

    public void setLeftBound(float leftBound) {
        this.leftBound = leftBound;
    }

    public float getRightBound() {
        return rightBound;
    }

    public void setRightBound(float rightBound) {
        this.rightBound = rightBound;
    }

    public void setPaintColor(int color) {
        getPaint().setColor(color);
    }
    
    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public static int getBrushLength() {
        return brushLength;
    }

    public static void setBrushLength(int brushLength) {
        Axis.brushLength = brushLength;
    }

    public static int getBrushWidth() {
        return brushWidth;
    }

    public static void setBrushWidth(int brushWidth) {
        Axis.brushWidth = brushWidth;
    }

    public static int getInterval() {
        return interval;
    }

    public static void setInterval(int interval) {
        Axis.interval = interval;
    }

    public static float getAmplify() {
        return amplify;
    }

    public static void setAmplify(float amplify) {
        Axis.amplify = amplify;
    }

    public static float getParentViewHeight() {
        return parentViewHeight;
    }

    public static void setParentViewHeight(float parentViewHeight) {
        Axis.parentViewHeight = parentViewHeight;
        updatePtsSize();
    }

    public static Paint getGraphPaint() {
        return graphPaint;
    }

    public static void setGraphPaint(Paint graphPaint) {
        Axis.graphPaint = graphPaint;
    }

}
