package com.tifaniwarnita.metsky.views.profil;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tifaniwarnita.metsky.R;
import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;

public class MetSKYCreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MetSkyPreferences.setMetSkyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_met_skycredits);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}
