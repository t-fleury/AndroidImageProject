package com.project.img.projectimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Created by gauthier on 07/02/17.
 */

public class CustomImageView extends ImageView {

    private Bitmap bitmap;

    private int bmWidth, bmHeight, drawableWidth, drawableHeight;

    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 5f;

    // set scroll limits
    private int maxLeft, maxRight, maxTop, maxBottom;

    private float downX, downY;
    private float mLastTouchX, mLastTouchY;
    private int totalX, totalY;
    private int scrollByX, scrollByY;
    private static final int INVALID_POINTER_ID = -1;
    private static final String LOG_TAG = "TouchImageView";

    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;

    private float scaleFactor = 1.f;
    private float pivotPointX = 0f;
    private float pivotPointY = 0f;
    private ScaleGestureDetector detector;

    public CustomImageView(Context context, Bitmap bitmap) {
        super(context);
        init(bitmap);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public CustomImageView(Context context, AttributeSet attrs, Bitmap bitmap) {
        super(context, attrs);
        init(bitmap);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr, Bitmap bitmap) {
        super(context, attrs, defStyleAttr);
        init(bitmap);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    private void init(Bitmap bitmap){
        this.bitmap = bitmap;

        bmWidth = bitmap.getWidth();
        bmHeight = bitmap.getHeight();

        drawableWidth = this.getWidth();
        drawableHeight = this.getHeight();

    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        postInvalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                downX = ev.getX();
                downY = ev.getY();

                mLastTouchX = downX;
                mLastTouchY = downY;

                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float currentX = ev.getX(pointerIndex);
                final float currentY = ev.getY(pointerIndex);

                scrollByX = (int)(downX - currentX);
                scrollByY = (int)(downY - currentY);

                // scrolling to left side of image (pic moving to the right)
                if (currentX > downX)
                {
                    if (totalX == maxLeft)
                    {
                        scrollByX = 0;
                    }
                    if (totalX > maxLeft)
                    {
                        totalX = totalX + scrollByX;
                    }
                    if (totalX < maxLeft)
                    {
                        scrollByX = maxLeft - (totalX - scrollByX);
                        totalX = maxLeft;
                    }
                }

                // scrolling to right side of image (pic moving to the left)
                if (currentX < downX)
                {
                    if (totalX == maxRight)
                    {
                        scrollByX = 0;
                    }
                    if (totalX < maxRight)
                    {
                        totalX = totalX + scrollByX;
                    }
                    if (totalX > maxRight)
                    {
                        scrollByX = maxRight - (totalX - scrollByX);
                        //scrollByX = maxRight;
                        totalX = maxRight;
                    }
                }

                // scrolling to top of image (pic moving to the bottom)
                if (currentY > downY)
                {
                    if (totalY == maxTop)
                    {
                        scrollByY = 0;
                    }
                    if (totalY > maxTop)
                    {
                        totalY = totalY + scrollByY;
                    }
                    if (totalY < maxTop)
                    {
                        scrollByY = maxTop - (totalY - scrollByY);
                        totalY = maxTop;
                    }
                }

                // scrolling to bottom of image (pic moving to the top)
                if (currentY < downY)
                {
                    if (totalY == maxBottom)
                    {
                        scrollByY = 0;
                    }
                    if (totalY < maxBottom)
                    {
                        totalY = totalY + scrollByY;
                    }
                    if (totalY > maxBottom)
                    {
                        scrollByY = maxBottom - (totalY - scrollByY);
                        totalY = maxBottom;
                    }
                }

                scrollBy(scrollByX, scrollByY);
                downX = currentX;
                downY = currentY;

                mLastTouchX = totalX;
                mLastTouchY = totalY;
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawableWidth == 0 && drawableHeight == 0) {
            drawableWidth = this.getWidth();
            drawableHeight = this.getHeight();

            maxLeft = 0;
            maxRight = bmWidth - drawableWidth;
            maxTop = 0;
            maxBottom = bmHeight - drawableHeight;
        }

        canvas.save();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor, pivotPointX,
                pivotPointY);

        canvas.drawBitmap(bitmap, matrix,
                null);

        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d("scale detector", "detected");
            scaleFactor *= detector.getScaleFactor();
            pivotPointX = detector.getFocusX();
            pivotPointY = detector.getFocusY();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            invalidate();
            return true;
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
