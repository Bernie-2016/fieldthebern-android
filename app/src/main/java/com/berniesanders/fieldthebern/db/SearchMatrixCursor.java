/*
 * Copyright (c) 2016 - Bernie 2016, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.berniesanders.fieldthebern.db;

import android.app.SearchManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import com.berniesanders.fieldthebern.FTBApplication;
import com.berniesanders.fieldthebern.R;
import com.berniesanders.fieldthebern.config.Actions;
import com.berniesanders.fieldthebern.models.ApiItem;
import com.berniesanders.fieldthebern.models.Collection;
import com.berniesanders.fieldthebern.models.Page;
import com.berniesanders.fieldthebern.repositories.CollectionRepo;
import com.berniesanders.fieldthebern.repositories.specs.CollectionSpec;
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

  public static final String[] COLUMNS = {
      BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_ICON_1,
      SearchManager.SUGGEST_COLUMN_INTENT_ACTION, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
  };

  private static final long MAX_RESULTS = 5;

  static Collection collection;
  Subscription subscription;
  public static Set<ApiItem> allItems;

  @Inject
  CollectionRepo repo;
  private String query;

  public SearchMatrixCursor() {
    super(COLUMNS);
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

    if (query == null) {
      return;
    }

    int numRows = 0;

    for (ApiItem apiItem : allItems) {

      if (apiItem.getTitle().toLowerCase().contains(query.toLowerCase().trim())) {

        if ("page".equals(apiItem.getType())) {
          addRow(getPageRow((Page) apiItem, numRows));
          numRows++;
        } else if ("collection".equals(apiItem.getType())) {
          addRow(getCollectionRow((Collection) apiItem, numRows));
          numRows++;
        }
      }

      if (numRows >= 5) {
        break;
      }
    }
  }

  private Object[] getCollectionRow(Collection item, int id) {
    return new Object[] {
        id, item.getTitle(), R.drawable.ic_view_module_white_36dp,
        Actions.SEARCH_SELECTION_COLLECTION, item.getTitle() //SUGGEST_COLUMN_INTENT_EXTRA_DATA
    };
  }

  private Object[] getPageRow(Page page, int id) {
    return new Object[] {
        id, page.getTitle(), R.drawable.ic_file, Actions.SEARCH_SELECTION_PAGE, page.getTitle()
        //SUGGEST_COLUMN_INTENT_EXTRA_DATA
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
