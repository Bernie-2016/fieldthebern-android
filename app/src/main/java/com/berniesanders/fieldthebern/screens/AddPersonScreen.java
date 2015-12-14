package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
import com.berniesanders.fieldthebern.parsing.FormValidator;
import com.berniesanders.fieldthebern.repositories.VisitRepo;
import com.berniesanders.fieldthebern.views.AddPersonView;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Provides;
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

    @Nullable
    private final Person personToEdit;

    /**
     */
    public AddPersonScreen(@Nullable Person personToEdit) {
        this.personToEdit = personToEdit;
    }

    /**
     */
    @Override
    public Object createComponent() {
        return DaggerAddPersonScreen_Component
                .builder()
                .mainComponent(FTBApplication.getComponent())
                .module(new Module(personToEdit))
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
        private final Person personToEdit;

        public Module(Person personToEdit) {
            this.personToEdit = personToEdit;
        }

        @Provides
        @FtbScreenScope
        @Nullable
        public Person providePerson() {
            return personToEdit;
        }
    }

    /**
     */
    @FtbScreenScope
    @dagger.Component(dependencies = MainComponent.class, modules = Module.class)
    public interface Component {
        void inject(AddPersonView t);
        VisitRepo visitRepo();
    }

    @FtbScreenScope
    static public class Presenter extends ViewPresenter<AddPersonView> {

        private final VisitRepo visitRepo;
        private final Person personToEdit;

        @Bind(R.id.submit)
        Button submitButton;
        @Bind(R.id.instructions_label)
        TextView instructionsLabel;

        @BindString(android.R.string.cancel) String cancel;
        @BindString(R.string.add_person) String addPerson;
        @BindString(R.string.edit_person) String editPerson;
        @BindString(R.string.editing_person) String editing;
        @BindString(R.string.err_their_first_name_blank) String blankFirstName;

        @Inject
        Presenter(VisitRepo visitRepo, @Nullable Person personToEdit) {
            this.visitRepo = visitRepo;
            this.personToEdit = personToEdit;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
            if(personToEdit!=null) {
                getView().showPerson(personToEdit);
                submitButton.setText(R.string.done);
                instructionsLabel.setText(String.format(editing, personToEdit.fullName()));
            }
        }

        String getScreenTitle() {
            return (personToEdit==null) ? addPerson : editPerson;
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
                    .get(getView())
                    .showToolbar()
                    .closeAppbar()
                    .setMainImage(null)
                    .setConfig(new ActionBarController.Config(getScreenTitle(), menu));
        }

        @Override
        protected void onSave(Bundle outState) {
        }

        @Override
        public void dropView(AddPersonView view) {
            super.dropView(view);

            if(personToEdit!=null) {
                // update on dropView to keep our personToEdit in sync
                // with the values displayed in the view
                view.updatePerson(personToEdit);
            }
        }

        @OnClick(R.id.submit)
        public void addPerson() {

            Person person = null;

            if (personToEdit==null) {
                person = new Person();
                getView().updatePerson(person);
                visitRepo.addPerson(person);
            } else {
                person = personToEdit;
                getView().updatePerson(personToEdit);
            }

            if(!formIsValid(person)) { return; }

            Flow.get(getView()).goBack();
        }

        private boolean formIsValid(Person person) {

            if (StringUtils.isBlank(person.attributes().firstName())) {
                ToastService.get(getView()).bern(blankFirstName);
                return false;
            } else if (StringUtils.isBlank(person.attributes().firstName())) {
                ToastService.get(getView()).bern(blankFirstName);
                return false;
            }
            return true;
        }

    }
}
