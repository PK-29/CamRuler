package com.example.prince.camruler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;

/**
 * Created by princ on 2017-11-26.
 */

public class SurfaceImage extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap icon;
    private Paint paint;
    String imageFile;

    public SurfaceImage(Context context, File image) {
        super(context);
        getHolder().addCallback(this);
        imageFile = image.getAbsolutePath();
        icon = BitmapFactory.decodeFile(imageFile);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int newWidth = canvas.getHeight() * icon.getWidth() / icon.getHeight();
        icon = Bitmap.createScaledBitmap(icon, newWidth+250, canvas.getHeight()+100, false);

        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(imageFile, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        //icon = BitmapFactory.decodeFile(imageFile, bmOptions);
//        canvas.drawBitmap(icon, photoW/targetW, photoH/targetH,paint);

        int cx = (canvas.getWidth() - icon.getWidth()) / 2;
        int cy = (canvas.getHeight() - icon.getHeight()) / 2;
        canvas.drawBitmap(icon, cx, cy, paint);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @SuppressLint("WrongCall")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas(null);
            synchronized (holder) {
                onDraw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
}
