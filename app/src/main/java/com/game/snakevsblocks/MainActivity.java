package com.game.snakevsblocks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    RelativeLayout gameContainer,upperBox;
    TextView hint, balls;
    View initBall;
    GestureDetector gestureDetector;
    Display display;
    Point size;
    float screenWidth, screenHeight;
    LinearLayout linearLayout;
    View[] tail;
    int tailLength=5;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        tail=new View[200];

        handler=new Handler();
        gameContainer=(RelativeLayout)findViewById(R.id.game_container);
        linearLayout=(LinearLayout)findViewById(R.id.linear_layout);
        upperBox=(RelativeLayout)findViewById(R.id.upper_box);
        hint=(TextView)findViewById(R.id.hint);
        balls=(TextView)findViewById(R.id.balls);
        initBall=(View)findViewById(R.id.init_dot);

        display=getWindowManager().getDefaultDisplay();
        size=new Point();
        display.getSize(size);
        screenWidth=size.x;
        screenHeight=size.y;

        gestureDetector=new GestureDetector(MainActivity.this, MainActivity.this);


        for (int i=0; i<tailLength; i++) {
            View newBall = new View(MainActivity.this);
            newBall.setBackgroundResource(R.drawable.dot);
            newBall.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
            tail[i]=newBall;
            linearLayout.addView(tail[i]);
        }




        gameContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float bY=balls.getY();
                float ibY=initBall.getY();


                upperBox.setVisibility(View.INVISIBLE);
                hint.setVisibility(View.INVISIBLE);


                balls.setY(bY);
                initBall.setY(ibY);

                startGame();

                return false;
            }
        });


    }

    float ibcurrentX, bcurrentX;

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
//
        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
//        Toast.makeText(getApplicationContext(), "Down", Toast.LENGTH_SHORT).show();
        ibcurrentX=initBall.getX();
        bcurrentX=balls.getX();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        float ibdx= ibcurrentX-motionEvent.getX();
        float bdx= bcurrentX-motionEvent.getX();

//        if (Math.abs(motionEvent.getX()-initBall.getX())<50) {
        if (motionEvent1.getX()+ibdx>10 && motionEvent1.getX()+ibdx<screenWidth-100)
        {
            final float posX=motionEvent1.getX()+ibdx;

            final float startX=initBall.getX();

            initBall.setX(motionEvent1.getX()+ibdx);
            balls.setX(motionEvent1.getX()+bdx);

            final float endibdx=initBall.getX();

            final float deltaX=Math.abs(endibdx-startX);



            for (int i=0; i<tailLength; i++)
            {
                tail[i].setY(tail[i].getY() - deltaX/5);
            }




                handler.postDelayed(new Runnable() {
                    int i=0;
                    @Override
                    public void run() {
                        tail[i].setX(posX);
                        tail[i].setY(tail[i].getY() + deltaX/5);
//                        try {
////                            tail[i + 1].setY(tail[i + 1].getY() - 30);
//                        }
//                        catch (Exception e)
//                        {
//
//                        }
                        i++;

                        if (i<tailLength)
                        {
                            handler.postDelayed(this, 30);
                        }
                    }
                },30);

        }
//        else {
//
//        }


        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {



        return false;
    }


    public void startGame()
    {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                int energyProbability=new Random().nextInt(4);

                if (energyProbability==2)
                {
                    int noOfBalls=new Random().nextInt(3);

                    for (int i=0; i<noOfBalls; i++) {
                        int ballEnergy=new Random().nextInt(5)+1;
                        int energyX=new Random().nextInt((int)screenWidth-110)+10;
//                        int energyY=new Random().nextInt((int)screenHeight*3/4-200);

                        final View newEnergy = new View(MainActivity.this);
                        newEnergy.setBackgroundResource(R.drawable.dot);
                        newEnergy.setLayoutParams(new LinearLayout.LayoutParams(48, 48));
                        newEnergy.setY(0);
                        newEnergy.setX(energyX);
                        gameContainer.addView(newEnergy);
                        newEnergy.animate()
                                .translationY(screenHeight)
                                .setInterpolator(new AccelerateInterpolator())
//                                .setInterpolator(new BounceInterpolator())
                                .setDuration(2000);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gameContainer.removeView(newEnergy);
                            }
                        }, 1900);

                    }

                }

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 10);


    }
}
