package org.feelthebern.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.mortar.LayoutFactory;
import org.feelthebern.android.screens.DaggerMain_Component;
import org.feelthebern.android.screens.Main;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.History;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import timber.log.Timber;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

public class MainActivity extends AppCompatActivity {

    Flow flow;

    @Bind(R.id.container_main)
    FrameLayout container;

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
                    .withService(DaggerService.DAGGER_SERVICE, getMainScreenComponent())
                    .build(getScopeName());
        }

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    private Main.Component getMainScreenComponent() {
        return DaggerMain_Component.builder()
                .mainComponent(FTBApplication.getComponent())
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFlow();
    }

    private void initFlow() {
        flow = new Flow(History.single(new Main()));
        flow.setDispatcher(new Flow.Dispatcher() {
            @Override
            public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
                Timber.v("Flow.Traversal direction: %s", traversal.direction.toString());
                Object newState = traversal.destination.top();
                Timber.v("Flow.Traversal newState: %s", newState.getClass().getSimpleName());
                View view = LayoutFactory.createView(container.getContext(), newState);

                //if we want fancy transitions, this is where we do that
                container.removeAllViews();
                container.addView(view);
                /////////////////////////////////////////////////////////

                callback.onTraversalCompleted();
            }
        });
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
