package com.example.honeycombmemorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int levelnumber = 1;
    int totalTaps = 2;
    List<Integer> l = new ArrayList<>();
    List<Integer> pattern = new ArrayList<>();
    boolean gameActive = false;
    int tapCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playMusic(R.raw.background_music);
        addItemstoList();
        TextView tv = findViewById(R.id.level_number);
        tv.setText("Level 1 \nTotal taps : 2");
        generatepattern();
    }
    public static MediaPlayer music;
    public void playMusic(int id)
    {
        music = MediaPlayer.create(MainActivity.this, id);
        music.setLooping(true);
        music.start();
    }

    public void addItemstoList()
    {
        for(int i=0;i<19;i++)
            pattern.add(i);
    }

    public void generatepattern()
    {
        Collections.shuffle(pattern);
        RelativeLayout relative_grid = findViewById(R.id.relative_grid);
        for(int i=0;i<totalTaps;i++)
        {
            ImageView img = (ImageView) relative_grid.getChildAt(pattern.get(i));
            Animation animate = AnimationUtils.loadAnimation(this, R.anim.blink);
            img.startAnimation(animate);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameActive = true;
                }
            }, 2000);
        }
    }

    public void tapIn(View v)
    {
        ImageView img = (ImageView) findViewById(v.getId());
        int tag = Integer.parseInt(img.getTag().toString());
        if(gameActive && !l.contains(tag))
        {
            l.add(tag);
            img.setImageAlpha(0);
            tapCounter++;
        }
        if(tapCounter == totalTaps)
        {
            gameActive = false;
            check();
        }
    }

    public void check()
    {
        Collections.sort(l);
        List<Integer> temp = new ArrayList<>();
        temp = pattern.subList(0,totalTaps);
        Collections.sort(temp);
        TextView result = findViewById(R.id.display_result);
        if(temp.equals(l))
        {
            result.setText("Perfect! Please advance to next level");
            Button nextlevel = findViewById(R.id.nextlevel_button);
            nextlevel.setVisibility(View.VISIBLE);
        }
        else
        {
            result.setText("Pattern did not match. Please try again.");
            Button retry = findViewById(R.id.retry_button);
            retry.setVisibility(View.VISIBLE);
        }
    }

    public void reset()
    {
        l.clear();
        tapCounter=0;
        RelativeLayout relative_grid = findViewById(R.id.relative_grid);
        for(int i=0;i<19;i++)
        {
            ImageView img =(ImageView) relative_grid.getChildAt(i);
            img.setImageAlpha(255);
        }
        TextView result = findViewById(R.id.display_result);
        result.setText("");
    }

    public void nextLevel(View v)
    {
        reset();
        Button nextlevel = findViewById(R.id.nextlevel_button);
        nextlevel.setVisibility(View.INVISIBLE);
        if(totalTaps != 10)
            totalTaps++;
        levelnumber++;
        TextView tv = findViewById(R.id.level_number);
        String level_text = "Level ";
        level_text+=Integer.toString(levelnumber);
        level_text+="\nTotal taps : "+Integer.toString(totalTaps);
        tv.setText(level_text);
        generatepattern();
    }

    public void retry(View v)
    {
        reset();
        Button retry = findViewById(R.id.retry_button);
        retry.setVisibility(View.INVISIBLE);
        generatepattern();
    }
}