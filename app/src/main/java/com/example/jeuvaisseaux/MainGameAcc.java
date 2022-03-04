package com.example.jeuvaisseaux;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Chronometer;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MainGameAcc extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor accelerometer;

    //on définie les objets
    Tie tieObject = new Tie();
    Accelerometer AccObject = new Accelerometer();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_acc);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // on recupere l accelerometre a partir du SensorManager
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // on associe l ecouteur d’evenements au SensorManager
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //déclaration des images view
        ImageView tieImg = (ImageView) findViewById(R.id.tie);


        //récupèration de la taille de l'écran en pixels et variable globales
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int largeurEcran = size.x;
        int hauteurEcran = size.y;
        String TailleEcran = "Taille Ecran = \n- x : " + largeurEcran + "\n- y : " + hauteurEcran;
        Log.i("Info",TailleEcran);


        //définition des propriétés des différents objets
        //le tie
        float POSITION_TIE_X = (float)(largeurEcran/2.6);
        float POSITION_TIE_Y = (float) (hauteurEcran/2.0);

        tieObject.setTieImage(tieImg);
        tieObject.setPositionX(POSITION_TIE_X);
        tieObject.setPositionY(POSITION_TIE_Y);
        tieObject.setYmax((float) (hauteurEcran - 270));
        tieObject.setXmax((float) (largeurEcran - 270));


        //le chronomètre
        Chronometer simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        simpleChronometer.start();

        // les astéroides
        ImageView asteroid1 = (ImageView) findViewById(R.id.asteroid1);
        Path path1 = new Path();
        path1.moveTo(-100,-1000);
        path1.lineTo((float)largeurEcran/2,(float)hauteurEcran+50);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(asteroid1, View.X,View.Y,path1);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setDuration(4000);
        anim1.setRepeatCount(Animation.INFINITE);
        anim1.start();

        ImageView asteroid2 = (ImageView) findViewById(R.id.asteroid2);
        Path path2 = new Path();
        path2.moveTo((float)largeurEcran,(float)1/3*hauteurEcran);
        path2.lineTo(-200,(float)hauteurEcran);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(asteroid2, View.X,View.Y,path2);
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setDuration(3000);
        anim2.setRepeatCount(Animation.INFINITE);
        anim2.start();

        ImageView asteroid3 = (ImageView) findViewById(R.id.asteroid3);
        Path path3 = new Path();
        path3.moveTo((float)-400,(float)hauteurEcran*1/4);
        path3.lineTo((float)largeurEcran+200, (float)hauteurEcran*4/5);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(asteroid3, View.X,View.Y,path3);
        anim3.setInterpolator(new LinearInterpolator());
        anim3.setDuration(6000);
        anim3.setRepeatCount(Animation.INFINITE);
        anim3.start();

        ImageView asteroid4 = (ImageView) findViewById(R.id.asteroid4);
        Path path4 = new Path();
        path4.moveTo((float)largeurEcran*1,(float)hauteurEcran*1);
        path4.lineTo((float)(largeurEcran*0.2),-400);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(asteroid4, View.X,View.Y,path4);
        anim4.setInterpolator(new LinearInterpolator());
        anim4.setDuration(5000);
        anim4.setRepeatCount(Animation.INFINITE);
        anim4.start();

        // les collisions
        ImageView explosion = (ImageView) findViewById(R.id.explosion);
        explosion.setX(-300f); // Image hors champ
        explosion.setY(-300f); // Image hors champ

        String debugMsg = "Position TIE : \n x = " + POSITION_TIE_X + "\n y = " + POSITION_TIE_Y;
        Log.i("MAJ",debugMsg);

        //on initialise le thread des collisions
        Handler handler = new Handler();

        Runnable r = new Runnable() {
            public void run() {
                if(enCollision(asteroid1,tieObject.getTieImage())){
                    explosion.setX(tieObject.getPositionX());
                    explosion.setY(tieObject.getPositionY());
                }
                else if (enCollision(asteroid2,tieObject.getTieImage())){
                    explosion.setX(tieObject.getPositionX());
                    explosion.setY(tieObject.getPositionY());
                }
                else if (enCollision(asteroid3, tieObject.getTieImage())){
                    explosion.setX(tieObject.getPositionX());
                    explosion.setY(tieObject.getPositionY())    ;
                }
                else if (enCollision(asteroid4, tieObject.getTieImage())){
                    explosion.setX(tieObject.getPositionX());
                    explosion.setY(tieObject.getPositionY());
                }
                else{
                    explosion.setX(-300f); // Image hors champ
                    explosion.setY(-300f); // Image hors champ

                }
                handler.postDelayed(this, 100);
            }
        };

        handler.postDelayed(r, 100);


    }

    public void initValues(float x, float y){
        AccObject.setDecalageX(x);
        AccObject.setDecalageY(y);
    }

    public void setPositionTie(){
        float nvelleValeurX = tieObject.getPositionX() - 10 * (AccObject.getxActuel() /*- (float)(0.36)*/);
        float nvelleValeurY = tieObject.getPositionY() + 15 * (AccObject.getyActuel() /*- (float)(0.15)*/);
        boolean estDansIntervalleX = (nvelleValeurX > tieObject.getXmin()) && (nvelleValeurX < tieObject.getXmax());
        boolean estDansIntervalleY = (nvelleValeurY > tieObject.getYmin()) && (nvelleValeurY < tieObject.getYmax());
        //Log.i("DEBUG"," " + "\n x = " + String.valueOf(nvelleValeurX) + "\n y = " + String.valueOf(nvelleValeurY));
        if (estDansIntervalleX && estDansIntervalleY) {
            Log.i("DEBUG", "Dans intervalle X et Y défini");
            tieObject.setPositionX(nvelleValeurX);
            tieObject.setPositionY(nvelleValeurY);
        }else{
            Log.i("DEBUG","Update apppelé mais hors intervalle X et Y");
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto−generated method stub
         }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto−generated method stub
        float gammaX = event.values[0], gammaY = event.values[1];
        if (AccObject.isInitMode()){
            AccObject.setDecalageX(gammaX - (float)(0.36));
            AccObject.setDecalageY(gammaY - (float)(0.145));
            AccObject.setInitMode(false);
        }
        AccObject.setxActuel(gammaX - AccObject.getDecalageX());
        AccObject.setyActuel(gammaY - AccObject.getDecalageY());
        Log.d("Valeurs accelerometre",AccObject.getxActuel() + "," + AccObject.getyActuel());
        setPositionTie();



    }

    boolean enCollision(ImageView img1, ImageView img2) {
        int[] firstPosition = new int[2];
        int [] secondPosition = new int[2];
        img1 .getLocationOnScreen(firstPosition);
        img2.getLocationOnScreen(secondPosition);
        Rect rectImg1 = new Rect(firstPosition [0], firstPosition [1], firstPosition [0] + img1.getMeasuredWidth(), firstPosition [1] + img1.getMeasuredHeight());
        Rect rectImg2= new Rect(secondPosition[0], secondPosition[1], secondPosition[0] + img2.getMeasuredWidth(), secondPosition[1] + img2.getMeasuredHeight());
        boolean intersect = rectImg1.intersect(rectImg2);
        if (intersect){
            float deltaLR = 50;
            if ((rectImg1.right > rectImg2.left + deltaLR)||(rectImg2.right > rectImg1.left + deltaLR)){
                float deltaUD = 50;
                if ((rectImg1.bottom > rectImg2.top + deltaUD)||(rectImg2.bottom > rectImg1.top + deltaUD)){
                    return true;
                }
            }
        }
        return false;
    }

}

