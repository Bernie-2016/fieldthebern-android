package org.feelthebern.android;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.GsonBuilder;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.feelthebern.android.events.ChangePageEvent;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Content;
import org.feelthebern.android.mortar.GsonParceler;
import org.feelthebern.android.mortar.MortarScreenSwitcherFrame;
import org.feelthebern.android.parsing.CollectionDeserializer;
import org.feelthebern.android.parsing.PageContentDeserializer;
import org.feelthebern.android.screens.Main;
import org.feelthebern.android.views.PaletteTransformation;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;
import static org.feelthebern.android.apilevels.ApiLevel.isLollipopOrAbove;

public class MainActivity extends AppCompatActivity implements Flow.Dispatcher {

    private MortarScope activityScope;
    private FlowDelegate flowDelegate;

    @Bind(R.id.container_main)
    MortarScreenSwitcherFrame container;

    @Bind(R.id.backdrop)
    ImageView backgroundImage;

    @Bind(R.id.shading)
    View shading;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;

    @Override
    public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {
//        Path newScreen = traversal.destination.top();
//        String title = newScreen.getClass().getSimpleName();
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
                    .build(getScopeName());
        }

        return activityScope.hasService(name) ? activityScope.getService(name)
                : super.getSystemService(name);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Collection.class, new CollectionDeserializer());
        gsonBuilder.registerTypeAdapter(Content.class, new PageContentDeserializer());

        GsonParceler parceler = new GsonParceler(gsonBuilder.create());

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
                getHistory(savedInstanceState, parceler),
                this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FTBApplication.getEventBus().register(this);

        setToolbarStyle();
    }

    private History getHistory(Bundle savedInstanceState, GsonParceler parceler) {
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("ENTRIES") != null) {
            return History.from(savedInstanceState, parceler);
        }
        return History.single(new Main());
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        flowDelegate.onSaveInstanceState(outState);
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
    public void onChangePageEvent(ChangePageEvent event) {

        Timber.v("onChangePageEvent e=%s", event.toString());
        Picasso.with(getApplicationContext())
                .load(event.getImgUrl())
                .transform(PaletteTransformation.instance())
                .placeholder(backgroundImage.getDrawable())
                .into(backgroundImage, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) backgroundImage.getDrawable()).getBitmap(); // Ew!
                        Palette palette = PaletteTransformation.getPalette(bitmap);

                        if (isLollipopOrAbove()) {
                            setStatusBarColor(palette.getDarkVibrantColor(Color.BLACK));
                        }
                    }
                });

        appBarLayout.setExpanded(!event.shouldClose(), true);

        collapsingToolbar.setTitle(event.getTitle());

        animateShading(event.getImgUrl() != null);
        animateBg(event.getImgUrl() != null);

        if (event.getImgUrl() == null && isLollipopOrAbove()) {
            setStatusBarColor(Color.parseColor("#087ed7"));
        }
    }



    private void setToolbarStyle() {
        Typeface typeface = TypefaceUtils.load(getAssets(), "fonts/Dosis-Medium.otf");
        collapsingToolbar.setCollapsedTitleTypeface(typeface);
        collapsingToolbar.setExpandedTitleTypeface(typeface);

        if (isLollipopOrAbove()) {
            setStatusBarColor(Color.parseColor("#087ed7"));
        }
    }

    void animateShading(boolean show) {
        shading.setVisibility(View.VISIBLE);
        float toAlpha = show ? 1 : 0;
        ObjectAnimator.ofFloat(shading, "alpha", shading.getAlpha(), toAlpha)
                .setDuration(100)
                .start();
    }

    void animateBg(boolean show) {
        backgroundImage.setVisibility(View.VISIBLE);
        float toAlpha = show ? 1 : 0;
        ObjectAnimator.ofFloat(backgroundImage, "alpha", backgroundImage.getAlpha(), toAlpha)
                .setDuration(100)
                .start();
    }


    @TargetApi(21)
    void setStatusBarColor(int color) {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }




}
