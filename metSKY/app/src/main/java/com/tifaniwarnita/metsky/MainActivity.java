package com.tifaniwarnita.metsky;

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

public class MainActivity extends AppCompatActivity implements SignUpFragment.SignUpFragmentListener,
        LoginFragment.LoginFragmentListener, CarouselFragment.CarouselFragmentListener,
        EmotionFragment.EmotionFragmentListener, HomeFragment.HomeFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase library
        Firebase.setAndroidContext(this);
        FirebaseConfig.initialize();
        AuthenticationHandler.setActivity(this);

        setContentView(R.layout.activity_main);

        // Initialize the fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        fm.beginTransaction()
            .replace(R.id.fragment_container, new SignUpFragment())
            .commit();

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
        //TODO: nanti harus diganti

        FragmentManager fm = getSupportFragmentManager();

        /* fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit(); */

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, new HomeFragment())
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
    }

    //TODO: SEMUA JENIS ADD TO BACKSTACK

    @Override
    public void onLanjutButtonPressed() {
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, new EmotionFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onEmotionSelected(int emotion) {
        /*/* switch (emotion) {
            case EmotionFragment.EMOTION_TWINK:

                break;
            case EmotionFragment.EMOTION_SURPRISED:

                break;
            case EmotionFragment.EMOTION_HAPPY:

                break;
            case EmotionFragment.EMOTION_FLAT:

                break;
            case EmotionFragment.EMOTION_ANGRY:

                break;
            default:

                break;
        }*/
        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.fragment_container, new HomeFragment())
                .addToBackStack(null)
                .commit();
    }
}
