package com.ice.bunchbead.android.listener.search;

public interface EventResult {
    interface Search {
        /**
         * Event result
         *
         * @param text Query text
         */
        boolean result(String text);
    }
}
