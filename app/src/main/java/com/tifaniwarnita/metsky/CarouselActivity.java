package com.tifaniwarnita.metsky;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;
import com.tifaniwarnita.metsky.views.auth.AuthActivity;
import com.tifaniwarnita.metsky.views.auth.CarouselFragment;
import com.tifaniwarnita.metsky.views.home.MainActivity;

public class CarouselActivity extends AppCompatActivity
        implements CarouselFragment.CarouselFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.carousel_fragment_container, new CarouselFragment())
                .commit();
    }


    @Override
    public void onLanjutButtonPressed() {
        int emotion = MetSkyPreferences.getEmotion(getApplicationContext());
        if (emotion == -1) { //no emotion
            Intent intent = new Intent(CarouselActivity.this, AuthActivity.class);
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
            Intent intent = new Intent(CarouselActivity.this, MainActivity.class);
            intent.putExtra("param", param);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            startActivity(intent);
        }
    }
}
