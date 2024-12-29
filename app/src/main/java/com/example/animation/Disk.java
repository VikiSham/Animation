package com.example.animation;

import android.graphics.Bitmap;

public class Disk extends Figure {

    private int dx;//kezev hitkadmut
    private int dy;//kezev hitkadmut

    public Disk(int x, int y, Bitmap bitmap, int w, int h) {
        super(x, y, bitmap, w, h);
        dx=(int )(Math.random() * 50 + 10);
        dy=(int )(Math.random() * 50 + 10);
    }

    public void move()
    {
        x+=dx;
        y+=dy;
        if(x<0 && dx<0)//left limit
        {
            initdX();
        }
        if(getXRight()>width && dx>0)//right limit
        {
            initdX();
        }


        if(y<0 && dy<0)//up limit
        {
            initdY();
        }
        if(getYDown()>height && dy>0)//down limit
        {
            initdY();
        }


    }

    public void initdX()
    {
        int d;
        if(dx<0)
            d=1;
        else
            d=-1;
        dx=d*(10+rand.nextInt(20));
        //  changeSize();
    }

    public void initdY()
    {
        int d;
        if(dy<0)
            d=1;
        else
            d=-1;
        dy=d*(10+rand.nextInt(20));
        //  changeSize();

    }
}
