package org.feelthebern.android.views.holders;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.Video;
import org.feelthebern.android.views.VideoContainerView;

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

    VideoHolder(View itemView) {
        super(itemView);
        videoContainer = (VideoContainerView) itemView;
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setModel(final Video model) {
        super.setModel(model);

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
            thumbnail
                    .initialize(thumbnail
                                    .getContext()
                                    .getString(R.string.youtubeApiKey),
                            new YouTubeThumbnailView.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubeThumbnailView view,
                                                                    YouTubeThumbnailLoader loader) {
                                    Timber.v("onInitializationSuccess");
                                    VideoHolder.this.loader = loader;
                                    loader.setVideo(model.getId());
                                }

                                @Override
                                public void onInitializationFailure(YouTubeThumbnailView view,
                                                                    YouTubeInitializationResult result) {
                                    Timber.e("onInitializationFailure: %s", result.toString());
                                }
                            });


            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = YouTubeIntents.createPlayVideoIntent(v.getContext(), model.getId());
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            //textView.setText(model.getText());
            //textView.setVisibility(View.VISIBLE);
        }
    }

    VideoContainerView.OnDetachListener onDetachListener = new VideoContainerView.OnDetachListener() {
        @Override
        public void onDetach(VideoContainerView videoContainerView) {
            if (loader!=null) {
                loader.release();
            }
        }
    };
}
