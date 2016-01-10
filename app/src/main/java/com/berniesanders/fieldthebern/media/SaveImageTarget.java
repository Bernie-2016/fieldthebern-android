package com.berniesanders.fieldthebern.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import rx.Observable;
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

    public SaveImageTarget(OnLoad callback) {

        this.callback = callback;
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

        String encodedString = base64EncodeBitmap(bitmap);

        callback.onLoad(bitmap, encodedString);
    }

    /**
     * TODO this doesnt seem to work correctly with the API.
     * It does seem to correctly convert a bitmap to a base64 string, but for some reason
     * the API does not understand the data
     */

    public static String base64EncodeBitmap(final Bitmap bitmap) {

        int bytes = bitmap.getByteCount();
        Timber.v("num kb of img %d", bytes/1000);
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();

//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 1, out);
//
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        byte[] outArray = out.toByteArray();
//        options.inSampleSize = 4;
//        Bitmap decoded = BitmapFactory.decodeByteArray(outArray, 0, outArray.length, options);
//
//        int bytes = decoded.getByteCount();
//        Timber.v("num kb of img %d", bytes/1000);
//        ByteBuffer buffer = ByteBuffer.allocate(bytes);
//        decoded.copyPixelsToBuffer(buffer);
//        byte[] array = buffer.array();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 5, out);
//        out.toByteArray()
        return Base64.encodeToString(array, Base64.NO_WRAP);
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
