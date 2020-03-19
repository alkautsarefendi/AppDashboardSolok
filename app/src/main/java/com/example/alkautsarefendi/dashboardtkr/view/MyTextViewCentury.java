package com.example.alkautsarefendi.dashboardtkr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.alkautsarefendi.dashboardtkr.R;

/**
 * Created by Akautsar Efendi on 11/7/2017.
 */

public class MyTextViewCentury extends android.support.v7.widget.AppCompatTextView {
    public MyTextViewCentury(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs){
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        String fontName = attributeArray.getString(R.styleable.MyTextView_fontText);
        Typeface customFont = selectTypeface(context, fontName);
        setTypeface(customFont);

        attributeArray.recycle();
    }

    private Typeface selectTypeface(Context context, String fontName){
        try {
            return FontCache.getTypeface(fontName, context);
        }catch (Exception e){
            return null;
        }
    }

}
