package com.ice.bunchbead.android.listener.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class DataChildListener implements ChildEventListener {

    private EventResult.Complete mEventChildAdded;
    private EventResult.Complete mEventChildChanged;
    private EventResult.CompleteNoPrevious mEventChildRemoved;
    private EventResult.Complete mEventChildMoved;
    private EventResult.Error mEventCanceled;

    public DataChildListener(@Nullable EventResult.Complete mEventChildAdded,
                             @Nullable EventResult.Complete mEventChildChanged,
                             @Nullable EventResult.CompleteNoPrevious mEventChildRemoved,
                             @Nullable EventResult.Complete mEventChildMoved,
                             @Nullable EventResult.Error mEventCanceled) {
        this.mEventChildAdded = mEventChildAdded;
        this.mEventChildChanged = mEventChildChanged;
        this.mEventChildRemoved = mEventChildRemoved;
        this.mEventChildMoved = mEventChildMoved;
        this.mEventCanceled = mEventCanceled;
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (mEventChildAdded != null) {
            mEventChildAdded.result(dataSnapshot, s);
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (mEventChildChanged != null) {
            mEventChildChanged.result(dataSnapshot, s);
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        if (mEventChildRemoved != null) {
            mEventChildRemoved.result(dataSnapshot);
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (mEventChildMoved != null) {
            mEventChildMoved.result(dataSnapshot, s);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        if (mEventCanceled != null) {
            mEventCanceled.result(databaseError);
        }
    }
}
