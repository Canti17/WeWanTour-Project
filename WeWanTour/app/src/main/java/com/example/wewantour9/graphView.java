package com.example.wewantour9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class graphView extends View {

    Paint paint;
    Paint d_paint;
    Paint paint2;
    Path path;


    private int h_view=0;
    private int w_view=0;


    public graphView(Context context) {
        super(context);
        init();
    }

    public graphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public graphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);

        d_paint = new Paint();
        d_paint.setColor(Color.BLACK);
        d_paint.setStrokeWidth(12);
        d_paint.setStyle(Paint.Style.STROKE);

        paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        paint2.setStrokeWidth(15);
        paint2.setStyle(Paint.Style.STROKE);



    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        h_view=h;
        w_view=w;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path = new Path();
        path.moveTo(0,h_view);
        path.lineTo(400, 400);
        path.addCircle(400,400,10,Path.Direction.CW);
        path.lineTo(700, 400);
        path.addCircle(700,400,10,Path.Direction.CW);
        path.lineTo(900, 600);
        path.addCircle(900,600,10,Path.Direction.CW);

        Log.d("QUESTO é H_VIEWWWWWW_",h_view+"");
        Log.d("QUESTO é W_VIEWWWWWW_",w_view+"");
        canvas.drawLine(0, 0, 0, w_view, paint);
        canvas.drawLine(0, h_view, w_view,h_view, paint);
        canvas.drawPath(path, paint2);

    }
}
