 package com.example.wewantour9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

 public class compassView extends View{

     private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
     private int width = 0;
     private int height = 0;
     private Matrix matrix; // to manage rotation of the compass view
     private Bitmap bitmap;
     private float bearing; // rotation angle to North

     private int h_view=0;
     private int w_view=0;

     public compassView(Context context) {
         super(context);
         initialize();
     }

     public compassView(Context context, AttributeSet attr) {
         super(context, attr);
         initialize();
     }

     private void initialize() {
         matrix = new Matrix();
         // create bitmap for compass icon
         bitmap = BitmapFactory.decodeResource(getResources(),
                 R.drawable.compass1);
     }

     public void setBearing(float b) {
         bearing = b;
     }

     @Override
     protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         width = MeasureSpec.getSize(widthMeasureSpec);
         height = MeasureSpec.getSize(heightMeasureSpec);
         setMeasuredDimension(width, height);
     }

     @Override
     protected void onSizeChanged(int w, int h, int oldw, int oldh) {
         super.onSizeChanged(w, h, oldw, oldh);
         h_view=h;
         w_view=w;

     }


     @Override
     protected void onDraw(Canvas canvas) {


         int bitmapWidth = bitmap.getWidth();
         int bitmapHeight = bitmap.getHeight();
         int canvasWidth = canvas.getWidth();
         int canvasHeight = canvas.getHeight();

         bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.compass);


         bitmap = Bitmap.createScaledBitmap(bitmap,
                     (int) (h_view*0.65), (int) (h_view*0.65), true);


         // center
         int bitmapX = bitmap.getWidth() / 2;
         int bitmapY = bitmap.getHeight() / 2;
         int parentX = width /2;
         int parentY = height /3;
         int centerX = parentX - bitmapX;
         int centerY = parentY - bitmapY;

         // calculate rotation angle
         int rotation = (int) (360 - bearing);

         // reset matrix
         matrix.reset();
         matrix.setRotate(rotation, bitmapX, bitmapY);
         // center bitmap on canvas
         matrix.postTranslate(centerX, centerY);
         // draw bitmap
         canvas.drawBitmap(bitmap, matrix, paint);

         invalidate();

     }

 }
