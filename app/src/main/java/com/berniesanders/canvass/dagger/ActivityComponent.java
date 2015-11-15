package com.berniesanders.canvass.dagger;

import com.berniesanders.canvass.MainActivity;
import com.berniesanders.canvass.controllers.ActionBarController;
import com.berniesanders.canvass.repositories.CollectionRepo;
import com.berniesanders.canvass.repositories.PageRepo;
import com.google.gson.Gson;

import dagger.Component;

/**
 *
 */
@FtbActivityScope
@Component(
        modules = ActionBarController.ActionBarModule.class,
        dependencies = MainComponent.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    //What we provide on the FtbActivityScope
    ActionBarController actionBarController();

}
