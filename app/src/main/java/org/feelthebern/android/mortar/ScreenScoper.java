package org.feelthebern.android.mortar;

import android.content.Context;

import flow.Flow;
import mortar.MortarScope;


public class ScreenScoper {
  public MortarScope getScreenScope(Context context, String name, Object screen) {
    MortarScope parentScope = MortarScope.getScope(context);
    return getScreenScope(parentScope, name, screen);
  }

  /**
   * Finds or creates the scope for the given screen.
   */
  public MortarScope getScreenScope(MortarScope parentScope, final String name, final Object screen) {
    MortarScope childScope = parentScope.findChild(name);
    if (childScope == null) {
      FlowPathBase basePath = (FlowPathBase) screen;
      childScope = parentScope.buildChild()
              .withService(DaggerService.DAGGER_SERVICE, basePath.createComponent())
              .build(name);
    }



//    if (childScope != null) {
//      childScope.destroy();
//    }
//
//    // this hack kills Path's ability to save state but allows search to work.
//    // before, the component was only created if it didn't exist, and was not destroyed above
//    // TODO: see if we can save scroll position some other way so we can leave this hack in
//    FlowPathBase basePath = (FlowPathBase) screen;
//    childScope = parentScope.buildChild()
//            .withService(DaggerService.DAGGER_SERVICE, basePath.createComponent())
//            .build(name);
    return childScope;
  }
}
