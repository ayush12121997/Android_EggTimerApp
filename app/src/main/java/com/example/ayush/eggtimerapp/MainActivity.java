package com.example.ayush.eggtimerapp;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    SeekBar seekBar;
    TextView timerTextView;
    int seconds;
    boolean timeOn;
    boolean allowReset;
    boolean reminderOn;
    CountDownTimer countDownTimer;
    MediaPlayer clockMediaPlayer;
    MediaPlayer timeUpMediaPlayer;

    public String getTime(int sec)
    {
        String ans = "";
        int count1 = sec / 60;
        int count2 = sec % 60;
        if (count1 < 10)
        {
            ans = ans + "0" + Integer.toString(count1) + ":";
        }
        else
        {
            ans = ans + Integer.toString(count1) + ":";
        }
        if (count2 < 10)
        {
            ans = ans + "0" + Integer.toString(count2);
        }
        else
        {
            ans = ans + Integer.toString(count2);
        }
        return ans;
    }

    public void startTimer(View view)
    {
        if (!timeOn)
        {
            if(seconds == 0)
            {
                Toast.makeText(this, "PLEASE SELECT A NON ZERO TIMER", Toast.LENGTH_LONG).show();
                return;
            }
            allowReset = false;
            timeOn = true;
            countDownTimer = new CountDownTimer(1000 * seconds, 1000)
            {
                public void onTick(long milliSecondsUntilDone)
                {
                    seconds = seconds - 1;
                    //Log.i("Seconds Left", Integer.toString(seconds));
                    timerTextView.setText(getTime(seconds));
                }

                public void onFinish()
                {
                    Toast.makeText(MainActivity.this, "PRESS RESET TO STOP", Toast.LENGTH_LONG).show();
                    timeOn = false;
                    allowReset = true;
                    reminderOn = true;
                    clockMediaPlayer.stop();
                    clockMediaPlayer.release();
                    timeUpMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.timeup);
                    timeUpMediaPlayer.setLooping(true);
                    timeUpMediaPlayer.start();
                }
            }.start();
            clockMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.clock);
            clockMediaPlayer.setLooping(true);
            clockMediaPlayer.start();
        }
        else
        {
            timeOn = false;
            countDownTimer.cancel();
            clockMediaPlayer.pause();
        }
    }

    public void resetTimer(View view)
    {
        if(timeOn)
        {
            clockMediaPlayer.stop();
            clockMediaPlayer.release();
        }
        if(reminderOn)
        {
            timeUpMediaPlayer.stop();
            timeUpMediaPlayer.release();
        }
        timeOn = false;
        allowReset = true;
        reminderOn = false;
        countDownTimer.cancel();
        timerTextView.setText(getTime(0));
        seconds = 0;
        seekBar.setProgress(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allowReset = true;
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        seekBar.setMax(240);
        seconds = seekBar.getProgress() * 15;
        timeOn = false;
        timerTextView.setText(getTime(seconds));
        reminderOn = false;
        countDownTimer = new CountDownTimer(10000, 100)
        {
            @Override
            public void onTick(long l)
            {

            }

            @Override
            public void onFinish()
            {

            }
        };
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                if(allowReset)
                {
                    seconds = i*15;
                    timerTextView.setText(getTime(seconds));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                if (!allowReset)
                {
                    Toast.makeText(MainActivity.this, "PRESS RESET AND THEN CHANGE TIMER", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if (allowReset)
                {
                    seconds = seekBar.getProgress() * 15;
                    timerTextView.setText(getTime(seconds));
                }
            }
        });
    }
}
