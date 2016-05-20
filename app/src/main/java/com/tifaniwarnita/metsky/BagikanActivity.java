package com.tifaniwarnita.metsky;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.tifaniwarnita.metsky.R;
import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

public class BagikanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MetSkyPreferences.setMetSkyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bagikan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ShareDialog shareDialog = new ShareDialog(this);

        final ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://facebook.com/metskyproject"))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#loveindoweather")
                        .build())
                .setQuote("met.SKY - Sekarang, semua orang dapat berbagi informasi cuaca dengan mudah, informatif, dan menyenangkan dengan sentuhan jari!")
                .build();

        Button facebookButton = (Button) findViewById(R.id.bagikan_facebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(content);
                }
            }
        });

        Button twitterButton = (Button) findViewById(R.id.bagikan_twitter);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri myImageUri = Uri.parse("android.resource://com.tifaniwarnita.metsky/" + R.drawable.metsky_share);
                Intent intent = new TweetComposer.Builder(getApplicationContext())
                        .text("met.SKY http://facebook.com/metskyproject #loveindoweather")
                        .image(myImageUri)
                        .createIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        Button googleButton = (Button) findViewById(R.id.bagikan_google);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the Google+ share dialog with attribution to your app.
                Intent shareIntent = new PlusShare.Builder(getApplicationContext())
                        .setType("text/plain")
                        .setText("met.SKY - Sekarang, semua orang dapat berbagi informasi cuaca dengan mudah, informatif, dan menyenangkan dengan sentuhan jari! #loveindoweather")
                        .setContentUrl(Uri.parse("http://facebook.com/metskyproject"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
            }
        });
    }

}
