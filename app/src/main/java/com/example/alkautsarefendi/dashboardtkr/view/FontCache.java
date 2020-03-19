package com.example.alkautsarefendi.dashboardtkr.view;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Akautsar Efendi on 11/7/2017.
 */

public class FontCache {
    private static HashMap<String, Typeface> fontMap = new HashMap<>();

    public static Typeface getTypeface(String fontName, Context context){
        if (fontMap.containsKey(fontName)){
            return fontMap.get(fontName);
        } else {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Glam_Queen.tff" +fontName);
            fontMap.put(fontName, tf);
            return tf;
        }
    }
}
