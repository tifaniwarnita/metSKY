package com.tifaniwarnita.metsky;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tifaniwarnita.metsky.R;
import com.tifaniwarnita.metsky.controllers.FirebaseHandler;
import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;

public class UmpanBalikActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MetSkyPreferences.setMetSkyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umpan_balik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final EditText editTextNama = (EditText) findViewById(R.id.umpanbalik_edittext_nama);
        final EditText editTextEmail = (EditText) findViewById(R.id.umpanbalik_edittext_email);
        final EditText editTextFeedback = (EditText) findViewById(R.id.umpanbalik_edittext_umpanbalik);

        Button kirim = (Button) findViewById(R.id.umpanbalik_button_kirim);
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = editTextNama.getText().toString();
                String email = editTextEmail.getText().toString();
                String feedBack = editTextFeedback.getText().toString();
                if (nama.length() == 0 || email.length() == 0 || feedBack.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Mohon isi seluruh form dengan lengkap",
                            Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseHandler.sendUmpanBalik(getApplicationContext(), nama, email, feedBack);
                }
            }
        });
    }

}
