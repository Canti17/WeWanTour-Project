package com.example.wewantour9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class My_Canvas_View extends SurfaceView implements Runnable,SurfaceHolder.Callback  {
    private int shapeWidth = 100;
    private Thread gameThread;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private Canvas canvas;// New variables for the sprite sheet animation


    public Canvas getCanvas() {
        return canvas;
    }

    private long timeThisFrame;

    long fps;
    // Declare an object of type Bitmap
    Bitmap bitmapBob;

    // Bob starts off not moving
    boolean isMoving = false;

    // He can walk at 150 pixels per second
    float walkSpeedPerSecond = 250;

    // He starts 10 pixels from the left
    float bobXPosition =  20;
    float bobYPosition = 0;

    // These next two values can be anything you like
// As long as the ratio doesn't distort the sprite too much
    private int frameWidth = 120, frameHeight = 370;

    // How many frames are there on the sprite sheet?
    private int frameCount = 6;  //deve rimanere 8 perchè l'immagine è composta da 8 sprite

    // Start at the first frame - where else?
    private int currentFrame = 0;

    // What time was it when we last changed frames
    private long lastFrameChangeTime = 0;

    // How long should each frame last
    private int frameLengthInMilliseconds = 190;

    // A rectangle to define an area of the sprite sheet that represents 1 frame
    private Rect frameToDraw;

    // A rect that defines an area of the screen on which to draw
    private RectF whereToDraw;

    public RectF getWhereToDraw() {
        return whereToDraw;
    }

    Paint paint;


    private int window_w=0;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Load Bob from his .png file

        bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.walk_man_bitmap);


        // Scale the bitmap to the correct size
        // We need to do this because Android automatically
        // scales bitmaps based on screen density
        bitmapBob = Bitmap.createScaledBitmap(bitmapBob,
                w/2*frameCount,
                h/2,
                true);
        frameHeight=h/2;
        frameWidth=w/2;
        window_w=w;
        //bobXPosition=this.getWidth()/2;
    }


    public My_Canvas_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        ourHolder = getHolder();
        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        this.setZOrderOnTop(true); //necessary
        ourHolder.setFormat(PixelFormat.TRANSPARENT);
        ourHolder.addCallback(this);
        paint = new Paint();



        // Set our boolean to true - game on!
        //playing = true;
    }



    public void getCurrentFrame(){
        long time  = System.currentTimeMillis();
        if(isMoving) {// Only animate if bob is moving
            if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount-1) {

                    currentFrame = 0;
                }
            }
        }
        //update the left and right values of the source of
        //the next frame on the spritesheet
        frameToDraw.left = currentFrame * frameWidth;
        frameToDraw.right = frameToDraw.left + frameWidth;

    }


    public float k= 0;
    public float current_position=0;

    public void setK_to2(int val){
        k= (float) 8*val;
        current_position+=k;
    }
    public void setK_to0(){
        k=5;
        current_position+=k;
    }

    // Draw the newly updated scene
    public void draw() throws IOException {
        // Make sure our drawing surface is valid or we crash

        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            frameToDraw = new Rect(0, 0, frameWidth, frameHeight);
            whereToDraw = new RectF(bobXPosition,0, bobXPosition + frameWidth, frameHeight);

            Log.d("STAMPO K", ""+k);

            whereToDraw.set( current_position,frameHeight/2, (float) (current_position+frameWidth/1.5),frameHeight*2);

            getCurrentFrame();
            canvas.drawBitmap(bitmapBob, frameToDraw, whereToDraw, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
        //invalidate();


    }

    // If SimpleGameEngine Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    // If SimpleGameEngine Activity is started theb
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN :
                isMoving = !isMoving;
                break;
        }
        return true;
    }*/


    public void setMoving(boolean value){
        isMoving=value;
    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Draw the frame
            try {
                draw();
            } catch (IOException e) {
                Log.d("PORCO DI QUEL", "DIO");
                e.printStackTrace();
            }

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

        }

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
