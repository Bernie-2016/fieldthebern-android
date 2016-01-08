package com.berniesanders.fieldthebern.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.models.ApiAddress;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.parsing.StatesDataParser;
import com.berniesanders.fieldthebern.screens.StatePrimaryScreen;

import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * Custom view for State primaries
 * Created by Vishal on 1/7/16.
 */
public class StatePrimaryView extends RelativeLayout {

    @Inject
    StatePrimaryScreen.Presenter presenter;

    @Bind(R.id.state_primary_image)
    AppCompatImageView primaryImage;

    @Bind(R.id.state_primary_name)
    TextView stateName;

    @Bind(R.id.state_primary_type)
    TextView primaryType;

    @Bind(R.id.state_primary_date)
    TextView primaryDate;

    @Bind(R.id.state_primary_deadline)
    TextView primaryDeadline;

    @Bind(R.id.state_primary_description)
    TextView primaryDescription;

    public StatePrimaryView(Context context) {
        super(context);
        injectSelf(context);
    }

    public StatePrimaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public StatePrimaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);

    }

    /**
     * This is how the presenter is injected on to this view
     *
     * Component type is how the DaggerService finds the right component
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) {return;}
        DaggerService.
                <StatePrimaryScreen.Component>getDaggerComponent(context, DaggerService.DAGGER_SERVICE) // get component
                .inject(this); // inject presenter into this view

    }

    public void populateStateInfo(ApiAddress apiAddress) {
        String stateCode = apiAddress.attributes().state();

        Context context = getContext();
        StatesDataParser parser = new StatesDataParser(context);
        Map<String, String> dictionary = parser.getStateInfoFromCode(stateCode);

        String name = dictionary.get("state");
        String type = dictionary.get("type");
        String date = "On " + dictionary.get("date");
        String deadline = "Registration Deadline: " + dictionary.get("deadline");
        String description = dictionary.get("details");

        String packageName = context.getPackageName();
        if (name != null) {
            int resId = getResources().getIdentifier(name.toLowerCase(), "drawable", packageName);
            Drawable img = context.getResources().getDrawable(resId);
            if (img != null) {
                primaryImage.setImageDrawable(img);
            }
        }

        stateName.setText(name);
        primaryType.setText(type);
        primaryDate.setText(date);
        primaryDeadline.setText(deadline);
        primaryDescription.setText(description);
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
        if (isInEditMode()) {return;}
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

}
