package org.feelthebern.android.mortar;

import android.content.Context;

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
    return childScope;
  }
}
