package com.berniesanders.canvass.mortar;

/**
 * Uses the @Layout annotation to build a layout for a mortar/flow screen
 */

import android.content.Context;
import android.view.LayoutInflater;

import com.berniesanders.canvass.annotations.Layout;

public final class LayoutFactory {

    /** Create an instance of the view specified in a {@link Layout} annotation. */
    public static android.view.View createView(Context context, Object screen) {
        return createView(context, screen.getClass());
    }

    /** Create an instance of the view specified in a {@link Layout} annotation. */
    public static android.view.View createView(Context context, Class<?> screenType) {
        Layout screen = screenType.getAnnotation(Layout.class);
        if (screen == null) {
            throw new IllegalArgumentException(
                    String.format("@%s annotation not found on class %s",
                            Layout.class.getSimpleName(),
                            screenType.getName()));
        }

        int layout = screen.value();
        return inflateLayout(context, layout);
    }

    private static android.view.View inflateLayout(Context context, int layoutId) {
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    private LayoutFactory() {
    }
}
