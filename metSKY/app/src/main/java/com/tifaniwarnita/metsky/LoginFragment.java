package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tifaniwarnita.metsky.controllers.AuthenticationHandler;


public class LoginFragment extends Fragment {
    // The fragment initialization parameters
    private static final String ARG_EMAIL = "email";
    private static final String ARG_SANDI = "sandi";

    private String email = "";
    private String sandi = "";

    private EditText editTextEmail;
    private EditText editTextSandi;
    private Button buttonMasuk;

    private ProgressDialog progressDialog;
    private LoginFragmentListener loginFragmentListener;

    public interface LoginFragmentListener {
        public void onLoginSuccess();
    }

    public static LoginFragment newInstance(String email, String sandi) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_SANDI, sandi);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
            sandi = getArguments().getString(ARG_SANDI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_login, container, false);
        editTextEmail = (EditText) v.findViewById(R.id.login_edittext_email);
        editTextSandi = (EditText) v.findViewById(R.id.login_edittext_sandi);
        buttonMasuk = (Button) v.findViewById(R.id.login_button_masuk);

        editTextEmail.setText(email);
        editTextSandi.setText(sandi);

        createListener();
        return v;
    }

    private void createListener() {
        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextEmail.getText().toString().length() == 0) {
                    // Email has not been filled
                    Toast.makeText(getActivity(), "Silakan isi email terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (editTextSandi.getText().toString().length() == 0) {
                    // Sandi has not been filled
                    Toast.makeText(getActivity(), "Silakan isi sandi terlebih dahulu", Toast.LENGTH_LONG).show();
                } else {
                    // All fields have been filled, login
                    progressDialog = ProgressDialog.show(getActivity(), "", "Masuk...");
                    AuthenticationHandler.login(editTextEmail.getText().toString(),
                            editTextSandi.getText().toString(), progressDialog, loginFragmentListener);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Make sure that the container activity has implemented
        //the interface
        try {
            loginFragmentListener = (LoginFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginFragmentListener methods");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginFragmentListener = null;
    }

}

