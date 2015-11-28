package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.adapters.NavigationAdapter;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.CollectionRepo;
import com.berniesanders.fieldthebern.views.NavigationView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 * Layout only really needed for Flow screens
 */
@Layout(R.layout.screen_navigation_drawer)
public class NavigationScreen extends FlowPathBase {


    /**
     * Not used in this case as flow isn't involved to call this method.
     * NavigationView injects itself directly
     */
    @Override
    public Object createComponent() {
        return null;
    }

    @Override
    public String getScopeName() {
        return NavigationScreen.class.getName();
    }

//    @dagger.Module
//    class Module {
//    }

    /**
     * This component is used to inject the view with the presenter once the view is inflated.
     * The view will injected itself using this component on inflate.
     * Expose anything you want injected to the presenter here
     *
     * Only use "dependencies = MainComponent.class" if you need something from the main component
     * Only use "modules = Module.class" if you need a module
     */
    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(NavigationView t);
        CollectionRepo repo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<NavigationView> {


        // in case needed later to show expandable issue list
        private final CollectionRepo repo;
        DrawerLayout drawerLayout;

        @Bind(R.id.drawer_listview)
        ListView drawerListView;

        /**
         * When the view is inflated, this presented is automatically injected to the View
         * Constructor parameters are injected here automatically
         */
        @Inject
        Presenter(CollectionRepo repo) {
            this.repo = repo;
        }

        /**
         * called when the presenter and view are ready.
         * getView() will not be null
         */
        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");

            drawerLayout = (DrawerLayout) getView().getParent();
            ButterKnife.bind(this, getView());
            createNavigationDrawer();
        }

        private void createNavigationDrawer() {

            drawerListView.setAdapter(new NavigationAdapter(new String[]{"Canvassing", "Issues"}));

            drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Flow flow = Flow.get(view);
                    switch (position) {
                        case 0:
                            if (!(flow.getHistory().top() instanceof MapScreen)) {
                                view.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        flow.set(new MapScreen());
                                    }
                                }, 150);
                            }
                            break;
                        case 1:
                            if (!(flow.getHistory().top() instanceof Main)) {
                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        flow.set(new Main());
                                    }
                                });
                            }
                            break;
                    }
                    drawerLayout.closeDrawers();
                }
            });
        }

        /**
         * called on rotation only -- may not be applicable here, because no-flow
         */
        @Override
        protected void onSave(Bundle outState) {
        }


        /**
         * last chance at the view before it is detached
         */
        @Override
        public void dropView(NavigationView view) {
            super.dropView(view);
            drawerLayout=null;
            drawerListView.setOnItemClickListener(null);
            drawerListView=null;
        }

    }
}
