package com.tifaniwarnita.metsky;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tifaniwarnita.metsky.controllers.CameraController;
import com.tifaniwarnita.metsky.controllers.MetSkySettings;
import com.tifaniwarnita.metsky.views.HomeCarouselCameraView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setMetSkyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MetSkySettings.initialize(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Initialize the fragment
        String emotionParam = getIntent().getStringExtra("param");
        if (emotionParam != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.main_fragment_container, HomeFragment.newInstance(emotionParam))
            .commit();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.main_fragment_container, new HomeFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_laporkan) {
            // Handle the camera action
        } else if (id == R.id.nav_ubah_lokasi) {

        } else if (id == R.id.nav_kenali_awan) {

        } else if (id == R.id.nav_umpan_balik) {

        } else if (id == R.id.nav_bagikan) {

        } else if (id == R.id.nav_profil) {
            Toast.makeText(getApplicationContext(), "Profil",
                    Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setMetSkyTheme() {
        int emotion = MetSkySettings.getEmotion();
        switch (emotion) {
            case EmotionFragment.EMOTION_TWINK:
                setTheme(R.style.AppTheme_Blue);
                break;
            case EmotionFragment.EMOTION_SURPRISED:
                setTheme(R.style.AppTheme_LightBlue);
                break;
            case EmotionFragment.EMOTION_HAPPY:
                setTheme(R.style.AppTheme_Orange);
                break;
            case EmotionFragment.EMOTION_FLAT:
                setTheme(R.style.AppTheme_Green);
                break;
            case EmotionFragment.EMOTION_ANGRY:
                setTheme(R.style.AppTheme_Red);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraController.REQUEST_TAKE_PHOTO) {
            CameraController.onCameraResult(this, requestCode, resultCode, data);
        }
    }

    @Override
    public void onCameraButtonClicked() {
        CameraController.dispatchTakePictureIntent(this);
    }
}
