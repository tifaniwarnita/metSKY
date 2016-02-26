package com.tifaniwarnita.metsky.controllers;

import com.firebase.client.Firebase;

/**
 * Created by Tifani on 2/18/2016.
 */
public class FirebaseConfig {
    private static final String URL = "https://metsky.firebaseio.com";
    public static Firebase ref = null;

    public static void initialize() {
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        ref = new Firebase(URL);
    }
}