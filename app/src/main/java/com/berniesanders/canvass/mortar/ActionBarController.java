package com.berniesanders.canvass.mortar;

/**
 *
 */

import android.content.Context;
import android.os.Bundle;

import com.berniesanders.canvass.dagger.FtbActivityScope;

import dagger.Module;
import dagger.Provides;
import mortar.Presenter;
import mortar.bundler.BundleService;
import rx.functions.Action0;

import static mortar.bundler.BundleService.getBundleService;

/**
 * Allows shared configuration of the Android ActionBar.
 */
public class ActionBarController extends Presenter<ActionBarController.Activity> {
    public interface Activity {

        void setTitle(CharSequence title);
        void setMenu(MenuAction action);
        void hideToolbar();
        void showToolbar();
        void closeAppbar();
        void openAppbar();
        void setMainImage(String url);
        Context getContext();
    }

    public static class Config {
        public final CharSequence title;
        public final MenuAction action;

        public Config(CharSequence title, MenuAction action) {
            this.title = title;
            this.action = action;
        }

        public Config withAction(MenuAction action) {
            return new Config(title, action);
        }
    }

    public static class MenuAction {
        private CharSequence label;
        private Action0 action;
        private boolean isSearch;
        private int menuResId = -1;

        public MenuAction label(CharSequence label){
            this.label = label;
            return this;
        }
        public MenuAction action(Action0 action){
            this.action = action;
            return this;
        }
        public MenuAction setIsSearch(){
            this.isSearch = true;
            return this;
        }
        public MenuAction menuResId(int id){
            this.menuResId = id;
            return this;
        }
        public MenuAction() {
        }

        public CharSequence label() {
            return label;
        }

        public Action0 action() {
            return action;
        }

        public boolean isSearch() {
            return isSearch;
        }

        public int menuResId() {
            return menuResId;
        }
    }

    private Config config;

    ActionBarController() {
    }

    @Override
    public void onLoad(Bundle savedInstanceState) {
        if (config != null) update();
    }

    public void setConfig(Config config) {
        this.config = config;
        update();
    }

    public Config getConfig() {
        return config;
    }

    @Override
    protected BundleService extractBundleService(Activity activity) {
        return getBundleService(activity.getContext());
    }

    private void update() {
        if (!hasView()) { return; }
        Activity activity = getView();

        activity.setTitle(config.title);
        activity.setMenu(config.action);
    }



    public ActionBarController hideToolbar() {
        Activity activity = getView();
        activity.hideToolbar();
        return this;
    }
    public ActionBarController showToolbar() {
        Activity activity = getView();
        activity.showToolbar();
        return this;
    }
    public ActionBarController closeAppbar() {
        Activity activity = getView();
        activity.closeAppbar();
        return this;
    }
    public ActionBarController openAppbar() {
        Activity activity = getView();
        activity.openAppbar();
        return this;
    }
    public ActionBarController setMainImage(String url) {
        Activity activity = getView();
        activity.setMainImage(url);
        return this;
    }

    @Module
    public static class ActionBarModule {

        @Provides
        @FtbActivityScope
        ActionBarController provideActionBarOwner() {
            return new ActionBarController();
        }
    }

    /*
        @Module
    public static class ActionBarModule {
        private static ActionBarOwner actionBarOwner;
        public ActionBarModule() {
            actionBarOwner = new ActionBarOwner();
        }

        @Provides
        @FtbActivityScope
        ActionBarOwner provideActionBarOwner() {
            return actionBarOwner;
        }
    }
     */
}
