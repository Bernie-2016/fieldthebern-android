package org.feelthebern.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.GridView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.feelthebern.android.api.Api;
import org.feelthebern.android.api.models.Collection;
import org.feelthebern.android.dagger.Dagger;
import org.feelthebern.android.issues.IssuesAdapter;

import java.io.IOException;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject
    Api mApi;
    @Inject
    Gson mGson;

    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dagger.mainCompenent(this).inject(this);

        mGridView = (GridView) findViewById(R.id.issues_GridView);
        mApi.loadFeed(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }

                final String responseString = response.body().string();
                final Collection homeCollection = mGson.fromJson(responseString, Collection.class);
                mGridView.post(new Runnable() {
                    @Override
                    public void run() {
                        mGridView.setAdapter(new IssuesAdapter(MainActivity.this, homeCollection.getApiItems()));
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
