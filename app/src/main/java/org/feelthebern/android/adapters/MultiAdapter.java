package org.feelthebern.android.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * handles multiple content types and view holders, which all must extend a base view holder
 */
public abstract class MultiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    /**
     * Created in the adapter and passed to view holders
     */
    public interface ClickListener  {
        void onClick(Object model, View view);
    }


}
