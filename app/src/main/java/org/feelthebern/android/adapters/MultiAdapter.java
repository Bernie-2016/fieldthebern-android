package org.feelthebern.android.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * handles multiple content types and view holders, which all must extend a base view holder
 */
public abstract class MultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Map<Class<?>, OnTypedItemClick> listeners = new HashMap<>();
    protected ClickListener itemClickListener;


    public MultiAdapter() {
        setupMainListener();
    }

    private void setupMainListener() {

        itemClickListener = new ClickListener() {
            @Override
            public void onClick(Object model, View view) {

                // sanity check, dump early if no model
                // because if we have no model, we don't know which listener to trigger
                if (model == null) { return; }

                Class<?> klazz = model.getClass();

                if (listeners.containsKey(klazz)) {
                    listeners.get(klazz).onClick(model, view);
                }
            }
        };
    }


    /**
     * Add a listener for a specific model. Listeners are kept based on the model class as the 'key'
     *
     * @param modelType The type of model backing the View being clicked. This must match the class
     *                  of the type used to create the ViewHolder
     */
    public <MT> void addItemClickListener(Class<MT> modelType, OnTypedItemClick listener) {
        listeners.put(modelType, listener);
    }



    /**
     * Listens to item clicks from a RecyclerView.
     *
     * Basically the same as #ItemClickListener, but this is for outside clients
     */
    public interface OnTypedItemClick {
        void onClick(Object model, View view);
    }


    /**
     * Created in the adapter and passed to view holders
     */
    public interface ClickListener  {
        void onClick(Object model, View view);
    }


}
