package org.feelthebern.android;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.feelthebern.android.api.Api;
import org.feelthebern.android.api.models.Collection;
import org.feelthebern.android.dagger.Dagger;

import java.io.IOException;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject
    Api mApi;
    @Inject
    Gson mGson;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dagger.mainCompenent(this).inject(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.issues_gridview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        final TextView json = (TextView) findViewById(R.id.mytext);
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
                Collection homeCollection = mGson.fromJson(responseString, Collection.class);
                json.post(new Runnable() {
                    @Override
                    public void run() {
                        json.setText(responseString);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
