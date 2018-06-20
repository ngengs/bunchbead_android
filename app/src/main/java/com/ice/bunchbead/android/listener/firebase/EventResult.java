package com.ice.bunchbead.android.listener.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface EventResult {

    interface Complete {
        /**
         * Event result
         *
         * @param dataSnapshot      An immutable snapshot of the data at the new data at the child location
         * @param previousChildName The key name of sibling location ordered before the child.
         *                          This will be null for the first child node of a location.
         *                          Or The key name of sibling location ordered before the new child.
         *                          This will be null for the first child node of a location.
         */
        void result(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName);
    }

    interface CompleteNoPrevious {
        /**
         * Event result
         *
         * @param dataSnapshot An immutable snapshot of the data at the new data at the child location
         */
        void result(@NonNull DataSnapshot dataSnapshot);
    }

    interface Error {
        void result(@NonNull DatabaseError databaseError);
    }

}
