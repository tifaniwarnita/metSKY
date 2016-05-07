package com.tifaniwarnita.metsky;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;


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
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://facebook.com/metskyproject"))
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#loveindoweather")
                        .build())
                .setQuote("met.SKY")
                .build();

        ShareButton facebookButton = (ShareButton) v.findViewById(R.id.bagikan_facebook);
        facebookButton.setShareContent(content);
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
