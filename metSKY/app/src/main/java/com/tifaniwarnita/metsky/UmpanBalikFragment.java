package com.tifaniwarnita.metsky;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tifaniwarnita.metsky.controllers.FirebaseHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class UmpanBalikFragment extends Fragment {
    private MainActivityListener mainActivityListener;


    public UmpanBalikFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_umpan_balik, container, false);
        final EditText editTextNama = (EditText) v.findViewById(R.id.umpanbalik_edittext_nama);
        final EditText editTextEmail = (EditText) v.findViewById(R.id.umpanbalik_edittext_email);
        final EditText editTextFeedback = (EditText) v.findViewById(R.id.umpanbalik_edittext_umpanbalik);

        Button kirim = (Button) v.findViewById(R.id.umpanbalik_button_kirim);
        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = editTextNama.getText().toString();
                String email = editTextEmail.getText().toString();
                String feedBack = editTextFeedback.getText().toString();
                if (nama.length() == 0 || email.length() == 0 || feedBack.length() == 0) {
                    Toast.makeText(getContext(), "Mohon isi seluruh form dengan lengkap",
                            Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseHandler.sendUmpanBalik(getContext(), nama, email, feedBack);
                }
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
