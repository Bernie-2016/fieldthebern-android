package com.berniesanders.fieldthebern.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import timber.log.Timber;

/*
 * Made with love by volunteers
 * Copyright 2015 BernieSanders.com, FeelTheBern.org,
 * Coderly, LostPacketSoftware and the volunteers
 * License: GNU AGPLv3 - https://gnu.org/licenses/agpl.html 
 */
public class SaveImageTarget implements Target {

    public interface OnLoad {
        void onLoad(Bitmap bitmap, String encodedString);
    }

    private final OnLoad callback;
    private final Context context;

    public SaveImageTarget(OnLoad callback, Context context) {

        this.callback = callback;
        this.context = context;
    }

    /**
     * Callback when an image has been successfully loaded.
     * <p/>
     * <strong>Note:</strong> You must not recycle the bitmap.
     *
     * @param bitmap
     * @param from
     */
    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        Timber.v("onBitmapLoaded %s", from.toString());

        String encodedString = base64EncodeBitmap(bitmap, context);

        callback.onLoad(bitmap, encodedString);
    }

    /**
     * TODO this doesnt seem to work correctly with the API.
     * It does seem to correctly convert a bitmap to a base64 string, but for some reason
     * the API does not understand the data
     * TODO this should maybe be move to it's own class
     */

    public static String base64EncodeBitmap(final Bitmap bitmap, Context context) {


        File originalFile = SavePhoto.saveBitmap(bitmap, context);
        InputStream inputStream = null;//You can get an inputStream using any IO API
        try {
            inputStream = new FileInputStream(originalFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.NO_WRAP);
        Timber.v("encodedString " + encodedString);
        return encodedString;
    }


    /**
     * Callback indicating the image could not be successfully loaded.
     * <p/>
     * <strong>Note:</strong> The passed {@link Drawable} may be {@code null} if none has been
     * specified via {@link com.squareup.picasso.RequestCreator#error(Drawable)}
     * or {@link com.squareup.picasso.RequestCreator#error(int)}.
     *
     * @param errorDrawable
     */
    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        Timber.w("onBitmapFailed");
    }

    /**
     * Callback invoked right before your request is submitted.
     * <p/>
     * <strong>Note:</strong> The passed {@link Drawable} may be {@code null} if none has been
     * specified via {@link com.squareup.picasso.RequestCreator#placeholder(Drawable)}
     * or {@link com.squareup.picasso.RequestCreator#placeholder(int)}.
     *
     * @param placeHolderDrawable
     */
    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }


}
