package com.sramanujamn.sgbus.sgnextbus;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;

public class BusAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    BusAutoCompleteTextView(Context context) {
        super(context);
    }

    BusAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }
}
