package org.feelthebern.android.adapters;

/**
 *
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * A base ViewHolder that has a Model type "M". to set the mode call setModel()
 */

public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected M model;
    protected MultiAdapter.ClickListener adapterListener;


    public BaseViewHolder(View view) {
        super(view);
    }


    /**
     * Not enabled by default.
     * To enable in your view, add <br /><br />
     * {@code this.setOnClickListener(this);} <br /><br />
     * in your constructor.
     */
    @Override
    public void onClick(View v) {
        if (model == null) {
            return;
        }

        if (adapterListener != null) {
            adapterListener.onClick(model, v);
        }
    }

    public void setModel(M model) {
        this.model = model;
    }

    @SuppressWarnings("UnusedDeclaration")
    public M getModel() {
        return model;
    }

    public void setItemClickListener(MultiAdapter.ClickListener adapterListener) {
        this.adapterListener = adapterListener;
    }
}
