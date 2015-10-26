package org.feelthebern.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.feelthebern.android.events.ChangeBackgroundEvent;
import org.feelthebern.android.mortar.DaggerService;
import org.feelthebern.android.mortar.GsonParceler;
import org.feelthebern.android.mortar.HandlesBack;
import org.feelthebern.android.mortar.MortarScreenSwitcherFrame;
import org.feelthebern.android.screens.DaggerMain_Component;
import org.feelthebern.android.screens.Main;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import flow.path.Path;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

public class MainActivity extends AppCompatActivity implements Flow.Dispatcher {

    private MortarScope activityScope;
    private FlowDelegate flowDelegate;

    @Bind(R.id.container_main)
    MortarScreenSwitcherFrame container;

    @Bind(R.id.backdrop)
    ImageView backgroundImage;

    @Override
    public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
        Path newScreen = traversal.destination.top();
        String title = newScreen.getClass().getSimpleName();
//        ActionBarOwner.MenuAction menu = new ActionBarOwner.MenuAction("Friends", new Action0() {
//            @Override public void call() {
//                Flow.get(MortarDemoActivity.this).set(new FriendListScreen());
//            }
//        });
//        actionBarOwner.setConfig(
//                new ActionBarOwner.Config(false, !(newScreen instanceof ChatListScreen), title, menu));

        container.dispatch(traversal, callback);
    }

    //------------- Mortar ----------------//

    @Override
    public Object getSystemService(String name) {

        if (flowDelegate != null) {
            Object flowService = flowDelegate.getSystemService(name);
            if (flowService != null) return flowService;
        }

        MortarScope activityScope = findChild(getApplicationContext(), getScopeName());

        if (activityScope == null) {
            activityScope = buildChild(getApplicationContext()) //
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    //.withService(DaggerService.DAGGER_SERVICE, getMainScreenComponent())
                            //.withService("flow.Flow.FLOW_SERVICE", getFlow())
                    .build(getScopeName());
        }

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }

//    private Flow getFlow() {
//        if (flow == null) {
//            flow = new Flow(History.single(new Main()));
//        }
//        return flow;
//    }

//    private Main.Component getMainScreenComponent() {
//        return DaggerMain_Component.builder()
//                .mainComponent(FTBApplication.getComponent())
//                .build();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GsonParceler parceler = new GsonParceler(new Gson());

        @SuppressWarnings("deprecation")
        FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) getLastNonConfigurationInstance();


        //BundleService helps us save state using the standard activity bundle
        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        flowDelegate = FlowDelegate.onCreate(
                nonConfig,
                getIntent(),
                savedInstanceState,
                parceler,
                History.single(new Main()),
                this);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        FTBApplication.getEventBus().register(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        flowDelegate.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flowDelegate.onResume();
    }

    @Override
    protected void onPause() {
        flowDelegate.onPause();
        super.onPause();
    }

//    @SuppressWarnings("deprecation") // https://code.google.com/p/android/issues/detail?id=151346
//    @Override public Object onRetainNonConfigurationInstance() {
//        return flowDelegate.onRetainNonConfigurationInstance();
//    }

//    private void initFlow() {
//
//        flow.setDispatcher(new Flow.Dispatcher() {
//            @Override
//            public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
//                Object newState = traversal.destination.top();
//                MortarScope activityScope;
//                View view;
//
//                if (newState instanceof HasComponent) {
//                    activityScope = findChild(getApplicationContext(), getScopeName());
//
//                    MortarScope childScope = activityScope
//                            .buildChild()
//                            .build(newState.getClass().getName());
//
//                    final Context childContext = childScope.createContext(MainActivity.this);
//                    view = LayoutFactory.createView(childContext, newState);
//                } else {
//                    view = LayoutFactory.createView(container.getContext(), newState);
//                }
//
//                Timber.v("Flow.Traversal direction: %s", traversal.direction.toString());
//                Timber.v("Flow.Traversal newState: %s", newState.getClass().getSimpleName());
//
//                //if we want fancy transitions, this is where we do that
//                container.removeAllViews();
//                container.addView(view);
//                /////////////////////////////////////////////////////////
//
//                callback.onTraversalCompleted();
//
//                Timber.v("Flow onTraversalCompleted");
//            }
//        });
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        //actionBarOwner.dropView(this);
        //actionBarOwner.setConfig(null);

        // activityScope may be null in case isWrongInstance() returned true in onCreate()
        if (isFinishing() && activityScope != null) {
            activityScope.destroy();
            activityScope = null;
        }
        FTBApplication.getEventBus().unregister(this);
        super.onDestroy();
    }

    private String getScopeName() {
        return getClass().getName();
    }

    /**
     * Inform the view about back events.
     */
    @Override
    public void onBackPressed() {
        if (!container.onBackPressed()) super.onBackPressed();
    }


    /**
     * Required for the custom font library
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Subscribe
    public void onChangeBackgroundEvent(ChangeBackgroundEvent event) {

        Picasso.with(getApplicationContext())
                .load(event.getUrl())
                .into(backgroundImage);
    }
}
