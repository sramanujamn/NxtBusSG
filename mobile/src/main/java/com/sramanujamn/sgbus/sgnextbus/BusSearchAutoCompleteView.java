package com.sramanujamn.sgbus.sgnextbus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;

public class BusSearchAutoCompleteView extends SearchView {

    public BusSearchAutoCompleteView(Context context) {
        super(context);
    }

    public BusSearchAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static class SearchAutoComplete extends SearchView.SearchAutoComplete {

        //@SuppressLint("RestrictedApi")
        //public SearchAutoComplete(Context context) {
          //  super(context);
        //}

        @SuppressLint("RestrictedApi")
        public SearchAutoComplete(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void performFiltering(CharSequence text, int keyCode) {
            String filterText = "";
            super.performFiltering(filterText, keyCode);
        }
    }

}
