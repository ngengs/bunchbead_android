package com.ice.bunchbead.android.helpers;

import com.google.firebase.database.DataSnapshot;
import com.ice.bunchbead.android.data.Rank;
import com.ice.bunchbead.android.data.RankIngredient;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class RankHelper {
    public static Rank convertDataSnapshot(DataSnapshot dataSnapshot) {
        Rank data = new Rank();
        String timeStamp = dataSnapshot.child("timestamp").getValue(String.class);
        data.setTimeStamp(timeStamp);
        String id = dataSnapshot.getKey();
        data.setId(id);
        for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
            if (dataSnapshot.hasChild(String.valueOf(i))) {
                RankIngredient rankIngredient = dataSnapshot.child(String.valueOf(i)).getValue(RankIngredient.class);
                if (rankIngredient != null) {
                    rankIngredient.setRankId(id);
                    rankIngredient.setKey(String.valueOf(i));
                    data.addIngredients(rankIngredient);
                }
            }
        }

        return data;
    }
}
