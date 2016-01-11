package com.berniesanders.fieldthebern.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.annotations.Layout;
import com.berniesanders.fieldthebern.controllers.ActionBarController;
import com.berniesanders.fieldthebern.controllers.ActionBarService;
import com.berniesanders.fieldthebern.controllers.ToastService;
import com.berniesanders.fieldthebern.dagger.FtbScreenScope;
import com.berniesanders.fieldthebern.dagger.MainComponent;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.mortar.FlowPathBase;
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
import mortar.ViewPresenter;
import rx.functions.Action0;
import timber.log.Timber;

import static com.berniesanders.fieldthebern.parsing.FormValidator.isEmailValid;
import static com.berniesanders.fieldthebern.parsing.FormValidator.isPhoneValid;

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
        private Person personToEdit;       //person loaded from the API from a previous visit (aka being edited
        private Person currentPerson;
        private boolean isNewPerson;

        @Bind(R.id.submit)
        Button submitButton;
        @Bind(R.id.instructions_label)
        TextView instructionsLabel;

        @BindString(android.R.string.cancel) String cancel;
        @BindString(R.string.add_person) String addPerson;
        @BindString(R.string.edit_person) String editPerson;
        @BindString(R.string.editing_person) String editing;
        @BindString(R.string.err_their_first_name_blank) String blankFirstName;
        @BindString(R.string.err_invalid_email) String invalidEmailError;
        @BindString(R.string.err_invalid_phone) String invalidPhoneError;

        @Inject
        Presenter(VisitRepo visitRepo, @Nullable Person personToEdit) {
            this.visitRepo = visitRepo;
            this.personToEdit = personToEdit;
            isNewPerson = personToEdit == null;
            currentPerson = new Person();
            if (!isNewPerson) {
                currentPerson = Person.copy(personToEdit);
            }
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.v("onLoad");
            ButterKnife.bind(this, getView());
            setActionBar();
            if(!isNewPerson) {
                getView().showPerson(currentPerson);
                submitButton.setText(R.string.done);
                instructionsLabel.setText(String.format(editing, currentPerson.fullName()));

            }
        }

        String getScreenTitle() {
            return (isNewPerson) ? addPerson : editPerson;
        }

        void setActionBar() {
            ActionBarController.MenuAction menu =
                    new ActionBarController.MenuAction()
                            .label(cancel)
                            .action(new Action0() {
                                @Override
                                public void call() {
                                    if (getView()!= null) {
                                        Flow.get(getView()).goBack();
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

            if(currentPerson !=null) {
                // update on dropView to keep our currentPerson in sync
                // with the values displayed in the view
                view.updatePerson(currentPerson);
            }
        }

        @OnClick(R.id.submit)
        public void addPerson() {

            //update our "temp" person with value from the form
            getView().updatePerson(currentPerson);

            //make sure they are valid
            if(!formIsValid(currentPerson)) { return; }

            if (!isNewPerson) {
                //if we're editing a person loaded from the API, update its values and update the visit
                personToEdit.update(currentPerson);
                visitRepo.addPerson(personToEdit);
            } else {
                //otherwise, just add the temp person
                visitRepo.addPerson(currentPerson);
            }

            Flow.get(getView()).goBack();
        }

        private boolean formIsValid(Person person) {

            if (StringUtils.isBlank(person.attributes().firstName())) {
                ToastService.get(getView()).bern(blankFirstName);
                return false;
            } else if (!StringUtils.isBlank(person.attributes().email()) &&  //blank allowed, but invalid not
                    !isEmailValid(person.attributes().email())) {
                ToastService.get(getView()).bern(invalidEmailError);
                return false;
            } else if (!StringUtils.isBlank(person.attributes().phone()) &&  //blank allowed, but invalid not
                    !isPhoneValid(person.attributes().phone())) {
                ToastService.get(getView()).bern(invalidPhoneError);
                return false;
            } else if (getView().getEmailCheckBox().isChecked() &&
                    StringUtils.isBlank(person.attributes().email())) {
                ToastService.get(getView()).bern(invalidEmailError);
                return false;
            } else if (getView().getPhoneCheckBox().isChecked() &&
                    StringUtils.isBlank(person.attributes().phone())) {
                ToastService.get(getView()).bern(invalidPhoneError);
                return false;
            }
            return true;
        }
    }
}
