package com.berniesanders.canvass.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.berniesanders.canvass.R;
import com.berniesanders.canvass.mortar.DaggerService;
import com.berniesanders.canvass.screens.AddPersonScreen;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 */
public class AddPersonView extends RelativeLayout {

    /**
     */
    @Inject
    AddPersonScreen.Presenter presenter;

    @Bind(R.id.party)
    AppCompatSpinner partySpenner;
    @Bind(R.id.interest)
    AppCompatSpinner interestSpinner;

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
        partySpenner.setAdapter(adapter);
        //ColorStateList csl = new ColorStateList(new int[][]{new int[0]}, new int[]{0xffffcc00});
        //partySpenner.setSupportBackgroundTintList(csl);
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

}
