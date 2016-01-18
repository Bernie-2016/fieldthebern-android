package com.berniesanders.fieldthebern.views.holders;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.BaseViewHolder;
import com.berniesanders.fieldthebern.models.Video;
import com.berniesanders.fieldthebern.views.VideoContainerView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 *
 */
public class VideoHolder extends BaseViewHolder<Video> {

    //@Bind(R.id.text)
    //TextView textView;

    @Bind(R.id.youtubethumbnailview)
    YouTubeThumbnailView thumbnail;

    VideoContainerView videoContainer;
    YouTubeThumbnailLoader loader;

    @Bind(R.id.play_arrow_icon)
    ImageView playIcon;

    ViewGroup.LayoutParams layoutParams;

    VideoHolder(View itemView) {
        super(itemView);
        videoContainer = (VideoContainerView) itemView;
        ButterKnife.bind(this, itemView);
        layoutParams = thumbnail.getLayoutParams();
    }

    @Override
    public void setModel(final Video model) {
        super.setModel(model);
        removeTextView();
        playIcon.setVisibility(View.INVISIBLE);

        if (model.getSrc()==null) { return; }

        if (model.getSrc().toLowerCase().contains("youtube") && model.getId() == null) {
            // there are about 40 video without the id string set,
            // see if we can try parsing it from the url

            String idFromPath = Uri.parse(model.getSrc()).getLastPathSegment();

            if (idFromPath!=null) {
                model.setId(idFromPath);
                Timber.w("Hacking the youtube id from the path.  id: %s", idFromPath);
            }
        }

        if(model.getSrc().toLowerCase().contains("youtube") && model.getId() != null) {
            //youtube video?  we hope...
            //textView.setVisibility(View.GONE);
            videoContainer.setOnDetachListener(onDetachListener);
            if(loader!=null) {
                loader.release();
            }

            //see https://code.google.com/p/gdata-issues/issues/detail?id=7533
            videoContainer.removeViewAt(0);

            YouTubeThumbnailView thumbnailView = new YouTubeThumbnailView(videoContainer.getContext());
            thumbnailView.setLayoutParams(layoutParams);
            thumbnailView.setId(R.id.youtubethumbnailview);
            thumbnailView.setAdjustViewBounds(true);
            videoContainer.addView(thumbnailView, 0);
            thumbnailView
                    .initialize(thumbnail
                                    .getContext()
                                    .getString(R.string.googleApiKey),
                            new YouTubeThumbnailView.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubeThumbnailView view,
                                                                    YouTubeThumbnailLoader loader) {
                                    Timber.v("onInitializationSuccess");
                                    loader.setOnThumbnailLoadedListener(thumbnailLoadedListener);
                                    VideoHolder.this.loader = loader;
                                    loader.setVideo(model.getId());
                                }

                                @Override
                                public void onInitializationFailure(YouTubeThumbnailView view,
                                                                    YouTubeInitializationResult result) {
                                    Timber.e("onInitializationFailure: %s", result.toString());
                                }
                            });


            thumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = YouTubeIntents.createPlayVideoIntent(v.getContext(), model.getId());
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            //try showing the video url as a text-link
            playIcon.setVisibility(View.INVISIBLE);
            videoContainer.findViewById(R.id.youtubethumbnailview).setVisibility(View.INVISIBLE);

            TextView textView = (TextView) LayoutInflater
                    .from(videoContainer.getContext())
                    .inflate(R.layout.item_video_link, videoContainer, false);

            //String linkText = String.format("<a href=\"%s\">%s</a>", model.getText(), model.getText() );
            //textView.setMovementMethod(LinkMovementMethod.getInstance());
            //textView.setText(linkText);
            textView.setText(model.getText());
            videoContainer.addView(textView);
        }
    }

    VideoContainerView.OnDetachListener onDetachListener = new VideoContainerView.OnDetachListener() {
        @Override
        public void onDetach(VideoContainerView videoContainerView) {
            if (loader!=null) {
                loader.release();
            }

            removeTextView();
        }
    };

    private void removeTextView() {

        //if we added a textview we should probably remove it
        View textView = videoContainer.findViewById(R.id.video_txt_link);
        if (textView!=null) {
            videoContainer.removeView(textView);
        }
    }

    YouTubeThumbnailLoader.OnThumbnailLoadedListener thumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
            playIcon.setVisibility(View.VISIBLE);
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
            Timber.e("Youtube thumbnail loader error %s", errorReason.toString());
        }
    };
}
