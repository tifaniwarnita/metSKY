package com.tifaniwarnita.metsky.views.kenaliawan;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.tifaniwarnita.metsky.R;
import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;

public class KenaliAwanActivity extends AppCompatActivity {
    public static final String TAG = KenaliAwanActivity.class.getSimpleName();
    public static final String JENIS_AWAN = "jenis awan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MetSkyPreferences.setMetSkyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kenali_awan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString(JENIS_AWAN)!= null) {
            getSupportActionBar().setTitle(bundle.getString(JENIS_AWAN));
            if(bundle.getString(JENIS_AWAN).equals(getResources().getString(R.string.awan_rendah))) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.kenali_awan_fragment_container,
                                DaftarAwanFragment.newInstance( getResources().getString(R.string.awan_rendah)))
                        .commit();
            } else if(bundle.getString(JENIS_AWAN).equals(getResources().getString(R.string.awan_sedang))) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.kenali_awan_fragment_container,
                                DaftarAwanFragment.newInstance( getResources().getString(R.string.awan_sedang)))
                        .commit();
            } else if(bundle.getString(JENIS_AWAN).equals(getResources().getString(R.string.awan_tinggi))) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.kenali_awan_fragment_container,
                                DaftarAwanFragment.newInstance( getResources().getString(R.string.awan_tinggi)))
                        .commit();
            }
        } else {
            Log.d(TAG, "Extra string is null");
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStack();
                }
                else {
                    super.onBackPressed();
                }
                break;
        }
        return true;
    }

}
