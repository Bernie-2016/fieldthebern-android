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
import org.feelthebern.android.dagger.Dagger;
import org.feelthebern.android.dagger.MainComponent;
import org.feelthebern.android.dagger.MainModule;
import org.feelthebern.android.issues.IssuesAdapter;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.screens.Main;

import java.io.IOException;

import javax.inject.Inject;

import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;
import static org.feelthebern.android.mortar.DaggerService.createComponent;

public class MainActivity extends AppCompatActivity {
//    @Inject
//    Api mApi;
//    @Inject
//    Gson mGson;
//
//    private GridView mGridView;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Dagger.mainCompenent(this).inject(this);
//
//        mGridView = (GridView) findViewById(R.id.issues_GridView);
//        mApi.loadFeed(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(final Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    return;
//                }
//
//                final String responseString = response.body().string();
//                final Collection homeCollection = mGson.fromJson(responseString, Collection.class);
//                mGridView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mGridView.setAdapter(new IssuesAdapter(MainActivity.this, homeCollection.getApiItems()));
//                    }
//                });
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //------------- Mortar ----------------//

    @Override
    public Object getSystemService(String name) {
        MortarScope activityScope = findChild(getApplicationContext(), getScopeName());

        if (activityScope == null) {
            activityScope = buildChild(getApplicationContext()) //
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(Main.Component.class.getName(), createComponent(Main.Component.class, new MainModule(getApplicationContext())))
                    .build(getScopeName());
        }

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            MortarScope activityScope = findChild(getApplicationContext(), getScopeName());
            if (activityScope != null) activityScope.destroy();
        }

        super.onDestroy();
    }

    private String getScopeName() {
        return getClass().getName();
    }
}
