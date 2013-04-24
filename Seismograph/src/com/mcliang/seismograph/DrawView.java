package com.mcliang.seismograph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
    boolean loaded = false;
    Axis x, y, z;

    public DrawView(Context context) {
        super(context);
    }
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
            super.onSizeChanged(xNew, yNew, xOld, yOld);
            float width = xNew;
            float height = yNew;
            float colWidth = width/3;
            x = new Axis(0, colWidth);
            y = new Axis(colWidth, colWidth*2);
            z = new Axis(colWidth*2, width);
            Axis.setAmplify(width/12);
            Axis.setParentViewHeight(height);
            setTheme();
            loaded = true;
    }
    private void setTheme() {
        x.setPaintColor(Color.rgb(113, 204, 117));
        y.setPaintColor(Color.rgb(204, 113, 126));
        z.setPaintColor(Color.rgb(113, 145, 204));
        Axis.getPenPaint().setColor(Color.rgb(177, 194, 179));
        Axis.getGraphPaint().setColor(Color.rgb(48, 41, 55));
        this.setBackgroundColor(Color.rgb(34, 29, 39));
    }
    
    public void updateView(float deltaX, float deltaY, float deltaZ) {
        if (loaded) {
            x.getPts().push(deltaX);
            y.getPts().push(deltaY);
            z.getPts().push(deltaZ);
            this.invalidate(); //calls onDraw
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        x.plotLine(canvas);
        y.plotLine(canvas);
        z.plotLine(canvas);
    }
}



















































