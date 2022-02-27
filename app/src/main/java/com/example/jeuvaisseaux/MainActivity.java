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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView joystick = (ImageView) findViewById(R.id.objet_joystick);
        ImageView fond_joystick = (ImageView) findViewById(R.id.fond_joystick);
        //on spécifie la position initiale du joystick
        //on récupère la taille de l'écran en pixels
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int largeurEcran = size.x;
        int hauteurEcran = size.y;
        String TailleEcran = "Taille Ecran = \n- x : " + largeurEcran + "\n- y : " + hauteurEcran;
        Log.i("Info",TailleEcran);

        float POSITION_INIT_FOND_JOYSTICK_X = (float) (largeurEcran/3.15);   //on centre le fond du joystick au milieu
        float POSITION_INIT_FOND_JOYSTICK_Y = (float) (hauteurEcran/1.47);  //on centre le fond joystick en bas
        float POSITION_INIT_OBJET_JOYSTICK_X = (float) (largeurEcran/2.6);   //on centre le joystick au milieu
        float POSITION_INIT_OBJET_JOYSTICK_Y = (float) (hauteurEcran/1.4);  //on centre le joystick en bas
        //float DISTANCE_JOYSTICK_ACCEPTED = 100.0;
        joystick.setX(POSITION_INIT_OBJET_JOYSTICK_X);
        joystick.setY(POSITION_INIT_OBJET_JOYSTICK_Y );
        fond_joystick.setX(POSITION_INIT_FOND_JOYSTICK_X);
        fond_joystick.setY(POSITION_INIT_FOND_JOYSTICK_Y);
        joystick.setOnTouchListener(
                new OnTouchListener() {
                    Boolean joystickIsPressed = false;
                    public void majJoystick(int x, int y){
                        String debugMsg = "MAJ Joystick : \n x = " + x + "\n y = " + y;
                        Log.i("MAJ",debugMsg);
                        joystick.setX(x - 100);
                        joystick.setY(y - 400);
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
                        double distanceJoystickDoigt = Math.sqrt(Math.pow(posY - POSITION_INIT_OBJET_JOYSTICK_Y,2) + Math.pow(posX - POSITION_INIT_OBJET_JOYSTICK_X,2));

                        /*if (distanceJoystickDoigt <= DISTANCE_JOYSTICK_ACCEPTED) {
                                return true;
                        }else{
                            return false;
                        }*/
                        return true;
                    }
                    public void setPosInit(){
                        joystick.setX(POSITION_INIT_OBJET_JOYSTICK_X);
                        joystick.setY(POSITION_INIT_OBJET_JOYSTICK_Y );
                    }

                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                joystickIsPressed = true;
                                Log.i("CONTACT","premier contact du doigt a l'écran");
                                break;
                            case MotionEvent.ACTION_MOVE:
                                Log.i("DEPLACEMENT","déplacement du doigt");
                                if (canMoveJoystick(motionEvent) == true) {
                                    majJoystick(getPosTouchX(motionEvent), getPosTouchY(motionEvent));
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                joystickIsPressed = false;
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