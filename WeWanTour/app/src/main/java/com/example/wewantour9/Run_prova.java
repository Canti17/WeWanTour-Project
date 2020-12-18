package com.example.wewantour9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Run_prova extends AppCompatActivity{

    private int progressStatus = 0;
    private Handler handler = new Handler();

    private  My_Canvas_View canvas_view;

    private boolean isMoving=false;
    private ObjectAnimator animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_run_prova);



        final Button btn = (Button) findViewById(R.id.btn);
        final TextView tv = (TextView) findViewById(R.id.tv);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);

        canvas_view=findViewById(R.id.canvas_view);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the progress status zero on each button click
                progressStatus = progressStatus+10;
                pb.setProgress(progressStatus);
                tv.setText(progressStatus+"%");




                if(!isMoving){
                    isMoving=true;
                    float end = canvas_view.getTranslationX() + 100f;
                    animation= ObjectAnimator.ofFloat(canvas_view, "translationX", end);
                    animation.setDuration(5000);
                    animation.start();
                }else{
                    isMoving=false;
                }


                canvas_view.setMoving(isMoving);

                /*
                    A Thread is a concurrent unit of execution. It has its own call stack for
                    methods being invoked, their arguments and local variables. Each application
                    has at least one thread running when it is started, the main thread,
                    in the main ThreadGroup. The runtime keeps its own threads
                    in the system thread group.
                */
                // Start the lengthy operation in a background thread

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        canvas_view.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        canvas_view.pause();
    }


}
