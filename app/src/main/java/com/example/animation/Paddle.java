package com.example.animation;

import android.graphics.Bitmap;

public class Paddle extends Figure{
    public Paddle(int x, int y, Bitmap bitmap, int w, int h) {
        super(x, y, bitmap, w, h);
    }

    public void setX(int newX)
    {
        x=newX-bitmap.getWidth()/2; // siman negia baemza

        if(x<0) // bdikat hariga megvulot
            x=0;
        if(x+bitmap.getWidth()>width)
            x=width-bitmap.getWidth();
    }

    public void setY(int newY)
    {
        y=newY-bitmap.getHeight()/2;

        if(y<0)
            y=0;
        if(y+bitmap.getHeight()>height)
            y=height-bitmap.getHeight();

    }
}
