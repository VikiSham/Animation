package com.example.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public abstract class Figure {
    protected int x;
    protected int y;
    protected Bitmap bitmap;
    protected static int width;//screen
    protected static int height;//screen
    protected static Random rand;
    protected Rect rect=new Rect();



    public Figure(int x, int y, Bitmap bitmap,int w,int h) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        width=w;
        height=h;
        rand=new Random();
    }


    public int getXRight() {
        return x+bitmap.getWidth();
    }

    public int getYDown() {
        return y+bitmap.getHeight();
    }

    public  void draw(Canvas canvas)
    {
        canvas.drawBitmap(bitmap,x,y,null);
    }

    public void move()
    {

    }

    public void changeSize()
    {
        int num=rand.nextInt(250)+50;
        bitmap=Bitmap.createScaledBitmap(bitmap,num,num,false);

    }

// intersect mouse with paddle
    public  boolean inRange(int xTouch, int yTouch)
    {
        rect.set(x,y,getXRight(),getYDown());
        return rect.contains(xTouch,yTouch);
    }

    // intersect disk with paddle
    public  boolean inRange(Rect other)
    {
        rect.set(x,y,getXRight(),getYDown());
        return rect.intersect(other);
    }

    public Rect getRect() {
        rect.set(x,y,getXRight(),getYDown());
        return rect;
    }
}
