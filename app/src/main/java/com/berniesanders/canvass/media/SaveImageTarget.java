package com.berniesanders.canvass.media;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
        int bytes = bitmap.getByteCount();
        Timber.v("num kb of img %d", bytes/1000);

        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();
        String encodedString = Base64.encodeToString(array, Base64.DEFAULT);

        //target.setImageDrawable(new BitmapDrawable(target.getContext().getResources(), bitmap));

        callback.onLoad(bitmap, encodedString);

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
