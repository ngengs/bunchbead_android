package com.ice.bunchbead.android.listener.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DataSingleListener implements ValueEventListener {

    private EventResult.CompleteNoPrevious mEventSuccess;
    private EventResult.Error mEventCanceled;

    public DataSingleListener(@Nullable EventResult.CompleteNoPrevious mEventSuccess,
                              @Nullable EventResult.Error mEventCanceled) {
        this.mEventSuccess = mEventSuccess;
        this.mEventCanceled = mEventCanceled;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (mEventSuccess != null) {
            mEventSuccess.result(dataSnapshot);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        if (mEventCanceled != null) {
            mEventCanceled.result(databaseError);
        }
    }
}
