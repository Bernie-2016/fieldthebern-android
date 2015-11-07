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
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.berniesanders.canvass.config.Actions;
import com.berniesanders.canvass.dagger.ActivityComponent;
import com.berniesanders.canvass.dagger.DaggerActivityComponent;
import com.berniesanders.canvass.dagger.FtbActivityScope;
import com.berniesanders.canvass.db.SearchMatrixCursor;
import com.berniesanders.canvass.models.ApiItem;
import com.berniesanders.canvass.models.Collection;
import com.berniesanders.canvass.models.Page;
import com.berniesanders.canvass.mortar.ActionBarController;
import com.berniesanders.canvass.mortar.ActionBarService;
import com.berniesanders.canvass.mortar.GsonParceler;
import com.berniesanders.canvass.mortar.MortarScreenSwitcherFrame;
import com.berniesanders.canvass.screens.CollectionScreen;
import com.berniesanders.canvass.screens.Main;
import com.berniesanders.canvass.screens.MapScreen;
import com.berniesanders.canvass.screens.PageScreen;
import com.berniesanders.canvass.views.PaletteTransformation;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import flow.Flow;
import flow.FlowDelegate;
import flow.History;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static com.berniesanders.canvass.apilevels.ApiLevel.isLollipopOrAbove;
import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

@FtbActivityScope
public class MainActivity extends AppCompatActivity implements ActionBarController.Activity, Flow.Dispatcher {

    private MortarScope activityScope;
    private FlowDelegate flowDelegate;
    private Menu menu;
    SearchView searchView;
    private ActionBarController.MenuAction actionBarMenuAction;

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

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.drawer_listview)
    ListView drawerListView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.bernie_dark_blue)
    int bernieDarkBlue;

    @Inject
    Gson gson;
    private ActionBarDrawerToggle drawerToggle;

    @Inject
    ActionBarController actionBarController;


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

        if (activityScope!=null && activityScope.hasService(name)) {
            return activityScope.getService(name);
        }

        return super.getSystemService(name);
    }

    ActivityComponent activityComponent;
    private ActivityComponent createComponent() {

        if (activityComponent==null) {
            activityComponent = DaggerActivityComponent.builder()
                    .mainComponent(FTBApplication.getComponent())
                    .actionBarModule(new ActionBarController.ActionBarModule())
                    .build();
        }

        return activityComponent;
    }

    FlowDelegate.NonConfigurationInstance nonConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createComponent().inject(this);

        initActivityScope();

        GsonParceler parceler = new GsonParceler(gson);

        FlowDelegate.NonConfigurationInstance nonConfig =
                (FlowDelegate.NonConfigurationInstance) getLastCustomNonConfigurationInstance();


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

        setSupportActionBar(toolbar);

        FTBApplication.getEventBus().register(this);

        setToolbarStyle();

        createNavigationDrawer();

        handleIntent(getIntent());
        actionBarController.takeView(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void initActivityScope() {
        activityScope = findChild(getApplicationContext(), getScopeName());

        if (activityScope == null) {
            activityScope = buildChild(getApplicationContext()) //
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(ActionBarService.NAME, actionBarController)
                    .build(getScopeName());
        }
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
    @Override public Object onRetainCustomNonConfigurationInstance() {
        return flowDelegate.onRetainNonConfigurationInstance();
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
        actionBarController.dropView(this);
        actionBarController.setConfig(null);

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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!container.onBackPressed()) super.onBackPressed();
        }
    }


    /**
     * Required for the custom font library
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void setToolbarStyle() {
        Typeface typeface = TypefaceUtils.load(getAssets(), "fonts/Dosis-Medium.otf");
        collapsingToolbar.setCollapsedTitleTypeface(typeface);
        collapsingToolbar.setExpandedTitleTypeface(typeface);

        if (isLollipopOrAbove()) {
            setStatusBarColor(bernieDarkBlue);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        handleOptionsMenu(menu);
        return true;
    }

    private void handleOptionsMenu(Menu menu) {
        if (actionBarMenuAction != null) {

            if (actionBarMenuAction.isSearch()) {
                setupSearchMenu(menu);
            } else {
                menu.add(actionBarMenuAction.label())
                        .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                actionBarMenuAction.action().call();
                                return true;
                            }
                        });
            }
            //        // Inflate the options menu from XML
            //        MenuInflater inflater = getMenuInflater();
            //        inflater.inflate(R.menu.menu_cancel, menu);
        }
    }

    private void setupSearchMenu(Menu menu){
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setBackgroundColor(bernieDarkBlue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //handleOptionsMenu(menu);
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
            if (item.getTitle().equals(title)) {
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

    private void createNavigationDrawer() {
        drawerListView.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, R.id.drawer_list_item_text, new String[]{"Issues", "Canvassing"}));
        // Set the list's click listener
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Flow flow = Flow.get(view);
                switch (position) {
                    case 0:
                        if (!(flow.getHistory().top() instanceof Main)) {
                            flow.set(new Main());
                        }
                        break;
                    case 1:
                        if (!(flow.getHistory().top() instanceof MapScreen)) {
                            flow.set(new MapScreen());
                        }
                        break;

                }
                drawerLayout.closeDrawers();
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public void setMenu(ActionBarController.MenuAction action) {
        if (action != actionBarMenuAction) {
            actionBarMenuAction = action;
            invalidateOptionsMenu();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTitle(CharSequence title) {
        collapsingToolbar.setTitle(title);
    }


    /**
     * TODO: move this animation and toolbar code to it's own controller thing...
     */
    @Override
    public void setMainImage(String url) {

        animateShading(false);

        Picasso.with(getApplicationContext())
                .load(url)
                .transform(PaletteTransformation.instance())
                .placeholder(backgroundImage.getDrawable())
                .into(backgroundImage,
                        new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) backgroundImage.getDrawable()).getBitmap(); // Ew!
                                Palette palette = PaletteTransformation.getPalette(bitmap);

                                if (isLollipopOrAbove()) {
                                    setStatusBarColor(palette.getDarkVibrantColor(Color.BLACK));
                                }

                                animateShading(true);
                                animateBg(true);
                            }
                        });

        if (url == null) {
            backgroundImage.setImageDrawable(null);
        }

        if (url == null && isLollipopOrAbove()) {
            setStatusBarColor(bernieDarkBlue);
        }
    }

    @Override
    public void hideToolbar() {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        collapsingToolbar.setLayoutParams(params);
        collapsingToolbar.requestLayout();
    }

    @Override
    public void showToolbar() {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        collapsingToolbar.setLayoutParams(params);
        collapsingToolbar.requestLayout();
        appBarLayout.setExpanded(false, true);
    }

    @Override
    public void openAppbar() {
        appBarLayout.setExpanded(true, true);
    }

    @Override
    public void closeAppbar() {
        appBarLayout.setExpanded(false, true);
    }
}
