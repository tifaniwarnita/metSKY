package com.tifaniwarnita.metsky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.Firebase;
import com.tifaniwarnita.metsky.controllers.AuthenticationHandler;
import com.tifaniwarnita.metsky.controllers.FirebaseConfig;
import com.tifaniwarnita.metsky.controllers.MetSkySettings;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements SignUpFragment.SignUpFragmentListener,
        LoginFragment.LoginFragmentListener, CarouselFragment.CarouselFragmentListener,
        EmotionFragment.EmotionFragmentListener, HomeFragment.HomeFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Blue);
        super.onCreate(savedInstanceState);

        // Initialize Firebase library
        Firebase.setAndroidContext(this);
        FirebaseConfig.initialize();
        AuthenticationHandler.setActivity(this);

        setContentView(R.layout.activity_main);
        MetSkySettings.initialize(this);

        // Initialize the fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);


        // Check whether there's a user's been logged in or not
        if (!AuthenticationHandler.auth()) {
            int emotion = MetSkySettings.getEmotion();
            if (emotion != -1) {
                goToMainScreen(emotion);
            } else {
                goToEmotionScreen();
            }
        } else {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new SignUpFragment())
                    .commit();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSignUpSuccess(String email, String sandi) {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, LoginFragment.newInstance(email, sandi))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginButtonPressed() {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginSuccess() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, new CarouselFragment())
                .addToBackStack(null)
                .commit();
        clearScreenStack();
    }

    @Override
    public void onLanjutButtonPressed() {
        goToEmotionScreen();
    }

    private void goToEmotionScreen() {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, new EmotionFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onEmotionSelected(int emotion) {
        MetSkySettings.setEmotion(emotion);
        goToMainScreen(emotion);
    }

    private void goToMainScreen(int emotion) {
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

        Intent intent = new Intent(this, MainActivity.class);
        myActivity.setFlags(myActivity.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(myActivity);

        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, HomeFragment.newInstance(param))
                .addToBackStack(null)
                .commit();

        clearScreenStack();
    }

    private void clearScreenStack() {
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
