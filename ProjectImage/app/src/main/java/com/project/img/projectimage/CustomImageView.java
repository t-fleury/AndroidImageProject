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

    private Drawable drawable;
    private Bitmap bitmap;
    private int bmWidth,bmHeight, drawableWidth, drawableHeight;

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

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        drawable = getDrawable();

        bmWidth = bitmap.getWidth();
        bmHeight = bitmap.getHeight();
        drawableWidth = getWidth();
        drawableHeight = getHeight();

        // set maximum scroll amount (based on center of image)
        int maxX = (int)((bmWidth / 2) - (drawableWidth / 2));
        int maxY = (int)((bmHeight / 2) - (drawableHeight / 2));

        // set scroll limits
        maxLeft = (maxX * -1);
        maxRight = maxX;
        maxTop = (maxY * -1);
        maxBottom = maxY;

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

        canvas.save();

        //canvas.translate(scrollByX, scrollByY);

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

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
