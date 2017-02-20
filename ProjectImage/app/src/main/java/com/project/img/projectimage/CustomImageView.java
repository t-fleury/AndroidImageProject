package com.project.img.projectimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Created by gauthier on 07/02/17.
 */

public class CustomImageView extends ImageView {

    //private Bitmap bitmap;

    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 5f;

    private float scaleFactor = 1.f;
    private float pivotPointX = 0f;
    private float pivotPointY = 0f;
    private ScaleGestureDetector detector;

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        //bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleFactor, scaleFactor, pivotPointX,
                pivotPointY);
        // canvas.setMatrix(matrix);

        canvas.drawBitmap(
                ((BitmapDrawable) this.getDrawable()).getBitmap(), matrix,
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
}
