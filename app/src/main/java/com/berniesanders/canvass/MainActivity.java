package com.berniesanders.canvass;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.berniesanders.canvass.db.SearchMatrixCursor;
import com.berniesanders.canvass.mortar.GsonParceler;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import com.berniesanders.canvass.config.Actions;
import com.berniesanders.canvass.events.ChangePageEvent;
import com.berniesanders.canvass.events.ShowToolbarEvent;
import com.berniesanders.canvass.models.ApiItem;
import com.berniesanders.canvass.models.Collection;
import com.berniesanders.canvass.models.Page;
import com.berniesanders.canvass.mortar.MortarScreenSwitcherFrame;
import com.berniesanders.canvass.screens.CollectionScreen;
import com.berniesanders.canvass.screens.Main;
import com.berniesanders.canvass.screens.PageScreen;
import com.berniesanders.canvass.views.PaletteTransformation;

import java.util.Set;

import javax.inject.Inject;

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
import static com.berniesanders.canvass.apilevels.ApiLevel.isLollipopOrAbove;

public class MainActivity extends AppCompatActivity implements Flow.Dispatcher {

    private MortarScope activityScope;
    private FlowDelegate flowDelegate;
    private Menu menu;
    SearchView searchView;

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

    @Inject
    Gson gson;



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

        FTBApplication.getComponent().inject(this);

        GsonParceler parceler = new GsonParceler(gson);

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

        handleIntent(getIntent());
    }

    private History getHistory(Bundle savedInstanceState, GsonParceler parceler) {
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("ENTRIES") != null) {
            return History.from(savedInstanceState, parceler);
        }
        return History.single(new Main());
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
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
        flowDelegate.onSaveInstanceState(outState);
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

        if(event.shouldHideToolbar()) {
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
            collapsingToolbar.setLayoutParams(params);
            collapsingToolbar.requestLayout();
        } else {
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            collapsingToolbar.setLayoutParams(params);
            collapsingToolbar.requestLayout();
        }

        if (!event.shouldRamain()) {
            appBarLayout.setExpanded(!event.shouldClose(), true);
        }

        if (event.getTitle() != null) {
            collapsingToolbar.setTitle(event.getTitle());
        }

        animateShading(event.getImgUrl() != null);
        animateBg(event.getImgUrl() != null);

        if (event.getImgUrl() == null && isLollipopOrAbove()) {
            setStatusBarColor(Color.parseColor("#087ed7"));
        }
    }

    @Subscribe
    public void onShowToolbarEvent(ShowToolbarEvent event) {
        if (event.shouldShowToolbar()) {

            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            collapsingToolbar.setLayoutParams(params);
            collapsingToolbar.requestLayout();
            appBarLayout.setExpanded(false, true);
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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setBackgroundColor(Color.parseColor("#087ed7"));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        flowDelegate.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Actions.SEARCH_SELECTION_PAGE.equals(intent.getAction())) {
            String title = intent.getExtras().getString(SearchManager.EXTRA_DATA_KEY);
            showPage(title);
            MenuItemCompat.collapseActionView(this.menu.findItem(R.id.menu_search));

        } else if (Actions.SEARCH_SELECTION_COLLECTION.equals(intent.getAction())) {
            String title = intent.getExtras().getString(SearchManager.EXTRA_DATA_KEY);
            showCollection(title);
            MenuItemCompat.collapseActionView(this.menu.findItem(R.id.menu_search));
        }
    }

    private void showPage(String title) {

        Set<ApiItem> items = SearchMatrixCursor.allItems;

        for (ApiItem item : items) {
            if (item.getTitle().equals(title)){
                final Page searchItem = (Page) item;
                Timber.v("Showing page from search: %s", searchItem.getTitle());
                container.post(new Runnable() {
                    @Override
                    public void run() {
                        Flow.get(MainActivity.this).set(new PageScreen(searchItem));
                    }
                });
                break;
            }
        }
    }

    private void showCollection(String title) {

        Set<ApiItem> items = SearchMatrixCursor.allItems;

        for (ApiItem item : items) {
            if (item.getTitle().equals(title)) {
                final Collection searchItem = (Collection) item;
                Timber.v("Showing Collection from search: %s", item.getTitle());
                container.post(new Runnable() {
                    @Override
                    public void run() {
                        Flow.get(MainActivity.this).set(new CollectionScreen(searchItem));
                    }
                });
                break;
            }
        }
    }
}
