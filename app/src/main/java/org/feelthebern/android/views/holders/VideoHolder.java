package org.feelthebern.android.views.holders;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.Video;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 *
 */
public class VideoHolder extends BaseViewHolder<Video> {

    @Bind(R.id.text)
    TextView textView;

    @Bind(R.id.youtubethumbnailview)
    YouTubeThumbnailView youTubeThumbnailView;

    VideoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setModel(final Video model) {
        super.setModel(model);
        textView.setText(model.getText());

        if(model.getText().toLowerCase().contains("youtube") && model.getId() != null) {
            //youtube video?  we hope...

            youTubeThumbnailView
                    .initialize(youTubeThumbnailView
                                    .getContext()
                                    .getString(R.string.youtubeApiKey),
                            new YouTubeThumbnailView.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                                    YouTubeThumbnailLoader youTubeThumbnailLoader) {
                                    Timber.v("onInitializationSuccess");
                                    youTubeThumbnailLoader.setVideo(model.getId());
                                }

                                @Override
                                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {
                                    Timber.e("onInitializationFailure");
                                }
                            });


            youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = YouTubeIntents.createPlayVideoIntent(v.getContext(), model.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
