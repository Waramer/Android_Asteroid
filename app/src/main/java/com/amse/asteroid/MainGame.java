package com.amse.asteroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;

public class MainGame extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Screen Size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        // ===== Start Game =====
        // Start Chrono
        Chronometer simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        simpleChronometer.start();

        // Game Canvas
        GameCanvas canvas = new GameCanvas(this);

        //Add to screen
        FrameLayout frame = (FrameLayout) findViewById(R.id.frameLayout);
        frame.addView(canvas);

    }
}

class GameCanvas extends View {
    Paint paint;
    int balleX, balleY;

    public GameCanvas(Context context){
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        balleX = 50;
        balleY = 50;
    }
    protected void onDraw (Canvas canvas){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        canvas.drawCircle(balleX,balleY,20,paint);
        balleX++;
        balleY++;
        invalidate();
    }
}
