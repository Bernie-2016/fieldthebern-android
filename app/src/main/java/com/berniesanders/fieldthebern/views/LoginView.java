package com.berniesanders.fieldthebern.views;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.UserAttributes;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.screens.LoginScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 */
public class LoginView extends RelativeLayout {

    /**
     */
    @Inject
    LoginScreen.Presenter presenter;

    @Bind(R.id.email)
    AppCompatEditText email;

    public LoginView(Context context) {
        super(context);
        injectSelf(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        DaggerService.<LoginScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
    }

    public void loadUserEmailAccounts(AutoCompleteTextView emailAutocompleteTV) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getContext()).getAccountsByType("com.google");

        final List<String> possibleEmails = new ArrayList<>();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                possibleEmails.add(possibleEmail);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                possibleEmails);

        emailAutocompleteTV.setAdapter(adapter);
        emailAutocompleteTV.setThreshold(0);

    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) { return; }
        Timber.v("onFinishInflate");
        ButterKnife.bind(this, this);
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

    public void showFacebook(UserAttributes userAttributes) {
        email.setText(userAttributes.getEmail());
    }
}
