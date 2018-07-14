package com.ice.bunchbead.android.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class Rank {
    private String id;
    private List<RankIngredient> ingredients;
    private String timeStamp;

    public Rank() {

    }

    public Rank(String id, List<RankIngredient> ingredients, String timeStamp) {
        this.id = id;
        this.ingredients = ingredients;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RankIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RankIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredients(RankIngredient ingredient) {
        if (ingredients == null) ingredients = new ArrayList<>();
        ingredients.add(ingredient);
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "ingredients=" + ingredients +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
