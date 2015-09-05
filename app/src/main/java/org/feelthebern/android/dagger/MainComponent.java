package org.feelthebern.android.dagger;

import org.feelthebern.android.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by AndrewOrobator on 8/29/15.
 */
@Singleton
@Component(modules = MainModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
