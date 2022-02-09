package com.revaki.revakipos.helper;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class FontTypeface {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public static Typeface get(Context context, String name) {
        Typeface typeface = fontCache.get(name);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, typeface);
        }
        return typeface;
    }
}
