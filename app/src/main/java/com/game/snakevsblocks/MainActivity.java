package com.game.snakevsblocks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
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
    boolean gameStarted=false;

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

                if (!gameStarted)
                {
                    gameStarted=true;
                    startGame();
                }

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
                int energyProbability=new Random().nextInt(7);
                int wallProbability=new Random().nextInt(9);

                if (energyProbability==2 || energyProbability==4 || energyProbability==5)
                {
                    int noOfBalls=new Random().nextInt(3)+1;

                    for (int i=0; i<noOfBalls; i++) {

                        int energyX=new Random().nextInt((int)screenWidth-110)+10;
//                        int energyY=new Random().nextInt((int)screenHeight*3/4-200);

                        final LinearLayout newEnergyHolder = new LinearLayout(MainActivity.this);
                        final int energyAmount=new Random().nextInt(5)+1;
                        final View newEnergy=new View(MainActivity.this);
                        final TextView energyAmountHolder=new TextView(MainActivity.this);

                        newEnergyHolder.setBackgroundColor(Color.TRANSPARENT);
                        newEnergyHolder.setOrientation(LinearLayout.VERTICAL);

                        energyAmountHolder.setText(String.valueOf(energyAmount));
                        newEnergy.setBackgroundResource(R.drawable.dot);
                        energyAmountHolder.setTextColor(Color.WHITE);

                        newEnergyHolder.setGravity(Gravity.CENTER);
                        energyAmountHolder.setGravity(Gravity.CENTER);


                        newEnergy.setLayoutParams(new LinearLayout.LayoutParams(48, 48));

                        newEnergyHolder.addView(energyAmountHolder);
                        newEnergyHolder.addView(newEnergy);

                        newEnergyHolder.setY(-(int)screenWidth/6);
                        newEnergyHolder.setX(energyX);

                        gameContainer.addView(newEnergyHolder);
                        newEnergyHolder.animate()
                                .translationY(screenHeight)
                                .setInterpolator(new AccelerateInterpolator())
//                                .setInterpolator(new BounceInterpolator())
                                .setDuration(2500);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gameContainer.removeView(newEnergyHolder);
                            }
                        }, 2500);

                    }

                }
                else if (wallProbability==2 || wallProbability==3)
                {
                    final Button[] tiles=new Button[6];
                    for (int i=0; i<6; i++)
                    {
                        tiles[i] = new Button(MainActivity.this);
                        tiles[i].setBackgroundResource(R.drawable.blue_box);
                        tiles[i].setLayoutParams(new LinearLayout.LayoutParams((int)screenWidth/6, (int)screenWidth/6));
                        tiles[i].setTextColor(Color.WHITE);
                        tiles[i].setTextSize(25);
                        tiles[i].setText("5");
                        tiles[i].setY(-(int)screenWidth/6);
                        tiles[i].setX(i*screenWidth/6);
                        gameContainer.addView(tiles[i]);
                        tiles[i].animate()
                                .translationY(screenHeight)
                                .setInterpolator(new AccelerateInterpolator())
//                                .setInterpolator(new BounceInterpolator())
                                .setDuration(2500);

                        final int finalI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gameContainer.removeView(tiles[finalI]);
                            }
                        }, 2500);
                    }

                }

                else if (wallProbability==4 || wallProbability==6)
                {
                    final Button[] tiles=new Button[6];
                    for (int i=0; i<6; i++)
                    {
                        int hideTile=new Random().nextInt(3);
                        if (hideTile==1)
                        {
                            continue;
                        }
                        tiles[i] = new Button(MainActivity.this);
                        tiles[i].setBackgroundResource(R.drawable.blue_box);
                        tiles[i].setLayoutParams(new LinearLayout.LayoutParams((int)screenWidth/6, (int)screenWidth/6));
                        tiles[i].setTextColor(Color.WHITE);
                        tiles[i].setTextSize(25);
                        tiles[i].setText("5");
                        tiles[i].setY(-(int)screenWidth/6);
                        tiles[i].setX(i*screenWidth/6);
                        gameContainer.addView(tiles[i]);
                        tiles[i].animate()
                                .translationY(screenHeight)
                                .setInterpolator(new AccelerateInterpolator())
//                                .setInterpolator(new BounceInterpolator())
                                .setDuration(2500);

                        final int finalI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gameContainer.removeView(tiles[finalI]);
                            }
                        }, 2500);
                    }
                }

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 100);


    }
}
