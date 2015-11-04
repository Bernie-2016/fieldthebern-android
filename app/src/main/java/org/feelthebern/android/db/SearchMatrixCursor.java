package org.feelthebern.android.db;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class SearchMatrixCursor extends MatrixCursor
{


    public static final String[] 	COLUMNS	 		= {	BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_ICON_1,
            SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
            SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA

    };

    private static final long		MAX_RESULTS		= 5;


    @SuppressWarnings("unused")
    private final Context	c;

    static Collection collection;
    Subscription subscription;
    public static List<Page> allPages;

    @Inject
    CollectionRepo repo;

    public SearchMatrixCursor( String[] columnNames, Context c )
    {
        super ( COLUMNS );
        this.c = c;


    }



    public Cursor getSearchSuggestionCursor( String query )
    {
        if (collection==null) {
            FTBApplication.getComponent().inject(this);
            loadCollectionFromRepo();
            return this;
        } else {
            parsePageList();
        }

        //List<Page>   	filteredList	= getPermissions(query);
        convertAndAddRows(query);
        return this;
    }

    private void parsePageList() {

        //List<Page> unfilteredPages;
        allPages = new ArrayList<>();
        allPages.addAll(addChildPages(collection));
        Timber.v("list...%d", allPages.size());
    }

    private List<Page> addChildPages(Collection subCollection){

        List<Page> unfilteredPages = new ArrayList<>();
        List<ApiItem> children = subCollection.getApiItems();

        for (ApiItem apiItem : children) {
            if("collection".equals(apiItem.getType())) {
                List<ApiItem> grandkids = ((Collection)apiItem).getApiItems();

                for (ApiItem grandKid : grandkids) {
                    if("page".equals(grandKid.getType())) {
                        unfilteredPages.add((Page) grandKid);
                    }
                }
            } else {
                unfilteredPages.add((Page) apiItem);
            }
        }

        return unfilteredPages;
    }



    private void loadCollectionFromRepo() {
        CollectionSpec spec = new CollectionSpec();
        subscription = repo.get(spec)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    private void convertAndAddRows( String query )
    {
        int 			id 		= 0;

        for(Page page : allPages)
        {
            if (page.getTitle().toLowerCase().contains(query)) {
                Object[] 		row 	= {	id,
                        page.getTitle(),
                        R.drawable.ic_file,
                        Actions.SEARCH_SELECTION,
                        page.getTitle() //SUGGEST_COLUMN_INTENT_EXTRA_DATA
                };

                this.addRow ( row );

                id++;

                if (id > 5) {
                    break;
                }
            }
        }
    }




    Observer<Collection> observer = new Observer<Collection>() {
        @Override
        public void onCompleted() {
            parsePageList();
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

//	private ArrayList<Object> getVOs (String constraint )
//	{
//		ArrayList<Object>   	filteredList 		= new ArrayList<Object>();
//
//		if(constraint != null && constraint.toString().length() > 0)
//		{
//			String 				constraintString 	= constraint.toString();
//			Matcher 			m 					= Pattern.compile(Pattern.quote( constraintString.trim() ), Pattern.CASE_INSENSITIVE ).matcher("");
//			int 				size 				= localList.size();
//			int					maxResults			= MAX_RESULTS;
//			int 				numMatches 			= 0;
//
//			for(int i = 0; i < size; i++)
//			{
//				PermissionObj 	pvo 				= (PermissionObj) localList.get(i);
//				String 			stringToSearch 		= StringUtil.getSearchStrFromPrmObj( pvo );
//
//				m.reset( stringToSearch	);
//
//				if ( m.find() )
//				{
//					filteredList.add ( pvo );
//					numMatches++;
//				}
//
//				if (numMatches >= maxResults )
//				{
//					break;
//				}
//			}
//		}
//		return filteredList;
//	}








}
