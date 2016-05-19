package com.tifaniwarnita.metsky;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;


public class BagikanFragment extends Fragment {
    private MainActivityListener mainActivityListener;

    public BagikanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_bagikan, container, false);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        final ShareDialog shareDialog = new ShareDialog(getActivity());

        final ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://facebook.com/metskyproject"))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#loveindoweather")
                        .build())
                .setQuote("met.SKY - Sekarang, semua orang dapat berbagi informasi cuaca dengan mudah, informatif, dan menyenangkan dengan sentuhan jari!")
                .build();

        Button facebookButton = (Button) v.findViewById(R.id.bagikan_facebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareDialog.show(content);
                }
            }
        });

        Button twitterButton = (Button) v.findViewById(R.id.bagikan_twitter);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri myImageUri = Uri.parse("android.resource://com.tifaniwarnita.metsky/" + R.drawable.metsky_share);
                TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                        .text("met.SKY http://facebook.com/metskyproject #loveindoweather")
                        .image(myImageUri);
                builder.show();
            }
        });

        Button googleButton = (Button) v.findViewById(R.id.bagikan_google);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the Google+ share dialog with attribution to your app.
                Intent shareIntent = new PlusShare.Builder(getActivity())
                        .setType("text/plain")
                        .setText("met.SKY - Sekarang, semua orang dapat berbagi informasi cuaca dengan mudah, informatif, dan menyenangkan dengan sentuhan jari! #loveindoweather")
                        .setContentUrl(Uri.parse("http://facebook.com/metskyproject"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mainActivityListener = (MainActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivityListener.backToMainActivity();
        mainActivityListener = null;
    }

}
