package com.ka8eem.market24.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class Keys {

    // vars
    public static final String latitude = "latitude";
    public static final String longtitude = "longtitude";
    public static final String Address = "address";
    public static final String image_domain = "https://markt-24.online/market/";

    public static final String buyer = "buyer";
    public static final String seller = "seller";

   public static MultipartBody.Part compressImage(
            Context mContext,
            Uri uri,
            String  fieldName
    ) { //create a file to write bitmap data
        File f =new  File(
                mContext.getCacheDir(),
                ThreadLocalRandom.current().nextInt() + ".jpg");
        try {
            f.createNewFile();
            Bitmap original= getBitmapFromPath(uri, mContext);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 70, out);
            byte[] imageBytes = out.toByteArray();
            //write the bytes in file
            FileOutputStream  fos = new FileOutputStream(f);
            fos.write(imageBytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), f);
        return MultipartBody.Part.createFormData(fieldName, "$fieldName.jpg", fileReqBody);
    }


    static Bitmap getBitmapFromPath(
            Uri uri,
            Context mContext
    ) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true ;
        ContentResolver cr = mContext.getContentResolver() ;
        InputStream input = null;
        InputStream input1  = null ;
        try {
            input = cr.openInputStream(uri);
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        Bitmap takenImage = null ;
        try {
            input1 = cr.openInputStream(uri);
            takenImage = BitmapFactory.decodeStream(input1);
            input1.close();
        } catch ( java.lang.Exception  e) {
            e.printStackTrace();
        }
        return takenImage;
    }

}
