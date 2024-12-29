package com.example.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private int width;
    private int height;

    private Bitmap imgDisk;
    private Bitmap imgPaddle;
    private Bitmap imgGateUp;
    private Bitmap imgGateDown;
    private Paint bgPaint;
    private Disk disk;
    private Paddle up;
    private Paddle down;

    private Disk[] disks;

    private Gate gateUp;
    private Gate gateDown;

    private SurfaceHolder holder;// #1
    private Canvas canvas;

    private Thread thread;

    private int interval = 50;
    private boolean isRunning=true;

    private Handler scoreHandler;

    public GameView(Context context, int width, int height, Handler scoreHandler) {//Handler os
        super(context);
        this.scoreHandler=scoreHandler;
        this.width = width;
        this.height = height;
        holder = getHolder();// #2
        // load image
        imgDisk = BitmapFactory.decodeResource(getResources(), R.drawable.disk);
        imgDisk = Bitmap.createScaledBitmap(imgDisk, 100, 100, false);

        imgPaddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        imgPaddle = Bitmap.createScaledBitmap(imgPaddle, 200, 100, false);

        imgGateUp = BitmapFactory.decodeResource(getResources(), R.drawable.gate);
        imgGateUp = Bitmap.createScaledBitmap(imgGateUp, 300, 100, false);

        imgGateDown = BitmapFactory.decodeResource(getResources(), R.drawable.gate);
        imgGateDown = Bitmap.createScaledBitmap(imgGateUp, 300, 100, false);
        imgGateDown = RotateBitmap(imgGateDown,180);



        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);

        disk = new Disk(0, height / 2, imgDisk, width, height);

        up = new Paddle(width / 2 - imgPaddle.getWidth() / 2, 100, imgPaddle, width, height);
        down = new Paddle(width / 2 - imgPaddle.getWidth() / 2, height - 100 - imgPaddle.getHeight(), imgPaddle, width, height);

        /*disks=new Disk[5];
        for (int i=0; i<disks.length; i++)
            disks[i]=new Disk((int) (Math.random() * 100 ),(int) (Math.random() * 100 ), imgDisk,width,height);*/

        gateUp = new Gate(width / 2 - imgGateUp.getWidth() / 2, 0, imgGateUp, width, height);
        gateDown = new Gate(width / 2 - imgGateDown.getWidth() / 2, height - imgGateDown.getHeight(), imgGateDown, width, height);

        thread = new Thread(this);
        thread.start();// do run function
    }

    public void drawCanvas() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawPaint(bgPaint);//background
            disk.draw(canvas);
            up.draw(canvas);
            down.draw(canvas);
            gateUp.draw(canvas);
            gateDown.draw(canvas);
            /*for (int i=0; i<disks.length; i++)
                disks[i].draw(canvas);*/
            holder.unlockCanvasAndPost(canvas);
        }
    }


    @Override
    public void run() {

        while (isRunning) {
            disk.move();//1
           /* for (int i=0; i<disks.length; i++)
                disks[i].move();*/
            drawCanvas();//2
            logicTest();//3
            touchGate();
            try {
                Thread.sleep(interval);//4
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void logicTest() {
        if(disk.inRange(up.getRect())) {
            disk.initdY();
            disk.initdX();
        }
        else  if(disk.inRange(down.getRect())) {
            disk.initdY();
            disk.initdX();
        }
    }

    private  int scoreUp=0, scoreDown=0;
    private void touchGate()
    {
        if(disk.inRange(gateUp.getRect()))
        {
            scoreDown++;
            updateScore();
        }
        if(disk.inRange(gateDown.getRect()))
        {
            scoreUp++;
            updateScore();
        }
    }

private  void updateScore()
{
    Message m=scoreHandler.obtainMessage();
    m.arg1=scoreDown;
    m.arg2=scoreUp;
    //m.getData().putInt("up",scoreUp);// another way m.args1
    //m.getData().putInt("down",scoreDown);// another way m.args1
    scoreHandler.sendMessage(m);
    disk.initdY();
    disk.initdX();
}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int count=event.getPointerCount();//for 2 or more players for more then one touch, count=how touch was
        for (int i=0;i<count;i++)
        {

        if (event.getAction() == MotionEvent.ACTION_MOVE)//grira
        {
            int x = (int) event.getX(i);// place were pressed
            int y = (int) event.getY(i);// place were pressed

            if (x >= (0 + imgPaddle.getWidth() / 2) && x <= (width - imgPaddle.getWidth() / 2)) { // limits in x
                if (y >= (0 + imgPaddle.getHeight() / 2) && y <= height / 3) { //limits for up paddle in 1/3 screen of framelayout
                    if (up.inRange(x, y)) {
                        up.setX(x);
                        up.setY(y);
                    }
                }

                if (y >= height * 2 / 3 && y <= (height - imgPaddle.getHeight() / 2)) {//limits for dauwn paddle in 2/3 screen of framelayout
                    if (down.inRange(x, y)) {
                        down.setX(x);
                        down.setY(y);
                    }
                }
            }
        }
        }
        return true;

    }

    public boolean changeState()
    {
        isRunning=!isRunning;
        if(isRunning){
            thread=new Thread(this);
            thread.start();

        }
        return isRunning;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
