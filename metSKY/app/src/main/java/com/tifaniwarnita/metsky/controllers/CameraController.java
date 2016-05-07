package com.tifaniwarnita.metsky.controllers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.tifaniwarnita.metsky.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tifani on 3/30/2016.
 */
public class CameraController {
    public static final int REQUEST_TAKE_PHOTO = 1;

    private static final String TAG = CameraController.class.getSimpleName();


    private static String currentPhotoPath;

    private static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/metSKY");
        try{
            if(storageDir.mkdir()) {
                Log.i(TAG, "Directory metSKY created");
            } else {
                Log.i(TAG, "Directory metSKY is not created");
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static void dispatchTakePictureIntent(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public static void galleryAddPic(Activity activity, Bitmap photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        //Add watermark
        try {
            OutputStream fOut = null;
            fOut = new FileOutputStream(f);
            photo.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
            fOut.close(); // do not forget to close the stream
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);
        Toast.makeText(activity.getApplicationContext(),
                "Photo saved!", Toast.LENGTH_SHORT).show();
    }

    // Another option
    private static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static Bitmap onCameraResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == activity.RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);

            //Add watermark
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), contentUri);
                Bitmap newPhoto = mark(activity.getApplicationContext(), photo, "metSKY");
                return newPhoto;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.i(TAG, "Result code is not ok");
            deletePicture(currentPhotoPath);
            return null;
        }
    }

    private static void deletePicture(String filePath) {
        try {
            // delete the original file
            new File(filePath).delete();

        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    public static Bitmap cropPicture(Bitmap srcBmp) {
        Bitmap dstBmp;
        if (srcBmp.getWidth() >= srcBmp.getHeight()){
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );
        }else {
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }
        return dstBmp;
    }

    public static Bitmap mark(Context context, Bitmap src, String watermark) {
        Bitmap watermarkImage = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.watermark_image);

        //Crop and scale
        Bitmap image = Bitmap.createScaledBitmap(
                cropPicture(src),
                watermarkImage.getWidth(),
                watermarkImage.getHeight(),
                true);

        Bitmap result = Bitmap.createBitmap(
        watermarkImage.getWidth(),
                watermarkImage.getHeight(),
                src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(image, 0, 0, null);
        canvas.drawBitmap(watermarkImage, 0, 0, null);

        return result;
    }
}

