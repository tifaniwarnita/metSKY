package com.tifaniwarnita.metsky.views.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tifaniwarnita.metsky.R;
import com.tifaniwarnita.metsky.controllers.AuthenticationHandler;


public class SignUpFragment extends Fragment {
    private EditText editTextNama;
    private EditText editTextEmail;
    private EditText editTextSandi;
    private Button buttonDaftar;
    private TextView buttonMasuk;

    private ProgressDialog progressDialog;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        editTextNama = (EditText) v.findViewById(R.id.signup_edittext_nama);
        editTextEmail = (EditText) v.findViewById(R.id.signup_edittext_email);
        editTextSandi = (EditText) v.findViewById(R.id.signup_edittext_sandi);
        buttonDaftar = (Button) v.findViewById(R.id.signup_button_daftar);
        buttonMasuk = (TextView) v.findViewById(R.id.signup_textview_masuk);

        createListener();
        return v;
    }

    private void createListener() {
        editTextNama.clearFocus();
        editTextEmail.clearFocus();
        editTextSandi.clearFocus();

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNama.getText().toString().length() == 0) {
                    // Nama has not been filled
                    Toast.makeText(getActivity(), "Silakan isi nama terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (editTextEmail.getText().toString().length() == 0) {
                    // Email has not been filled
                    Toast.makeText(getActivity(), "Silakan isi email terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (editTextSandi.getText().toString().length() == 0) {
                    // Sandi has not been filled
                    Toast.makeText(getActivity(), "Silakan isi sandi terlebih dahulu", Toast.LENGTH_LONG).show();
                } else if (editTextSandi.getText().toString().length() < 8) {
                    // Sandi characters is less than 8
                    Toast.makeText(getActivity(), "Sandi minimal berjumlah 8 huruf", Toast.LENGTH_LONG).show();
                } else {
                    // All fields have been filled, add new user
                    progressDialog = ProgressDialog.show(getActivity(), "", "Daftar akun baru...");
                    AuthenticationHandler.signUp(
                            (AuthActivity) getActivity(),
                            progressDialog,
                            editTextNama.getText().toString(),
                            editTextEmail.getText().toString(),
                            editTextSandi.getText().toString());
                }
            }
        });

        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // signUpFragmentListener.onLoginButtonPressed();
                FragmentManager fm = getActivity().getSupportFragmentManager();

                fm.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.auth_fragment_container, new LoginFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
