package com.berniesanders.canvass.views;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.models.UserAttributes;
import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.SignupScreen;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 */
public class SignupView extends RelativeLayout {

    /**
     */
    @Inject
    SignupScreen.Presenter presenter;

    @Bind(R.id.first_name)
    AppCompatEditText firstName;


    @Bind(R.id.last_name)
    AppCompatEditText lastName;


    @Bind(R.id.email)
    AppCompatEditText email;


    @Bind(R.id.password)
    AppCompatEditText password;


    @Bind(R.id.user_photo)
    ImageView userImageView;


    public SignupView(Context context) {
        super(context);
        injectSelf(context);
    }

    public SignupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public SignupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) { return; }
        DaggerService.<SignupScreen.Component>
                getDaggerComponent(context, DaggerService.DAGGER_SERVICE)
                .inject(this);
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

        firstName.setText(userAttributes.getFirstName());
        lastName.setText(userAttributes.getLastName());
        email.setText(userAttributes.getEmail());

    }

    public ImageView getUserImageView() {
        return userImageView;
    }

    public UserAttributes getInput(UserAttributes userAttributes) {

        return userAttributes
                    .email(email.getText().toString())
                    .password(password.getText().toString())
                    .firstName(firstName.getText().toString())
                    .lastName(lastName.getText().toString());
    }

}
