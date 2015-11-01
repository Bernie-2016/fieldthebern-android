package org.feelthebern.android.views.holders;

import android.view.View;
import android.widget.TextView;

import org.feelthebern.android.R;
import org.feelthebern.android.adapters.BaseViewHolder;
import org.feelthebern.android.models.Linkable;

import butterknife.Bind;
import butterknife.ButterKnife;

import static org.feelthebern.android.parsing.Linky.hasLinks;
import static org.feelthebern.android.parsing.Linky.linkify;

/**
 *
 */
public class LinkableTextHolder<M extends Linkable> extends BaseViewHolder<M> {

    @Bind(R.id.text)
    TextView textView;

    LinkableTextHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setModel(final M model) {
        super.setModel(model);
        setText(model);
    }

    protected void setText(final M model) {
        if (hasLinks(model) && hasText(model)) {
            linkify(textView, model.getLinks(), model.getText());
        } else if (hasText(model)) {
            textView.setText(model.getText());
        }
    }

    protected boolean hasText(final M model) {
        return model.getText() !=null &&
                !model.getText().isEmpty();
    }
}
