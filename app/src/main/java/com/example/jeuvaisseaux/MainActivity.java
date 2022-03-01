package com.example.jeuvaisseaux;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //déclaration imgview
        ImageView joystickImg = (ImageView) findViewById(R.id.objet_joystick);
        ImageView fondJoystickImg = (ImageView) findViewById(R.id.fond_joystick);
        ImageView tieImg = (ImageView) findViewById(R.id.tie);

        tieObject.setTieImage(tieImg);
        joystickObject.setJoystickImg(joystickImg);
        //on spécifie la position initiale du joystick
        //on récupère la taille de l'écran en pixels
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int largeurEcran = size.x;
        int hauteurEcran = size.y;
        String TailleEcran = "Taille Ecran = \n- x : " + largeurEcran + "\n- y : " + hauteurEcran;
        Log.i("Info",TailleEcran);

        float POSITION_fondJoystickImg_X = (float) (largeurEcran/3.15);   //on centre le fond du joystick au milieu
        float POSITION_fondJoystickImg_Y = (float) (hauteurEcran/1.47);  //on centre le fond joystick en bas
        float POSITION_INIT_OBJET_JOYSTICK_X = (float) (largeurEcran/2.6);   //on centre le joystick au milieu
        float POSITION_OBJET_JOYSTICK_Y = (float) (hauteurEcran/1.4);  //on centre le joystick en bas
        float DISTANCE_JOYSTICK_ACCEPTED = 250;
        float POSITION_TIE_X = (float)(largeurEcran/2.6);
        float POSITION_TIE_Y = (float) (hauteurEcran/2.0);

        //on place les différentes objets
        String debugMsg = "Position TIE : \n x = " + POSITION_TIE_X + "\n y = " + POSITION_TIE_Y;
        Log.i("MAJ",debugMsg);
        tieObject.setPositionX(POSITION_TIE_X);
        tieObject.setPositionY(POSITION_TIE_Y);
        joystickObject.setPositionX(POSITION_INIT_OBJET_JOYSTICK_X);
        joystickObject.setPositionY(POSITION_OBJET_JOYSTICK_Y);
        fondJoystickImg.setX(POSITION_fondJoystickImg_X);
        fondJoystickImg.setY(POSITION_fondJoystickImg_Y);

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
                    Boolean joystickIsPressed = false;

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
                        //float posJoyY = joystickObject.getPositionY() + 400;
                        //float posJoyX = joystickObject.getPositionX() + 160;
                        //float posJoyY = POSITION_OBJET_JOYSTICK_Y + 400;// + 495;
                        //float posJoyX = POSITION_OBJET_JOYSTICK_X + 160;// + 182;
                        double distanceJoystickDoigt = Math.sqrt(Math.pow(posY - posJoyY,2) + Math.pow(posX - posJoyX,2));
                        //String tmp = "\nXtouch = " + posX + "| Xjoystick = " + posJoyX;
                        //tmp = tmp + "\n" + "Ytouch = " + posY + " | Yjoystick = " + posJoyY;
                        //Log.i("INFO coord tmp", tmp);
                        //Log.i("INFO distance =", String.valueOf(distanceJoystickDoigt));

                        if (distanceJoystickDoigt <= DISTANCE_JOYSTICK_ACCEPTED) {
                                return true;
                        }else{
                            return false;
                        }
                    }

                    public void updateTiePos(MotionEvent motionEvent){
                        // en cours de dev
                        //if ((tieObject.getPositionX() > tieObject.getXmin()) && (tieObject.getPositionX() < tieObject.getXmax())){
                            //Log.i("debug update tie x", String.valueOf(tieObject.getPositionX() > tieObject.getXmin()));

                            //if ((tieObject.getPositionY() > tieObject.getYmin()) && (tieObject.getPositionY() < tieObject.getYmax())){
                                tieObject.setPositionX(tieObject.getPositionX() + (float)(getPosTouchX(motionEvent) - largeurEcran/2)/10);
                                tieObject.setPositionY(tieObject.getPositionY() + (float)(getPosTouchY(motionEvent) - hauteurEcran/1.08)/10);
                            //}
                        //}

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
                                if (canMoveJoystick(motionEvent) == true) {
                                    majJoystick(getPosTouchX(motionEvent), getPosTouchY(motionEvent));
                                    updateTiePos(motionEvent);
                                }
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
    private float positionX;
    private float positionY;
    private boolean isPressed;
    private float distanceMax;

    Joystick(){
        this.positionX = 0;
        this.positionY = 0;
        this.isPressed = false;
        distanceMax = 250;
    }
    public float getPositionX() {
        return positionX;
    }

    public float getDistanceMax(){
        return distanceMax;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        joystickImg.setX(positionX);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        joystickImg.setY(positionY);
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setIsPressed(boolean pressed) {
        isPressed = pressed;
    }

    public ImageView getJoystickImg() {
        return joystickImg;
    }

    public void setJoystickImg(ImageView joystickImg) {
        this.joystickImg = joystickImg;
    }

}
final class Tie{
    private ImageView tieImage;
    private float positionX;
    private float positionY;
    private float xmin;
    private float xmax;
    private float ymin;
    private float ymax;



    Tie(){
        this.positionX = 0;
        this.positionY = 0;
        this.xmin = 17;
        this.xmax = 1300;
        this.ymin = 16;
        this.ymax = 790;
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

    public ImageView getTieImage() {
        return tieImage;
    }

    public void setTieImage(ImageView tieImage) {
        this.tieImage = tieImage;
    }

}