package com.orca.orcagamepad.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.orca.orcagamepad.R;


public class GyroControl extends View {
    private final String LOG_TAG = getClass().getSimpleName();

    private Paint mWindmillPaint;
    private float mDegrees;
    private Bitmap mWheel;

    private Paint mCirclePaint;

    public GyroControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 1000;
        int desiredHeight = 1000;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    private void init() {

        mWindmillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWindmillPaint.setStyle(Paint.Style.FILL);
        mWindmillPaint.setColor(Color.BLACK);

        mWheel = BitmapFactory.decodeResource(getResources(), R.drawable.gamepadwheel);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw wheel
        int h = 0;
        int w = 0;
        canvas.drawBitmap(mWheel, rotate(mWheel, h, w), mWindmillPaint);
        //canvas.drawCircle(mWheel.getWidth()/2,mWheel.getHeight()/2, 120, mCirclePaint);
        invalidate();
    }

    public Matrix rotate(Bitmap bm, int x, int y){
        Matrix mtx = new Matrix();
        mtx.postRotate(mDegrees, bm.getWidth() / 2, bm.getHeight() / 2);
        mtx.postTranslate(x, y);  //The coordinates where we want to put our bitmap
        return mtx;
    }

    public void setDegrees(float degrees) {
        mDegrees = 0.25f * degrees;
    }
}
