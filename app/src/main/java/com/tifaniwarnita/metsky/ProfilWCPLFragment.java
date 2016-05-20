package com.tifaniwarnita.metsky;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tifaniwarnita.metsky.views.home.MainActivityListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilWCPLFragment extends Fragment {

    private MainActivityListener mainActivityListener;

    public ProfilWCPLFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil_wcpl, container, false);
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
