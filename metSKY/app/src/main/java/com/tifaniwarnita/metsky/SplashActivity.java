package com.tifaniwarnita.metsky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.tifaniwarnita.metsky.controllers.AuthenticationHandler;
import com.tifaniwarnita.metsky.controllers.FirebaseConfig;
import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;

public class SplashActivity extends AppCompatActivity {
    private boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase library
        Firebase.setAndroidContext(this);
        FirebaseConfig.initialize();
        AuthenticationHandler.setActivity(this);
        MetSkyPreferences.initialize(this);

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
                done = true; //TODO EDIT SUPAYA GA KE DIA LAGI
                // Check whether there's a user's been logged in or not
                if (AuthenticationHandler.getUId() == null) { // no user
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("type", AuthActivity.SIGN_UP);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    startActivity(intent);
                } else {
                    int emotion = MetSkyPreferences.getEmotion(getApplicationContext());
                    if (emotion == -1) { //no emotion
                        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("type", AuthActivity.EMOTION);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(intent);
                    } else {
                        String param;
                        switch (emotion) {
                            case EmotionFragment.EMOTION_TWINK:
                                param = "twink";
                                break;
                            case EmotionFragment.EMOTION_SURPRISED:
                                param = "surprised";
                                break;
                            case EmotionFragment.EMOTION_HAPPY:
                                param = "happy";
                                break;
                            case EmotionFragment.EMOTION_FLAT:
                                param = "flat";
                                break;
                            case EmotionFragment.EMOTION_ANGRY:
                                param = "angry";
                                break;
                            default:
                                param = "";
                                break;
                        }
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("param", param);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onAnimationRepeat (Animation animation){

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
