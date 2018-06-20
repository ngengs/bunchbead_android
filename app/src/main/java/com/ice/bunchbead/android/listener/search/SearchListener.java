package com.ice.bunchbead.android.listener.search;

import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;

public class SearchListener implements SearchView.OnQueryTextListener {

    private EventResult.Search mEventSubmit;
    private EventResult.Search mEventTextChange;

    public SearchListener(@Nullable EventResult.Search mEventSubmit,
                          @Nullable EventResult.Search mEventTextChange) {
        this.mEventSubmit = mEventSubmit;
        this.mEventTextChange = mEventTextChange;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return mEventSubmit != null && mEventSubmit.result(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return mEventTextChange != null && mEventTextChange.result(newText);
    }
}
