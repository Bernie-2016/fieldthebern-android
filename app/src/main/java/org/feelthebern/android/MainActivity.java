package org.feelthebern.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.mortar.HasComponent;
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
                    .withService("flow.Flow.FLOW_SERVICE", getFlow())
                    .build(getScopeName());
        }

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

    private Flow getFlow() {
        if (flow == null) {
            flow = new Flow(History.single(new Main()));
        }
        return flow;
    }

    private Main.Component getMainScreenComponent() {
        return DaggerMain_Component.builder()
                .mainComponent(FTBApplication.getComponent())
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //BundleService helps us save state using the standard activity bundle
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initFlow();
    }

    private void initFlow() {

        flow.setDispatcher(new Flow.Dispatcher() {
            @Override
            public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
                Object newState = traversal.destination.top();
                MortarScope activityScope;
                View view;

                if (newState instanceof HasComponent) {
                    activityScope = findChild(getApplicationContext(), getScopeName());

                    MortarScope childScope = activityScope
                            .buildChild()
                            .build(newState.getClass().getName());

                    final Context childContext = childScope.createContext(MainActivity.this);
                    view = LayoutFactory.createView(childContext, newState);
                } else {
                    view = LayoutFactory.createView(container.getContext(), newState);
                }

                Timber.v("Flow.Traversal direction: %s", traversal.direction.toString());
                Timber.v("Flow.Traversal newState: %s", newState.getClass().getSimpleName());

                //if we want fancy transitions, this is where we do that
                container.removeAllViews();
                container.addView(view);
                /////////////////////////////////////////////////////////

                callback.onTraversalCompleted();

                Timber.v("Flow onTraversalCompleted");
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

    @Override
    public void onBackPressed() {
        if (!flow.goBack()) {
            super.onBackPressed();
        }
    }
}
