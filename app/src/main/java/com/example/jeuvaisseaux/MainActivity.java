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
import android.widget.ImageView;



public class MainActivity extends AppCompatActivity {

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

}

final class Joystick{
    private ImageView joystickImg;
    private boolean isPressed;
    private final float distanceMax;

    Joystick(){
        this.isPressed = false;
        this.distanceMax = 250;
    }

    public float getDistanceMax() {
        return distanceMax;
    }

    public void setPositionX(float positionX) {
        joystickImg.setX(positionX);
    }

    public void setPositionY(float positionY) {
        joystickImg.setY(positionY);
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsPressed(boolean pressed) {
        isPressed = pressed;
    }

    public void setJoystickImg(ImageView joystickImg) {
        this.joystickImg = joystickImg;
    }

}
final class Tie{
    private ImageView tieImage;
    private float positionX;
    private float positionY;
    private float xmin;  //sera utilisé par la suite
    private float xmax;
    private float ymin;
    private float ymax;



    Tie(){
        this.positionX = 0;
        this.positionY = 0;
        this.xmin = 0;
        this.xmax = 790;
        this.ymin = 0;
        this.ymax = 1280;
    }

    public float getXmin() {
        return xmin;
    }

    public void setXmin(float xmin) {
        this.xmin = xmin;
    }

    public float getXmax() {
        return xmax;
    }

    public void setXmax(float xmax) {
        this.xmax = xmax;
    }

    public float getYmin() {
        return ymin;
    }

    public void setYmin(float ymin) {
        this.ymin = ymin;
    }

    public float getYmax() {
        return ymax;
    }

    public void setYmax(float ymax) {
        this.ymax = ymax;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        tieImage.setX(positionX);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        tieImage.setY(positionY);
    }

    public void setTieImage(ImageView tieImage) {
        this.tieImage = tieImage;
    }

}
