package com.berniesanders.fieldthebern;

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
import android.support.annotation.NonNull;
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
import android.widget.ImageView;

import com.berniesanders.fieldthebern.config.Actions;
import com.berniesanders.fieldthebern.controllers.DialogController;
import com.berniesanders.fieldthebern.controllers.DialogService;
import com.berniesanders.fieldthebern.controllers.PhotoController;
import com.berniesanders.fieldthebern.controllers.PhotoService;
import com.berniesanders.fieldthebern.controllers.ToastController;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.controllers.FacebookController;
import com.berniesanders.fieldthebern.controllers.FacebookService;
import com.berniesanders.fieldthebern.controllers.LocationController;
import com.berniesanders.fieldthebern.controllers.LocationService;
import com.berniesanders.fieldthebern.controllers.PermissionController;
import com.berniesanders.fieldthebern.controllers.PermissionService;
import com.berniesanders.fieldthebern.controllers.ProgressDialogController;
import com.berniesanders.fieldthebern.controllers.ProgressDialogService;
import com.berniesanders.fieldthebern.dagger.FtbActivityScope;
import com.berniesanders.fieldthebern.db.SearchMatrixCursor;
import com.berniesanders.fieldthebern.models.ApiItem;
import com.berniesanders.fieldthebern.models.Collection;
import com.berniesanders.fieldthebern.models.Page;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.mortar.GsonParceler;
import com.berniesanders.fieldthebern.mortar.MortarScreenSwitcherFrame;
import com.berniesanders.fieldthebern.screens.CollectionScreen;
import com.berniesanders.fieldthebern.screens.InitialScreen;
import com.berniesanders.fieldthebern.screens.PageScreen;
import com.berniesanders.fieldthebern.views.PaletteTransformation;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
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

import static com.berniesanders.fieldthebern.apilevels.ApiLevel.isLollipopOrAbove;
import static mortar.MortarScope.buildChild;
import static mortar.MortarScope.findChild;

@FtbActivityScope
public class MainActivity extends AppCompatActivity
        implements
        ActionBarController.Activity,
        DialogController.Activity,
        FacebookController.Activity,
        PermissionController.Activity,
        ProgressDialogController.Activity,
        ToastController.Activity,
        PhotoController.Activity,
        Flow.Dispatcher {

    private MortarScope activityScope;
    private FlowDelegate flowDelegate;
    private Menu menu;
    SearchView searchView;
    private ActionBarController.MenuAction actionBarMenuAction;
    private ActionBarDrawerToggle drawerToggle;
    private InitialScreen initialScreen;
    CallbackManager callbackManager;

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

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.bernie_dark_blue)
    int bernieDarkBlue;

    @Inject
    Gson gson;

    @Inject
    ActionBarController actionBarController;

    @Inject
    ToastController toastController;

    @Inject
    DialogController dialogController;

    @Inject
    FacebookController facebookController;

    @Inject
    LocationController locationController;

    @Inject
    ProgressDialogController progressDialogController;

    @Inject
    PermissionController permissionController;

    @Inject
    PhotoController photoController;

    @Override
    public void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback) {

        if (menu != null && menu.findItem(R.id.menu_search) != null) {
            MenuItemCompat.collapseActionView(menu.findItem(R.id.menu_search)); //hide any half-open SearchView
        }
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


    FlowDelegate.NonConfigurationInstance nonConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialScreen = new InitialScreen();
        FTBApplication.getComponent().inject(this);
        FTBApplication.getComponent().inject(initialScreen);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Timber.d("Facebook app signature: %s", FacebookSdk.getApplicationSignature(getApplicationContext()));
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

        setupDrawerToggle();

        handleIntent(getIntent());

        dialogController.takeView(this);
        actionBarController.takeView(this);
        facebookController.takeView(this);
        permissionController.takeView(this);
        progressDialogController.takeView(this);
        toastController.takeView(this);
        photoController.takeView(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookController.onActivityResult(requestCode, resultCode, data);
        photoController.onResult(requestCode, resultCode, data);
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionController.onResult(requestCode, permissions, grantResults);

    }
    private void setupDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
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
                    .withService(ToastService.NAME, toastController)
                    .withService(DialogService.NAME, dialogController)
                    .withService(FacebookService.NAME, facebookController)
                    .withService(LocationService.NAME, locationController)
                    .withService(ProgressDialogService.NAME, progressDialogController)
                    .withService(PermissionService.NAME, permissionController)
                    .withService(PhotoService.NAME, photoController)
                    .build(getScopeName());
        }
    }


    private History getHistory(Bundle savedInstanceState, GsonParceler parceler) {
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("ENTRIES") != null) {
            return History.from(savedInstanceState, parceler);
        }
        return History.single(initialScreen.get());
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
        flowDelegate.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        actionBarController.dropView(this);
        dialogController.dropView(this);
        facebookController.dropView(this);
        permissionController.dropView(this);
        progressDialogController.dropView(this);
        toastController.dropView(this);
        photoController.dropView(this);

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
        Typeface typeface = TypefaceUtils.load(getAssets(), "fonts/Lato-Heavy.ttf");
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



    @Override
    public void setMenu(ActionBarController.MenuAction action) {
        if (action != actionBarMenuAction) {
            actionBarMenuAction = action;
            invalidateOptionsMenu();
        }
    }

    @Override
    public AppCompatActivity getActivity() {
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

    @Override
    public void lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.syncState();
    }

    @Override
    public void unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
    }
}
