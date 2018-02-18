package com.example.pramendra.spotnavigation;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    Context context;
    Animation blink,bounce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        try {
            setContentView(R.layout.activity_splash);
            if (!NetworkUtility.isInternetAvailable(context)) {
                View parentLayout = findViewById(R.id.spicon);
                Snackbar.make(parentLayout, "You Are Not Connected To Internet", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Connect", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    NetworkUtility.setMobileDataEnabled(context, true);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }


            blink = AnimationUtils.loadAnimation(context, R.anim.blink);
            ImageView appIcon = (ImageView) findViewById(R.id.spicon);
            TextView txtApp = (TextView) findViewById(R.id.txtLoad);
            txtApp.startAnimation(blink);
            appIcon.clearAnimation();
            TranslateAnimation transAnim = new TranslateAnimation(0, 0, 0,
                    getDisplayHeight() / 5);
            transAnim.setStartOffset(100);
            transAnim.setDuration(2000);
            transAnim.setFillAfter(true);
            transAnim.setInterpolator(new BounceInterpolator());
            transAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {

                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            appIcon.startAnimation(transAnim);
            int secondsDelayed = 2;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(Splash.this, GetNavigation.class));
                    finish();
                }
            }, secondsDelayed * 1000);
        }
        catch (Exception e){
            Log.e("Exception",e.toString());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        try {
            super.onPostCreate(savedInstanceState);

        }
        catch(Exception ex){
            Log.e("Exception",ex.toString());
        }

    }
    @Override
    protected  void onStop(){
        super.onStop();
        blink.cancel();
    }
    private int getDisplayHeight() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }

}
