package com.tifaniwarnita.metsky.views.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;
import com.tifaniwarnita.metsky.AmbilFotoFragment;
import com.tifaniwarnita.metsky.BagikanActivity;
import com.tifaniwarnita.metsky.CarouselActivity;
import com.tifaniwarnita.metsky.HomeFragment;
import com.tifaniwarnita.metsky.KeadaanCuacaFragment;
import com.tifaniwarnita.metsky.views.kenaliawan.KenaliAwanActivity;
import com.tifaniwarnita.metsky.R;
import com.tifaniwarnita.metsky.ReportMapActivity;
import com.tifaniwarnita.metsky.ShowNotification;
import com.tifaniwarnita.metsky.UmpanBalikActivity;
import com.tifaniwarnita.metsky.WeatherInformationService;
import com.tifaniwarnita.metsky.controllers.AuthenticationHandler;
import com.tifaniwarnita.metsky.controllers.CameraController;
import com.tifaniwarnita.metsky.controllers.FirebaseConfig;
import com.tifaniwarnita.metsky.controllers.FirebaseHandler;
import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;
import com.tifaniwarnita.metsky.models.Cuaca;
import com.tifaniwarnita.metsky.views.profil.MetSKYCreditsActivity;
import com.tifaniwarnita.metsky.views.profil.ProfilMetSKYActivity;
import com.tifaniwarnita.metsky.views.profil.ProfilWCPLActivity;
import com.tifaniwarnita.metsky.views.templates.ExpandableListAdapter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.HomeFragmentListener,
        CarouselCameraFragment.CarouselCameraFragmentListener,
        AmbilFotoFragment.AmbilFotoFragmentListener,
        KeadaanCuacaFragment.KeadaanCuacaFragmentListener,
        LocationListener,
        MainActivityListener {

    private Cuaca cuaca;
    private HomeFragment homeFragment;
    private static MainActivity currentActivity = null;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    List<List<String>> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MetSkyPreferences.setMetSkyTheme(this);
        super.onCreate(savedInstanceState);

        // Initialize Firebase library
        Firebase.setAndroidContext(this);
        FirebaseConfig.initialize();
        AuthenticationHandler.setActivity(this);
        MetSkyPreferences.initialize(this);

        setContentView(R.layout.activity_main);
        currentActivity = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(getResources().getString(R.string.twitter_consumer_key),
                getResources().getString(R.string.twitter_consumer_secret));
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());

        MetSkyPreferences.initialize(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Notification service
        Intent notificationIntent = new Intent(MainActivity.this, ShowNotification.class);
        PendingIntent contentIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(contentIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + AlarmManager.INTERVAL_DAY * 2, AlarmManager.INTERVAL_DAY * 2, contentIntent);

        // Edit header
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        TextView nama = (TextView) header.findViewById(R.id.nav_header_nama);
        String namaUser = MetSkyPreferences.getNama(getApplicationContext());
        if (namaUser != null) {
            String[] splited = namaUser.split("\\s+");
            nama.setText("Hi, " + splited[0] + "!");
        }

        // Initialize the fragment
        String emotionParam = getIntent().getStringExtra("param");
        if (emotionParam != null) {
            FragmentManager fm = getSupportFragmentManager();
            homeFragment = HomeFragment.newInstance(emotionParam);
            fm.beginTransaction()
                    .replace(R.id.main_fragment_container, homeFragment)
            .commit();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            homeFragment = new HomeFragment();
            fm.beginTransaction()
                    .replace(R.id.main_fragment_container, homeFragment)
                    .commit();
        }

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setGroupIndicator(null);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                FragmentManager fm;
                Intent intent;
                switch (groupPosition) {
                    case 0:     // WCPL Weather Report Map
                        drawer.closeDrawer(Gravity.LEFT);
                        intent = new Intent(MainActivity.this, ReportMapActivity.class);
                        startActivity(intent);
                        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case 1:     // Laporkan
                        break;
                    /*case 2:     // Ubah Lokasi
                        drawer.closeDrawer(Gravity.LEFT);
                        goToMenuUbahLokasi();
                        fm = getSupportFragmentManager();
                        fm.beginTransaction()
                                .replace(R.id.main_fragment_container, new LokasiFragment())
                                .addToBackStack(null)
                                .commit();
                        break;*/
                    case 2:     // Kenali Awan
                        break;
                    case 3:     // Tutorial Aplikasi
                        drawer.closeDrawer(Gravity.LEFT);
                        intent = new Intent(MainActivity.this, CarouselActivity.class);
                        startActivity(intent);
                        break;
                    case 4:     // Umpan Balik
                        drawer.closeDrawer(Gravity.LEFT);
                        intent = new Intent(MainActivity.this, UmpanBalikActivity.class);
                        startActivity(intent);
                        break;
                    case 5:     // Bagikan
                        drawer.closeDrawer(Gravity.LEFT);
                        intent = new Intent(MainActivity.this, BagikanActivity.class);
                        startActivity(intent);
                        break;
                    case 6:     // Profil
                        break;
                }
                return false;
            }
        });

        // Listview on child click listener
        final MainActivity mainActivity = this;
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                FragmentManager fm;
                switch (groupPosition) {
                    case 0:             // WCPL Weather Report Map
                        break;
                    case 1:
                        switch (childPosition) {
                            case 0:     // Laporkan - Foto Cuaca
                                CameraController.dispatchTakePictureIntent(mainActivity);
                                break;
                            case 1:     // Laporkan - Keadaan Cuaca
                                goToMenuKeadaanCuaca();
                                fm = getSupportFragmentManager();
                                fm.beginTransaction()
                                        .replace(R.id.main_fragment_container, new KeadaanCuacaFragment())
                                        .addToBackStack(null)
                                        .commit();
                                break;
                        }
                        break;
                    /*case 2:             // Ubah Lokasi
                        break;*/
                    case 2:
                        Intent intent = new Intent(MainActivity.this, KenaliAwanActivity.class);
                        switch (childPosition) {
                            case 0:     // Kenali Awan - Awan Rendah
                                intent.putExtra(KenaliAwanActivity.JENIS_AWAN, getResources().getString(R.string.awan_rendah));
                                break;
                            case 1:     // Kenali Awan - Awan Sedang
                                intent.putExtra(KenaliAwanActivity.JENIS_AWAN, getResources().getString(R.string.awan_sedang));
                                break;
                            case 2:     // Kenali Awan - Awan Tinggi
                                intent.putExtra(KenaliAwanActivity.JENIS_AWAN, getResources().getString(R.string.awan_tinggi));
                                break;
                        }
                        startActivity(intent);
                        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case 3:             // Tutorial Aplikasi
                        break;
                    case 4:             // Umpan Balik
                        break;
                    case 5:             // Bagikan
                        break;
                    case 6:
                        switch (childPosition) {
                            case 0:     // Profil - metSKY
                                intent = new Intent(MainActivity.this, ProfilMetSKYActivity.class);
                                startActivity(intent);
                                break;
                            case 1:     // Profil - WCPL
                                intent = new Intent(MainActivity.this, ProfilWCPLActivity.class);
                                startActivity(intent);
                                break;
                            case 2:     // Profil - Credits
                                intent = new Intent(MainActivity.this, MetSKYCreditsActivity.class);
                                startActivity(intent);
                                break;
                        }
                        break;
                }
                drawer.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        ArrayList<String> headerTitle = new ArrayList<>();
        ArrayList<String> icon = new ArrayList<>();
        headerTitle.add("WCPL Weather Report Map");
        headerTitle.add("Laporkan");
        // headerTitle.add("Ubah Lokasi");
        headerTitle.add("Kenali Awan");
        headerTitle.add("Tutorial Aplikasi");
        headerTitle.add("Umpan Balik");
        headerTitle.add("Bagikan");
        headerTitle.add("Profil");

        icon.add("icon_lokasi");
        icon.add("icon_laporkan");
        icon.add("icon_menu_kenali_awan");
        icon.add("icon_menu_tutorial_aplikasi");
        icon.add("icon_menu_umpan_balik");
        icon.add("icon_menu_bagikan");
        icon.add("icon_menu_profil");

        listDataHeader.add(headerTitle);
        listDataHeader.add(icon);

        // Adding child data
        List<String> laporkan = new ArrayList<String>();
        laporkan.add("Foto Cuaca");
        laporkan.add("Keadaan Cuaca");

        List<String> awan = new ArrayList<String>();
        awan.add("Awan Rendah");
        awan.add("Awan Sedang");
        awan.add("Awan Tinggi");

        List<String> profil = new ArrayList<String>();
        profil.add("metSKY");
        profil.add("WCPL");
        profil.add("Credits");

        listDataChild.put(listDataHeader.get(0).get(1), laporkan); // Header, Child data
        listDataChild.put(listDataHeader.get(0).get(2), awan);
        listDataChild.put(listDataHeader.get(0).get(6), profil);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentActivity = null;
    }

    public static void System_exit( int exitCode ) {
        if ( null != currentActivity ) {
            currentActivity.finish();
            currentActivity = null;
        }
        System.exit( exitCode );
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CameraController.REQUEST_TAKE_PHOTO) {
            Bitmap photo = CameraController.onCameraResult(this, requestCode, resultCode, data);
            if (photo != null) {
                goToMenuAmbilFoto();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.main_fragment_container, AmbilFotoFragment.newInstance(photo))
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onCameraButtonClicked() {
        CameraController.dispatchTakePictureIntent(this);
    }

    @Override
    public void setCuaca(Cuaca cuaca) {
        this.cuaca = cuaca;
    }

    @Override
    public void onSimpanButtonClicked(Bitmap photo) {
        CameraController.galleryAddPic(this, photo);
    }

    @Override
    public void onLaporkanButtonClicked(Bitmap photo) {
        ProgressDialog progressDialog = ProgressDialog.show(this, "", "Menunggu...");
        FirebaseHandler.laporkanFoto(getApplicationContext(), cuaca, photo, progressDialog);
    }

    @Override
    public void onKeadaanCuacaClicked(String pilihan) {
        FirebaseHandler.laporkanKeadaanCuaca(getApplicationContext(), cuaca, pilihan);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        try {
            Intent intent = new Intent(this, WeatherInformationService.class);
            WeatherInformationService.initialize(homeFragment, this);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void backToMainActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void goToMenuAmbilFoto() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ambil Foto");
        toggle.setDrawerIndicatorEnabled(false);
    }

    @Override
    public void goToMenuKeadaanCuaca() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keadaan Cuaca");
        toggle.setDrawerIndicatorEnabled(false);
    }
}
