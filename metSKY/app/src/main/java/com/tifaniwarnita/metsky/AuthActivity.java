package com.tifaniwarnita.metsky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;
import com.tifaniwarnita.metsky.controllers.AuthenticationHandler;
import com.tifaniwarnita.metsky.controllers.FirebaseConfig;
import com.tifaniwarnita.metsky.controllers.MetSkySettings;

public class AuthActivity extends AppCompatActivity implements SignUpFragment.SignUpFragmentListener,
        LoginFragment.LoginFragmentListener, CarouselFragment.CarouselFragmentListener,
        EmotionFragment.EmotionFragmentListener {
    public static final String SIGN_UP = "sign up";
    public static final String EMOTION = "emotion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        AuthenticationHandler.setActivity(this);
        MetSkySettings.initialize(this);

        setContentView(R.layout.activity_auth);

        // Initialize the fragment
        FragmentManager fm = getSupportFragmentManager();

        String type = getIntent().getStringExtra("type");
        if (type.equals(AuthActivity.SIGN_UP)) {
            fm.beginTransaction()
                    .replace(R.id.auth_fragment_container, new SignUpFragment())
                    .commit();
        } else {
            assert(type.equals(AuthActivity.EMOTION));
            goToEmotionScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignUpSuccess(String email, String sandi) {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.auth_fragment_container, LoginFragment.newInstance(email, sandi))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginButtonPressed() {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.auth_fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginSuccess() {
        FragmentManager fm = getSupportFragmentManager();
        clearScreenStack();
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.auth_fragment_container, new CarouselFragment())
                .commit();
    }

    @Override
    public void onLanjutButtonPressed() {
        goToEmotionScreen();
    }

    private void goToEmotionScreen() {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.auth_fragment_container, new EmotionFragment())
                .commit();
    }

    @Override
    public void onEmotionSelected(int emotion) {
        MetSkySettings.setEmotion(emotion);
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
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        intent.putExtra("param", param);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(intent);
    }

    private void clearScreenStack() {
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
