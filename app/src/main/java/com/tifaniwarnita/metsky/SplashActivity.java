package com.tifaniwarnita.metsky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.tifaniwarnita.metsky.controllers.AuthenticationHandler;
import com.tifaniwarnita.metsky.controllers.FirebaseConfig;
import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;
import com.tifaniwarnita.metsky.views.auth.AuthActivity;
import com.tifaniwarnita.metsky.views.home.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

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

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Check whether there's a user's been logged in or not
                if (AuthenticationHandler.getUId() == null) { // no user
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("type", AuthActivity.SIGN_UP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    int emotion = MetSkyPreferences.getEmotion(getApplicationContext());
                    if (emotion == -1) { //no emotion
                        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("type", AuthActivity.EMOTION);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
