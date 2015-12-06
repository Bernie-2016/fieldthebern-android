package com.berniesanders.fieldthebern.views;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.CanvassResponse;
import com.berniesanders.fieldthebern.models.Contact;
import com.berniesanders.fieldthebern.models.Party;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.parsing.CanvassResponseEvaluator;
import com.berniesanders.fieldthebern.parsing.PartyEvaluator;
import com.berniesanders.fieldthebern.screens.AddPersonScreen;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import timber.log.Timber;

/**
 */
public class AddPersonView extends RelativeLayout {

    /**
     */
    @Inject
    AddPersonScreen.Presenter presenter;

    @Bind(R.id.party)
    AppCompatSpinner partySpinner;

    @Bind(R.id.interest)
    AppCompatSpinner interestSpinner;

    @Bind(R.id.first_name)
    EditText firstNameEditText;

    @Bind(R.id.last_name)
    EditText lastNameEditText;

    @Bind(R.id.email)
    EditText emailEditText;

    @Bind(R.id.phone)
    EditText phoneEditText;

    @Bind(R.id.phone_checkbox)
    CheckBox phoneCheckBox;

    @Bind(R.id.email_checkbox)
    CheckBox emailCheckBox;

    public AddPersonView(Context context) {
        super(context);
        injectSelf(context);
    }

    public AddPersonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public AddPersonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        DaggerService.<AddPersonScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) { return; }
        Timber.v("onFinishInflate");
        ButterKnife.bind(this, this);

        //noinspection unchecked
        ArrayAdapter<String> adapter = new ArrayAdapter(
                getContext(),
                R.layout.party_spinner_item,
                R.id.text,
                getContext().getResources().getStringArray(R.array.party)
                );
        adapter.setDropDownViewResource(R.layout.party_spinner_drop_item);
        partySpinner.setAdapter(adapter);
        //ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xffffcc00});
        //partySpinner.setSupportBackgroundTintList(csl);
        //noinspection unchecked
        ArrayAdapter<CharSequence> interestAdapter = new ArrayAdapter(
                getContext(),
                R.layout.party_spinner_item,
                R.id.text,
                getContext().getResources().getStringArray(R.array.interest)
        );
        interestAdapter.setDropDownViewResource(R.layout.party_spinner_drop_item);
        interestSpinner.setAdapter(interestAdapter);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) { return; }
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public boolean isValid() {
        //bit of half baked validation
        return firstNameEditText.getText() != null;
    }

    public Person getPerson() {
        Person person = new Person();
        person.attributes()
                .canvassResponse(getCanvassResponse())
                .party(getParty())
                .firstName(firstNameEditText.getText().toString());

        if (lastNameEditText.getText() != null) {
            person.attributes().lastName(lastNameEditText.getText().toString());
        }

        if (phoneEditText.getText() != null) {
            person.attributes().phone(phoneEditText.getText().toString());

            if(phoneCheckBox.isChecked()) {
                person.attributes().preferredContact(Contact.PHONE);
            }
        }

        if (emailEditText.getText() != null) {
            person.attributes().email(emailEditText.getText().toString());

            if(emailCheckBox.isChecked()) {
                person.attributes().preferredContact(Contact.EMAIL);
            }
        }

        return person;
    }

    @CanvassResponse.Response
    private String getCanvassResponse() {
        String selectedItem = (String) interestSpinner.getSelectedItem();
        String[] interest = getContext().getResources().getStringArray(R.array.interest);
        return CanvassResponseEvaluator.getResponse(selectedItem, interest);
    }

    @Party.Affiliation
    private String getParty() {
        String selectedItem = (String) partySpinner.getSelectedItem();
        String[] interest = getContext().getResources().getStringArray(R.array.party);
        return PartyEvaluator.getParty(selectedItem, interest);
    }

    @OnCheckedChanged(R.id.email_checkbox)
    void onEmailChecked(boolean checked) {

        if(checked) {
            phoneCheckBox.setChecked(false);
        }

    }

    @OnCheckedChanged(R.id.phone_checkbox)
    void onPhoneChecked(boolean checked) {
        if(checked) {
            emailCheckBox.setChecked(false);
        }
    }
}
