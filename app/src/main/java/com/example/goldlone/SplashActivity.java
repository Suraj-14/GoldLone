package com.example.goldlone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Handler hd;
    RelativeLayout rel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();
        setContentView(R.layout.activity_splash);

        TextView name = (TextView) findViewById(R.id.name);
        TextView year = (TextView) findViewById(R.id.year);
        rel=(RelativeLayout)findViewById(R.id.rel) ;
        Animation slidup = AnimationUtils.loadAnimation(this,
                R.anim.move);
        name.startAnimation(slidup);
        year.startAnimation(slidup);
        setFadeIn();
        hd=new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,DashBoardActivity.class);
                startActivity(i);
                finish();
            }
        },2200);
    }
    private void setFadeIn() {
        AnimationSet set = new AnimationSet(true);
        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);
        set.addAnimation(fadeIn);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.2f);
        rel.setLayoutAnimation(controller);
    }
}