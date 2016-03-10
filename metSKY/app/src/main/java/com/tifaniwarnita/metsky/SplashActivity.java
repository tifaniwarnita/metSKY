package com.tifaniwarnita.metsky;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private Intent myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView metSkyLogo = (ImageView) findViewById(R.id.logo_splash_metsky);
        final Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate); // TODO: Change from animation

        metSkyLogo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                myActivity = new Intent(SplashActivity.this, MainActivity.class);
                myActivity.setFlags(myActivity.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(myActivity);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myActivity==null) {
            myActivity = new Intent(SplashActivity.this, MainActivity.class);
            myActivity.setFlags(myActivity.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        }
        startActivity(myActivity);
    }
}
