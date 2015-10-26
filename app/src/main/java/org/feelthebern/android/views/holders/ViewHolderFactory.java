package org.feelthebern.android.views.holders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;

import timber.log.Timber;

/**
 *
 */
public class ViewHolderFactory {


    /**
     * @return a subclass of BaseViewHolder.
     */
    public static BaseViewHolder create(int resId, ViewGroup parent) {

        switch (resId) {
            case R.layout.row_h1:
                return new H1Holder(inflate(resId, parent));

            case R.layout.row_h2:
                return new H2Holder(inflate(resId, parent));

            case R.layout.row_h3:
                return new H3Holder(inflate(resId, parent));

            case R.layout.row_img:
                return new ImgHolder(inflate(resId, parent));

            case R.layout.row_nav:
                return new NavHolder(inflate(resId, parent));

            case R.layout.row_quote:
                return new QuoteHolder(inflate(resId, parent));

            case R.layout.row_p:
                return new PHolder(inflate(resId, parent));

            case R.layout.row_video:
                return new VideoHolder(inflate(resId, parent));

            case R.layout.row_list:
                return new ListHolder(inflate(resId, parent));

            default:
                Timber.e(new Exception(),
                        "Unmapped view holder... res id: %d, returning default",
                        resId);

                return new ErrorViewHolder(inflate(R.layout.row_error, parent));
        }

    }

    private static View inflate(int resId, ViewGroup parent) {
        return LayoutInflater
                .from(parent.getContext())
                .inflate(resId, parent, false);
    }
}
