package com.example.BluetoothRollersControl;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Елена on 24.03.2016.
 */
public class HSBColorPicker extends View implements View.OnTouchListener {

    public HSBColorPicker(Context context) {
        super(context);
        init();
    }

    public HSBColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HSBColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, 200);

        calculateSizes();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawColor(Color.BLUE);

        // Rectangle
        float leftx = 0;
        float topy = 3;
        float rightx = this.getWidth()-leftx;
        float bottomy = 53;


        Paint paint = new Paint();
        int[] colors = new int[] {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
        float[] positions = null;
        Shader s = new LinearGradient(leftx, topy, rightx, bottomy,colors,positions,Shader.TileMode.MIRROR);
        paint.setShader(s);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10);
        Paint line = new Paint();
        line.setColor(Color.WHITE);
        line.setStrokeWidth(5);
        canvas.drawRect(leftx, topy, rightx, bottomy, paint);
        canvas.drawLine(leftx, topy-3, leftx, bottomy+3, line);
    }

    private void calculateSizes() {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                System.out.println("start: " + motionEvent.getX() + " - y " + motionEvent.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                System.out.println("move: " + motionEvent.getX() + " - y " + motionEvent.getY());
                break;

            case MotionEvent.ACTION_UP:
                System.out.println("up: " + motionEvent.getX() + " - y " + motionEvent.getY());
                view.performClick();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
