package com.tifaniwarnita.metsky.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tifaniwarnita.metsky.models.Cuaca;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Tifani on 3/18/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    //Android's default system path of application db
    private static String DB_PATH = "";
    private static String DB_NAME = "metsky.sqlite";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase myDB;
    private Context context;

    // All Static variables
    // Tables' name
    private static final String TABLE_CUACA = "cuaca";

    // Cuaca table column names
    private static final String CUACA_KEY_ID = "_id";
    private static final String CUACA_KOTA = "kota";
    private static final String CUACA_LEVEL = "level";
    private static final String CUACA_CUACA = "cuaca_content";
    private static final String CUACA_LATITUDE = "latitude";
    private static final String CUACA_LONGITUDE = "longitude";

    private static final String FIXED_ID = "1";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if(android.os.Build.VERSION.SDK_INT >= 4.2){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(db); */
    }

    @Override
    public synchronized void close() {
        if (myDB!=null) {
            myDB.close();
        }
        super.close();
    }

    //Check if the database is exist on device or not
    public boolean checkDB() {
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            Log.e("bs - check", e.getMessage());
        }
        if (tempDB!=null)
            tempDB.close();
        return tempDB != null ? true : false;
    }

    //Method to copy database from source code assets to device
    public void copyDB() throws IOException {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("tle99 - copyDatabase", e.getMessage());
        }
    }

    //Open DB
    public void openDB() throws SQLiteException {
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    //Check if the database doesn't exist on device, create new one
    public void createDB() throws IOException {
        boolean dbExist = checkDB();

        if(dbExist) {

        } else {
            this.close();
            this.getReadableDatabase();
            try {
                copyDB();
            } catch (IOException e) {
                Log.e("bs - create", e.getMessage());
            }
        }
    }

    public Cuaca getCuaca() {
        Cuaca cuaca = null;
        this.close();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CUACA + " WHERE " + CUACA_KEY_ID + " = ?";
        System.out.println(query);
        Cursor c;

        try {
            c = db.rawQuery(query, new String[] {FIXED_ID});
            if (c==null) return null;

            c.moveToFirst();
            cuaca = new Cuaca(
                    String.valueOf(c.getString(c.getColumnIndex(CUACA_KOTA))),
                    String.valueOf(c.getString(c.getColumnIndex(CUACA_LEVEL))),
                    String.valueOf(c.getString(c.getColumnIndex(CUACA_CUACA))),
                    String.valueOf(c.getString(c.getColumnIndex(CUACA_LATITUDE))),
                    String.valueOf(c.getString(c.getColumnIndex(CUACA_LONGITUDE))));
            c.close();
        } catch (Exception e) {
            Log.e("bs - getService", e.getMessage());
        }
        db.close();
        System.out.println("GET CUACA " + cuaca.getKota());
        return cuaca;
    }

    public boolean insertCuaca(Cuaca cuaca) {
        deleteCuacaContents();
        System.out.println("INSERT CUACA");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUACA_KEY_ID, FIXED_ID);
        contentValues.put(CUACA_KOTA, cuaca.getKota());
        contentValues.put(CUACA_LEVEL, cuaca.getLevel());
        contentValues.put(CUACA_CUACA, cuaca.getCuaca());
        contentValues.put(CUACA_LATITUDE, cuaca.getLatitude());
        contentValues.put(CUACA_LONGITUDE, cuaca.getLongitude());
        db.insert(TABLE_CUACA, null, contentValues);
        return true;
    }

    private int deleteCuacaContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CUACA, null, null);
    }

    public ArrayList<String> getTables() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add( c.getString( c.getColumnIndex("name")) );
                c.moveToNext();
            }
        }
        return arrTblNames;
    }

}
