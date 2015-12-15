package com.berniesanders.fieldthebern.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.media.PartyIcon;
import com.berniesanders.fieldthebern.models.CanvassData;
import com.berniesanders.fieldthebern.models.CanvassResponse;
import com.berniesanders.fieldthebern.models.Person;
import com.berniesanders.fieldthebern.models.Visit;
import com.berniesanders.fieldthebern.mortar.DaggerService;
import com.berniesanders.fieldthebern.parsing.CanvassResponseEvaluator;
import com.berniesanders.fieldthebern.screens.AddPersonScreen;
import com.berniesanders.fieldthebern.screens.NewVisitScreen;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import timber.log.Timber;

/**
 * Example mortar screen.
 * Change what it extends as needed. Any View/Layout type is fine to extend
 */
public class NewVisitView extends RelativeLayout {

    /**
     * Make sure you are pointing at the correct presenter type
     * YourScreen.Presenter
     */
    @Inject
    NewVisitScreen.Presenter presenter;

    @Bind(R.id.person_container)
    ViewGroup personContainer;

    public NewVisitView(Context context) {
        super(context);
        injectSelf(context);
    }

    public NewVisitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        injectSelf(context);
    }

    public NewVisitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        injectSelf(context);
    }


    /**
     * This is how the presenter is injected on to this view
     *
     * Important to note component type is how the DaggerService finds the right component
     */
    private void injectSelf(Context context) {
        if (isInEditMode()) {return;}
        DaggerService.<NewVisitScreen.Component>
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
        if (isInEditMode()) {return;}
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void showPeople(final Visit visit) {

        personContainer.removeAllViews();

        for (CanvassData canvassItem : visit.included()) {
            if(canvassItem.type().equals(Person.TYPE)) {
                showPerson((Person) canvassItem);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showPerson(Person person) {
        View personRow = LayoutInflater
                .from(getContext())
                .inflate(R.layout.row_person, personContainer, false);

        TextView personName = (TextView) personRow.findViewById(R.id.person);
        ImageView partyIcon = (ImageView) personRow.findViewById(R.id.party);
        TextView supportLevel = (TextView) personRow.findViewById(R.id.interest);

        //personRow.findViewById(R.id.edit) //edit btn

        partyIcon.setImageResource(PartyIcon.get(person.attributes().party()));

        personName.setText(
                person.attributes().firstName()
                + " "
                + person.attributes().lastName());

        //TODO: canvass response is the machine readable format, this is not correct for i18n
        supportLevel.setText(CanvassResponseEvaluator
                .getText(person.attributes().canvassResponse(),
                        getContext().getResources().getStringArray(R.array.interest)));

        personContainer.addView(personRow);

        personRow.findViewById(R.id.edit).setOnClickListener(onClickListener);
        personRow.findViewById(R.id.edit).setTag(person);
    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            editPerson((Person) v.getTag());
        }
    };

    void editPerson(Person person) {
        Flow.get(this).set(new AddPersonScreen(person));
    }

}
