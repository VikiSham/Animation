package com.example.animation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Animation1 extends AppCompatActivity implements Runnable {

    private FrameLayout frameLayout;
    private GameView gameView;
    private TextView tvlifeUp, tvlifeDown;
    private Button btnPause;
    private TextView tvTimer;
    private int counter=0;
    private Thread thread;// to do timer , sub-thread, 1 thread=onCreate



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation1);
        frameLayout=findViewById(R.id.frameLayout);//step1
        Log.d("TAG", "onCreate: "+ frameLayout.getWidth());// 0 - lo oved be onCreate
        Log.d("TAG", "onCreate: "+ frameLayout.getHeight());// 0
        tvTimer=findViewById(R.id.tvTimer);
        thread=new Thread(this);
        thread.start();
    }

    // haim activity siem liton halon
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        {
            Log.d("TAG", "onCreate: "+ frameLayout.getWidth());//1080
            Log.d("TAG", "onCreate: "+ frameLayout.getHeight());//1868

            gameView=new GameView(this,frameLayout.getWidth(),frameLayout.getHeight(),scorehandler);//step2
            frameLayout.addView(gameView);// step3 - conect framelayout to canvas
            tvlifeUp=findViewById(R.id.tvLifeUp);
            tvlifeDown=findViewById(R.id.tvLifeDown);
            btnPause=findViewById(R.id.btnPause);
        }
    }

    boolean flag=true;

    public void changeState(View view) {
        flag=gameView.changeState();

        if(flag)
        {
            btnPause.setBackgroundResource(R.drawable.pause);
            thread=new Thread(this);
            thread.start();
        }
        else
            btnPause.setBackgroundResource(R.drawable.play);

    }

    // timer in thread
    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
          //  tvTimer.setText(""+counter);
         /*  if(counter <10)
            tvTimer.setText("00:0"+counter);
           else
           if(counter >10 && counter<60)
               tvTimer.setText("00:"+counter);
           else
               if(counter/60 <10 && counter%60 <10)
                tvTimer.setText("0"+counter/60+":0"+counter%60);
               else
               if(counter/60 < 10 && counter%60 >=10)
                   tvTimer.setText("0"+counter/60+":"+counter%60);
               else
               if(counter/60 > 10 && counter%60 <10)
                   tvTimer.setText(""+counter/60+":0"+counter%60);
               else
               if(counter/60 > 10 && counter%60 >=10)
                   tvTimer.setText(""+counter/60+":"+counter%60);
*/

            tvTimer.setText(String.format("%02d:%02d",counter/60,counter%60));

        }
    };

    // score
    private Handler scorehandler=new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {

        //TODO - update score
            int scoreDown=msg.arg1;
            int scoreUp=msg.arg2;

            //int scoreDown=msg.getData().getInt("down");// another wa
            //int scoreUp=msg.getData().getInt("up");// another wa

            tvlifeDown.setText(String.format("Down:%02d",scoreDown));
            tvlifeUp.setText(String.format("Up:%02d",scoreUp));
        }
    };

    @Override
    public void run() {
        while (flag)
        {
            try {
                Thread.sleep(1000);// 1sec
                counter++;
                handler.sendEmptyMessage(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}