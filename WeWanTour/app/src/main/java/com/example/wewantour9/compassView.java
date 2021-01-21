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

 public class compassView extends View implements SurfaceHolder.Callback{

     Paint paint;
     Paint d_paint;
     Paint number_paint;
     Paint number_paint2;
     Paint text_paint;
     Paint paint2;

     private static final Paint paint4 = new Paint(Paint.ANTI_ALIAS_FLAG);

     private Path path;

     private int h_view=0;
     private int w_view=0;


     private SurfaceHolder ourHolder;
     Bitmap compass;


     private static Matrix matrix = null;

     private int frameWidth = 120, frameHeight = 370;


     // A rectangle to define an area of the sprite sheet that represents 1 frame
     private Rect frameToDraw;

     // A rect that defines an area of the screen on which to draw
     private RectF whereToDraw;


     public Matrix getMatrix() {
         return matrix;
     }



     public compassView(Context context) {
         super(context);

         init();
     }

     public compassView(Context context, AttributeSet attrs) {
         super(context, attrs);
         init();
     }

     public compassView(Context context, AttributeSet attrs, int defStyle) {
         super(context, attrs, defStyle);
         init();
     }




     private void init() {

         matrix = new Matrix();


         path  = new Path();

         paint2 = new Paint();
         paint2.setColor(Color.BLUE);
         paint2.setStrokeWidth(6);
         paint2.setStyle(Paint.Style.STROKE);

         paint = new Paint();
         paint.setColor(Color.BLACK);
         paint.setStrokeWidth(15);
         paint.setStyle(Paint.Style.STROKE);

         d_paint = new Paint();
         d_paint.setColor(Color.BLACK);
         d_paint.setStrokeWidth(5);
         d_paint.setStyle(Paint.Style.STROKE);

         text_paint= new Paint();
         text_paint.setColor(Color.BLACK);
         text_paint.setTextSize(35);
         text_paint.setTypeface(Typeface.create("Arial",Typeface.BOLD));


         number_paint= new Paint();
         number_paint.setColor(Color.BLACK);
         number_paint.setTextSize(30);
         number_paint.setStyle(Paint.Style.FILL);
         number_paint.setTextAlign(Paint.Align.CENTER);


         number_paint2 = new Paint();
         number_paint2.setColor(Color.BLACK);
         number_paint2.setTextSize(30);
         number_paint2.setStyle(Paint.Style.FILL);
         number_paint2.setTextAlign(Paint.Align.RIGHT);
     }


     @Override
     protected void onSizeChanged(int w, int h, int oldw, int oldh) {
         super.onSizeChanged(w, h, oldw, oldh);
         h_view=h;
         w_view=w;

     }




     private Canvas canvas_m;

     public void setPosMatrix(float yaw){
         matrix.setRotate((float) (-yaw*180/Math.PI), w_view/2f, h_view/2f);
         Log.d("STAMPO YAW:", ""+yaw);
     }

     @Override
     protected void onDraw(Canvas canvas) {
         super.onDraw(canvas);
             canvas_m=canvas;

         compass= BitmapFactory.decodeResource(getResources(), R.mipmap.compass);

         compass = Bitmap.createScaledBitmap(compass,
                 w_view,
                 h_view,
                 true);

         matrix.reset();

         matrix.setTranslate(0f,0f);
         matrix.postScale(0.9f,0.9f,w_view/2f,h_view/2f);



         canvas.drawBitmap(compass,matrix, paint4);


         invalidate();

     }

     @Override
     public void surfaceCreated(SurfaceHolder holder) {

     }

     @Override
     public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

     }

     @Override
     public void surfaceDestroyed(SurfaceHolder holder) {

     }
 }
