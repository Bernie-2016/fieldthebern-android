package org.feelthebern.android.db;


import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;

import org.feelthebern.android.FTBApplication;
import org.feelthebern.android.R;
import org.feelthebern.android.config.Actions;
import org.feelthebern.android.models.ApiItem;
import org.feelthebern.android.models.Collection;
import org.feelthebern.android.models.Page;
import org.feelthebern.android.repositories.CollectionRepo;
import org.feelthebern.android.repositories.specs.CollectionSpec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class SearchMatrixCursor extends MatrixCursor {


    public static final String[] COLUMNS = {BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_ICON_1,
            SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
            SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA

    };

    private static final long MAX_RESULTS = 5;


    @SuppressWarnings("unused")
    private final Context c;

    static Collection collection;
    Subscription subscription;
    public static Set<ApiItem> allItems;

    @Inject
    CollectionRepo repo;
    private String query;

    public SearchMatrixCursor(String[] columnNames, Context c) {
        super(COLUMNS);
        this.c = c;
        allItems = new HashSet<>();
    }


    public Cursor getSearchSuggestionCursor(String query) {
        if (allItems.isEmpty()) {
            FTBApplication.getComponent().inject(this);
            loadCollectionFromRepo(query);
            return this;
        }

        convertAndAddRows(query);
        return this;
    }

    private void parsePageList(String query) {

        //List<Page> unfilteredPages;
        allItems = new HashSet<>();
        allItems.addAll(addChildPages(collection));
        Timber.v("list...%d", allItems.size());
        convertAndAddRows(query);
    }

    private Set<ApiItem> addChildPages(Collection subCollection) {

        Set<ApiItem> unfilteredPages = new HashSet<>();
        List<ApiItem> children = subCollection.getApiItems();

        unfilteredPages.addAll(children);

        //double for-loop here because using recursion got a stackoverflow... :(
        //kinda hack, there may be a better way to do this
        //only accounts for 2 level nesting
        for (ApiItem apiItem : children) {

            if ("collection".equals(apiItem.getType())) {
                List<ApiItem> grandkids = ((Collection) apiItem).getApiItems();
                unfilteredPages.addAll(grandkids);
//                for (ApiItem grandKid : grandkids) {
//                    unfilteredPages.add(grandKid);
//                }
            }
        }

        return unfilteredPages;
    }


    private void loadCollectionFromRepo(String query) {
        this.query = query;
        CollectionSpec spec = new CollectionSpec();
        subscription = repo.get(spec)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    private void convertAndAddRows(String query) {

        int numRows = 0;

        for (ApiItem apiItem:allItems) {

            if (apiItem.getTitle().toLowerCase().contains(query)) {

                if ("page".equals(apiItem.getType())) {
                    addRow(getPageRow((Page) apiItem, numRows));
                    numRows ++;
                } else if ("collection".equals(apiItem.getType())){
                    addRow(getCollectionRow((Collection) apiItem, numRows));
                    numRows ++;
                }
            }


            if (numRows >= 5) { break; }
        }
    }


    private Object[] getCollectionRow(Collection item, int id) {
        return new Object[]{id,
                item.getTitle(),
                R.drawable.ic_view_module_white_36dp,
                Actions.SEARCH_SELECTION_COLLECTION,
                item.getTitle() //SUGGEST_COLUMN_INTENT_EXTRA_DATA
        };
    }

    private Object[] getPageRow(Page page, int id) {
        return new Object[]{id,
                page.getTitle(),
                R.drawable.ic_file,
                Actions.SEARCH_SELECTION_PAGE,
                page.getTitle() //SUGGEST_COLUMN_INTENT_EXTRA_DATA
        };
    }


    Observer<Collection> observer = new Observer<Collection>() {
        @Override
        public void onCompleted() {
            parsePageList(SearchMatrixCursor.this.query);
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "onError in observer/rx");
        }

        @Override
        public void onNext(Collection col) {
            collection = col;
            Timber.v("... repo returned the collection");
        }
    };


}
