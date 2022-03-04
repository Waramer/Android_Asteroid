package com.example.jeuvaisseaux;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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



public class MainGame extends AppCompatActivity {

    //on définie les objets
    Tie tieObject = new Tie();
    Joystick joystickObject = new Joystick();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //déclaration des images view
        ImageView joystickImg = (ImageView) findViewById(R.id.objet_joystick);
        ImageView fondJoystickImg = (ImageView) findViewById(R.id.fond_joystick);
        ImageView tieImg = (ImageView) findViewById(R.id.tie);


        //récupèration de la taille de l'écran en pixels
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
        tieObject.setYmax((float) (hauteurEcran/1.68));
        tieObject.setXmax((float) (largeurEcran - 270));

        //le joystick
        float POSITION_fondJoystickImg_X = (float) (largeurEcran/3.15);   //on centre le fond du joystick au milieu
        float POSITION_fondJoystickImg_Y = (float) (hauteurEcran/1.47);  //on centre le fond joystick en bas
        float POSITION_INIT_OBJET_JOYSTICK_X = (float) (largeurEcran/2.6);   //on centre le joystick au milieu
        float POSITION_OBJET_JOYSTICK_Y = (float) (hauteurEcran/1.4);  //on centre le joystick en bas

        joystickObject.setJoystickImg(joystickImg);
        joystickObject.setPositionX(POSITION_INIT_OBJET_JOYSTICK_X);
        joystickObject.setPositionY(POSITION_OBJET_JOYSTICK_Y);
        fondJoystickImg.setX(POSITION_fondJoystickImg_X);
        fondJoystickImg.setY(POSITION_fondJoystickImg_Y);

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

        //on initialise le thread de la MAJ du vaisseau
        Handler handlerForMovingTie = new Handler();
        Runnable movingTie = new Runnable() {
            @Override
            public void run() {
                if (joystickObject.getIsPressed()){
                    handlerForMovingTie.postDelayed(this, 10);
                }
            }
        };
        movingTie.run();

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

        joystickImg.setOnTouchListener(

                new OnTouchListener() {

                    public void setPosInit(){
                        joystickObject.setPositionX((float)(largeurEcran/2.6));
                        joystickObject.setPositionY((float)(hauteurEcran/1.4));
                    }

                    public void majJoystick(int x, int y){
                        String debugMsg = " " + "\n" +"MAJ Joystick :  x = " + x + " y = " + y;
                        debugMsg = debugMsg + "\nMAJ Vaisseau :  x = " + tieObject.getPositionX() + " y = " + tieObject.getPositionY();
                        Log.i("MAJ",debugMsg);

                        joystickObject.setPositionX(x - 100);
                        joystickObject.setPositionY(y - 400);
                    }

                    public int getPosTouchX(MotionEvent motionEvent){
                        return (int)motionEvent.getRawX();
                    }

                    public int getPosTouchY(MotionEvent motionEvent){
                        return (int)motionEvent.getRawY();
                    }

                    public boolean canMoveJoystick(MotionEvent motionEvent){
                        int posX = getPosTouchX(motionEvent);
                        int posY = getPosTouchY(motionEvent);
                        float posJoyX = 550;
                        float posJoyY = 1947;
                        double distanceJoystickDoigt = Math.sqrt(Math.pow(posY - posJoyY,2) + Math.pow(posX - posJoyX,2));
                        //Log.i("INFO distance =", String.valueOf(distanceJoystickDoigt));

                        return distanceJoystickDoigt <= joystickObject.getDistanceMax();
                    }

                    public void updateTiePos(MotionEvent motionEvent){
                        float nvelleValeurX = tieObject.getPositionX() + (float) (getPosTouchX(motionEvent) - largeurEcran / 2) / 10;
                        float nvelleValeurY = tieObject.getPositionY() + (float) (getPosTouchY(motionEvent) - hauteurEcran / 1.08) / 10;
                        boolean estDansIntervalleX = (nvelleValeurX > tieObject.getXmin()) && (nvelleValeurX < tieObject.getXmax());
                        boolean estDansIntervalleY = (nvelleValeurY > tieObject.getYmin()) && (nvelleValeurY < tieObject.getYmax());
                        if (estDansIntervalleX && estDansIntervalleY) {
                            Log.i("DEBUG", "Dans intervalle X et Y défini");
                            tieObject.setPositionX(nvelleValeurX);
                            tieObject.setPositionY(nvelleValeurY);
                        }else{
                            Log.i("DEBUG","Update apppelé mais hors intervalle X et Y");
                        }

                    }

                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                joystickObject.setIsPressed(true);
                                Log.i("CONTACT","premier contact du doigt a l'écran");
                                break;
                            case MotionEvent.ACTION_MOVE:
                                Log.i("DEPLACEMENT","déplacement du doigt");
                                if (canMoveJoystick(motionEvent)) {
                                    majJoystick(getPosTouchX(motionEvent), getPosTouchY(motionEvent));

                                }
                                updateTiePos(motionEvent);
                                break;
                            case MotionEvent.ACTION_UP:
                                joystickObject.setIsPressed(false);
                                Log.i("RETRAIT","retrait du doigt");
                                setPosInit();
                                break;
                            default:
                                return false;
                        }
                        return true;
                    }


                }


        );

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

