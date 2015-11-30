package com.ticketrioapp.ticketrioapp.clases;

/**
 * Created by Juanma on 07/11/2015.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class DownloadQR extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;



    public DownloadQR(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    @Override
    protected Bitmap doInBackground(String... voids) {
        String urldisplay = voids[0];
        Bitmap qr = null;
        try {
            HttpResponse<InputStream> response = Unirest.post("https://neutrinoapi-qr-code.p.mashape.com/qr-code")
                    .header("X-Mashape-Key", "BW1eEAaGypmsh4GRu7S5FCbqM8H8p1BtFU0jsnXidKHpvRk5uS")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("bg-color", "#ffffff")
                    .field("content", urldisplay)
                    .field("fg-color", "#000000")
                    .asBinary();
            qr = BitmapFactory.decodeStream(response.getRawBody());

            return qr;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return qr;
    }

    @Override
    protected void onPostExecute(Bitmap result) {


        bmImage.setImageBitmap(result);




        BitmapDrawable drawable = (BitmapDrawable) bmImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();



        File sdCardDirectory = Environment.getExternalStorageDirectory();

        Random rn = new Random();
        Integer answer = rn.nextInt(1000) + 1;
        String numero =  answer.toString();

        File image = new File(sdCardDirectory, "SGEMqr"+numero+".png");



        boolean success = false;

        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        if (success) {
            // Toast.makeText(getApplicationContext(), "Image saved with success",
            // Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(getApplicationContext(),
            // "Error during image saving", Toast.LENGTH_LONG).show();
        }



    }


}