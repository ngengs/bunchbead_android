package com.ice.bunchbead.android.listener.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class SimpleTextChangeListener implements TextWatcher {

    private OnSimpleTextChange mEvent;

    public SimpleTextChangeListener(OnSimpleTextChange mEvent) {
        this.mEvent = mEvent;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No need implemented
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // No need implemented
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mEvent != null) {
            mEvent.result(s.toString());
        }
    }


    public interface OnSimpleTextChange {
        void result(String text);
    }
}
