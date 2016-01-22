/*
 * Copyright 2014 Square Inc.
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
 */

package com.berniesanders.fieldthebern.mortar;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import flow.path.Path;
import flow.path.PathContextFactory;
import mortar.MortarScope;

public final class MortarContextFactory implements PathContextFactory {
  private final ScreenScoper screenScoper = new ScreenScoper();

  public MortarContextFactory() {
  }

  @Override public Context setUpContext(Path path, Context parentContext) {
    MortarScope screenScope =
        screenScoper.getScreenScope(parentContext, ((FlowPathBase) path).getScopeName(), path);
    return new TearDownContext(parentContext, screenScope);
  }

  @Override public void tearDownContext(Context context) {
    TearDownContext.destroyScope(context);
  }

  static class TearDownContext extends ContextWrapper {
    private static final String SERVICE = "SNEAKY_MORTAR_PARENT_HOOK";
    private final MortarScope parentScope;
    private LayoutInflater inflater;

    static void destroyScope(Context context) {
      MortarScope.getScope(context).destroy();
    }

    public TearDownContext(Context context, MortarScope scope) {
      super(scope.createContext(context));
      this.parentScope = MortarScope.getScope(context);
    }

    @Override public Object getSystemService(String name) {
      if (LAYOUT_INFLATER_SERVICE.equals(name)) {
        if (inflater == null) {
          inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
        }
        return inflater;
      }

      if (SERVICE.equals(name)) {
        return parentScope;
      }

      return super.getSystemService(name);
    }
  }
}
