package com.example.samuel.umovies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Samuel on 25/07/2017.
 */

public class BitmapUtil {
    public static byte[] getbyteArray (Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }
    public static Bitmap getBitmap(byte[]array){
        return BitmapFactory.decodeByteArray(array,0,array.length);

    }
}

