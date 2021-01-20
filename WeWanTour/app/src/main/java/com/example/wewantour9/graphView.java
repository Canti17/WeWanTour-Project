 package com.example.wewantour9;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;

import androidx.annotation.Nullable;

public class graphView extends View {

    Paint paint;
    Paint d_paint;
    Paint number_paint;
    Paint number_paint2;
    Paint text_paint;
    Paint paint2;


    private Path path;

    private int h_view=0;
    private int w_view=0;

    private int distance=100;


    private Canvas canvas_aux;






    //------- ASSE X

    float delay;
    float durata_tour;
    float durata_tour_secondi;

    float tempo_intervallo_da_considerare;
    float numero_intervalli_x;
    float lunghezza_intervallo_x_canvas;

    //-------



    //-------- ASSE Y

    int lunghezza_intervallo_da_considerare; //metri
    float lunghezza_percorso; //metri
    float numero_intervalli;
    float lunghezza_intervallo_canvas;

    //---------




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

    public void setDurata_tour(float durata_tour) {
        this.durata_tour = durata_tour;
        delay=15*60;
        //durata_tour=20; //minuti
        durata_tour_secondi=this.durata_tour*60+delay;

        if(durata_tour_secondi<=1800){ //30 min
            tempo_intervallo_da_considerare= 300; // 5 min
        }else if(durata_tour_secondi<= 3600){ //1 h
            tempo_intervallo_da_considerare= 600; //10 min
        }else if(durata_tour_secondi<=7200){ // 2 h
            tempo_intervallo_da_considerare= 1200; //20 min
        }else{
            tempo_intervallo_da_considerare= 1800; //30 min
        }
    }

    public void setLunghezza_percorso(float lunghezza_percorso) {
        this.lunghezza_percorso = lunghezza_percorso;


        if(this.lunghezza_percorso<=100){
            lunghezza_intervallo_da_considerare=10; //metri
        }else if(this.lunghezza_percorso<=1000){
            lunghezza_intervallo_da_considerare=70; //metri
        }else if(this.lunghezza_percorso<=3000){
            lunghezza_intervallo_da_considerare=180; //metri
        }else{
            lunghezza_intervallo_da_considerare=350; //metri
        }
    }

    private void init() {
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

        lunghezza_intervallo_x_canvas= (w_view-(distance))/numero_intervalli_x;
        lunghezza_intervallo_canvas= (h_view-(distance))/numero_intervalli;

        path.moveTo(distance,h_view-distance);
    }


    public void addLine(int ripetition,float value){
        path.lineTo(distance+(lunghezza_intervallo_x_canvas/tempo_intervallo_da_considerare)*10*ripetition,(h_view-distance)-(value* (lunghezza_intervallo_canvas/lunghezza_intervallo_da_considerare)));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        canvas.drawPath(path,paint2);


        // ----------- ASSE X

        numero_intervalli_x= (float) (Math.floor(durata_tour_secondi/tempo_intervallo_da_considerare)+1);
        lunghezza_intervallo_x_canvas= (w_view-(distance))/numero_intervalli_x;

        //--------------------



        //-------------ASSE Y-----------------//

        numero_intervalli= (float) (Math.floor(lunghezza_percorso/lunghezza_intervallo_da_considerare)+1);
        lunghezza_intervallo_canvas= (h_view-(distance))/numero_intervalli;

        //--------------------


        for(float i=1;i<numero_intervalli;++i){
            //Log.d("STAMPO LA i",""+i);
            //Log.d("PORCACCIOOOOOOO", String.valueOf(h_view-(lunghezza_intervallo_canvas*i)-distance));
            canvas.drawLine(distance, h_view-(lunghezza_intervallo_canvas*i)-distance, (float) (distance*1.2), h_view-(lunghezza_intervallo_canvas*i)-distance,d_paint);
            canvas.drawLine(distance, h_view-(lunghezza_intervallo_canvas*i)-distance, (float)(distance-((distance*1.2)-distance)), h_view-(lunghezza_intervallo_canvas*i)-distance,d_paint);
            canvas.drawText(""+lunghezza_intervallo_da_considerare*(int)i, (float) (distance-distance/3), (float) ((float)h_view-(lunghezza_intervallo_canvas*i)-(distance/1.1)), number_paint2);
        }



        //-----------------------------------//


        //-------------ASSE X-----------------//


       // Log.d("LUNGHEZZA INTERVALLO X IN CANVAS",""+lunghezza_intervallo_x_canvas);

        canvas.drawText(""+0, (float) distance, (float)(h_view-distance+(distance*1.6-distance)), number_paint);
        canvas.drawText("min", (float) ((float) w_view-(distance/1.4)), (float)(h_view-distance+(distance*1.6-distance)), text_paint);
        canvas.drawText("m", (float) distance-60, (float)(35), text_paint);



        for(int i=1;i<numero_intervalli_x;i++){
            //Log.d("ASSE X____ STAMPO",""+(h_view-distance));
           // Log.d("ASSE X____ STAMPO distance*1.5",""+(distance*1.5));
            canvas.drawLine(lunghezza_intervallo_x_canvas*i+distance, h_view-distance, lunghezza_intervallo_x_canvas*i+distance, (float) (h_view-distance-(distance*1.2-distance)),d_paint);
            canvas.drawLine(lunghezza_intervallo_x_canvas*i+distance, h_view-distance, lunghezza_intervallo_x_canvas*i+distance, (float) (h_view-distance+(distance*1.2-distance)),d_paint);
            canvas.drawText(""+(int)(tempo_intervallo_da_considerare/60)*i, (float) (lunghezza_intervallo_x_canvas*i+distance), (float)(h_view-distance+(distance*1.6-distance)), number_paint);

        }


        //-----------------------------------//


        Log.d("QUESTO é H_VIEWWWWWW_",h_view-distance+"");
        Log.d("QUESTO é W_VIEWWWWWW_",w_view-distance+"");

        canvas.drawLine(distance, h_view-distance, w_view,h_view-distance, d_paint); //asse X
        canvas.drawLine(distance, h_view-distance, distance,0, d_paint); //asse Y

        canvas.drawLine(distance,0,distance-20,20,d_paint); //freccia sx asse y
        canvas.drawLine(distance,0,distance+20,20,d_paint); //freccia dx asse y

        canvas.drawLine(w_view,h_view-distance,w_view-20,h_view-distance-20,d_paint); //freccia sx asse x
        canvas.drawLine(w_view,h_view-distance,w_view-20,h_view-distance+20,d_paint); //freccia dx asse x



    }
}
