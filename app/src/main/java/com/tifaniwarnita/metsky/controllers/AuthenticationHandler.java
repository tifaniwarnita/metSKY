package com.tifaniwarnita.metsky.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.tifaniwarnita.metsky.views.auth.AuthActivity;
import com.tifaniwarnita.metsky.views.auth.CarouselFragment;
import com.tifaniwarnita.metsky.views.auth.LoginFragment;
import com.tifaniwarnita.metsky.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tifani on 2/18/2016.
 */
public class AuthenticationHandler {
    private static final String TAG = AuthenticationHandler.class.getSimpleName();
    private static AuthData authData = null;
    private static String message = "";
    private static Activity activity = null;

    public static boolean auth() {
        authData = FirebaseConfig.ref.getAuth();
        if (authData != null) {
            // user authenticated
            return true;
        } else {
            // no user authenticated
            return false;
        }
    }

    public static String getUId() {
        authData = FirebaseConfig.ref.getAuth();
        if (authData != null) {
            Log.d(TAG, "FirebaseConfig.ref.getAuth() is not null");
            return authData.getUid();
        } else {
            Log.d(TAG, "FirebaseConfig.ref.getAuth() return null");
            return null;
        }
    }

    public static boolean signUp(final AuthActivity authActivity,
                                 final ProgressDialog progressDialog,
                                 final String name,
                                 final String email,
                                 final String password) {

        FirebaseConfig.ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                // Creating new account completed successfully
                Log.i(TAG, "Successfully created user account with uid: " + stringObjectMap.get("uid"));
                AuthenticationHandler.addUser(stringObjectMap.get("uid").toString(), name, email);
                progressDialog.dismiss();
                FragmentManager fm = authActivity.getSupportFragmentManager();

                fm.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .replace(R.id.auth_fragment_container, LoginFragment.newInstance(email, password))
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // Something went wrong :(
                switch (firebaseError.getCode()) {
                    case FirebaseError.EMAIL_TAKEN:
                        // handle for email that has been taken
                        AuthenticationHandler.message = "Email sudah diambil";
                        break;
                    case FirebaseError.INVALID_EMAIL:
                        // handle an invalid email
                        AuthenticationHandler.message = "Email salah";
                        break;
                    default:
                        // handle other errors
                        AuthenticationHandler.message = "Terjadi kesalahan";
                        break;
                }
                progressDialog.dismiss();
                AuthenticationHandler.showAlert();
            }
        });

        if (message.equalsIgnoreCase("Sign up success")) {
            return true;
        } else {
            return false;
        }
    }

    private static void addUser(String id, String name, String email) {
        // Add new data in table user
        Map<String, String> newUser = new HashMap<String, String>();
        newUser.put("nama", name);
        newUser.put("email", email);
        Map<String, Map<String, String>> user = new HashMap<String, Map<String, String>>();

        FirebaseConfig.ref.child("users").child(id).setValue(newUser);
    }

    public static boolean login(final Context context,
                                final AuthActivity authActivity,
                                final ProgressDialog progressDialog,
                                final String email,
                                final String password) {
        FirebaseConfig.ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                AuthenticationHandler.authData = authData;
                AuthenticationHandler.message = "Login success";
                Log.d(AuthenticationHandler.class.getSimpleName(), "Login success");
                addUserInfoToSetting(context, authActivity, authData, progressDialog);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Something went wrong :(
                AuthenticationHandler.authData = null;
                switch (firebaseError.getCode()) {
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        // handle a non existing user
                        AuthenticationHandler.message = "Alamat email tidak terdaftar";
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        // handle an invalid password
                        AuthenticationHandler.message = "Kata sandi salah";
                        break;
                    default:
                        AuthenticationHandler.message = "Terjadi kesalahan";
                        break;
                }
                progressDialog.dismiss();
                AuthenticationHandler.showAlert();
            }
        });
        if (authData != null) {
            return true;
        } else {
            return false;
        }
    }

    public static void logout() {
        FirebaseConfig.ref.unauth();
        authData = null;
    }

    public static void setActivity(Activity a) {
        activity = a;
    }

    public static void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //TODO:
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void addUserInfoToSetting(final Context context,
                                            final AuthActivity authActivity,
                                            AuthData authData,
                                            final ProgressDialog progressDialog) {
        final String id = authData.getUid();
        Firebase userInfo = FirebaseConfig.ref.child("users").child(id).child("nama");
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(AuthenticationHandler.class.getSimpleName(), "Add user info to setting success");
                System.out.println(snapshot.getValue());
                String nama = snapshot.getValue().toString();
                MetSkyPreferences.setId(context, id);
                MetSkyPreferences.setNama(context, nama);
                progressDialog.dismiss();
                FragmentManager fm = authActivity.getSupportFragmentManager();
                authActivity.clearScreenStack();
                fm.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .replace(R.id.auth_fragment_container, new CarouselFragment())
                        .commit();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "The read failed: " + firebaseError.getMessage());
                MetSkyPreferences.setNama(context, "");
                progressDialog.dismiss();
            }
        });
    }
}
