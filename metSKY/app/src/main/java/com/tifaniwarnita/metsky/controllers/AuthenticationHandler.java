package com.tifaniwarnita.metsky.controllers;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tifani on 2/18/2016.
 */
public class AuthenticationHandler {
    private static AuthData authData = null;
    private static String message = "";

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

    public static boolean signUp(String email, String password) {
        final String userEmail = email;
        FirebaseConfig.ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                // Creating new account completed successfully
                Log.i("SIGN UP", "Successfully created user account with uid: " + stringObjectMap.get("uid"));

                // Add new data in table user
                Map<String, String> newUser = new HashMap<String, String>();
                newUser.put("email", authData.getProviderData().get("email").toString());
                FirebaseConfig.ref.child("users").child(authData.getUid()).setValue(newUser);
                AuthenticationHandler.message = "Sign up success";
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // Something went wrong :(
                switch (firebaseError.getCode()) {
                    case FirebaseError.EMAIL_TAKEN:
                        // handle for email that has been taken
                        AuthenticationHandler.message = "Email has been taken";
                        break;
                    case FirebaseError.INVALID_EMAIL:
                        // handle an invalid email
                        AuthenticationHandler.message = "Invalid email";
                        break;
                    default:
                        // handle other errors
                        AuthenticationHandler.message = "Other error";
                        break;
                }
            }
        });
        if (message.equalsIgnoreCase("Sign up success")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean login(String email, String password) {
        FirebaseConfig.ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                AuthenticationHandler.authData = authData;
                AuthenticationHandler.message = "Login success";
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // Something went wrong :(
                AuthenticationHandler.authData = null;
                switch (firebaseError.getCode()) {
                    case FirebaseError.USER_DOES_NOT_EXIST:
                        // handle a non existing user
                        AuthenticationHandler.message = "User does not exist";
                        break;
                    case FirebaseError.INVALID_PASSWORD:
                        // handle an invalid password
                        AuthenticationHandler.message = "Invalid password";
                        break;
                    default:
                        AuthenticationHandler.message = "Other error";
                        break;
                }
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

}
