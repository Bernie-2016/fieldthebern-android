package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.views.AddPersonView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import flow.History;
import mortar.ViewPresenter;
import rx.functions.Action0;
import timber.log.Timber;

/**
 * Example for creating new Mortar Screen that helps explain how it all works
 *
 * Set the @Layout annotation to the resource id of the layout for the screen
 */
@Layout(R.layout.screen_add_person)
public class AddPersonScreen extends FlowPathBase {

    /**
     */
    public AddPersonScreen() {
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerAddPersonScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .build();
    }

    /**
     */
    @Override
    public String getScopeName() {
        return AddPersonScreen.class.getName();
    }


    @dagger.Module
    class Module {
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class)
    public interface Component {
        void inject(AddPersonView t);
        VisitRepo visitRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<AddPersonView> {

        private final VisitRepo visitRepo;

        @BindString(android.R.string.cancel) String cancel;
        @BindString(R.string.add_person) String addPerson;

        @Inject
        Presenter(VisitRepo visitRepo) {
            this.visitRepo = visitRepo;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
        }


        void setActionBar() {
            ActionBarController.MenuAction menu =
                    new ActionBarController.MenuAction()
                            .label(cancel)
                            .action(new Action0() {
                                @Override
                                public void call() {
                                    if (getView()!=null) {
                                        Flow.get(getView()).setHistory(History.single(new Main()), Flow.Direction.BACKWARD);
                                    }
                                }
                            });
            ActionBarService
                    .getActionbarController(getView())
                    .showToolbar()
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(addPerson, menu));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(AddPersonView view) {
            super.dropView(view);
        }

        @OnClick(R.id.add_person)
        public void addPerson() {
            visitRepo.addPerson(getView().getPerson());
            Flow.get(getView()).goBack();
        }

    }
}
