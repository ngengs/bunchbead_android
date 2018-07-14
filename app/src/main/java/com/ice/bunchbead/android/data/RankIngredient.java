package com.ice.bunchbead.android.data;

/**
 * Created by rizky Kharisma on 14/07/18.
 */
public class RankIngredient {
    private String id;
    private String nama;
    private boolean processed;
    private String rankId;
    private String key;
    private Double min;
    private Double sisa;

    public RankIngredient() {
        // Create empty constructor
    }

    public RankIngredient(String id, String nama, boolean processed, String rankId, String key, Double min, Double sisa) {
        this.id = id;
        this.nama = nama;
        this.processed = processed;
        this.rankId = rankId;
        this.key = key;
        this.min = min;
        this.sisa = sisa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getSisa() {
        return sisa;
    }

    public void setSisa(Double sisa) {
        this.sisa = sisa;
    }

    @Override
    public String toString() {
        return "RankIngredient{" +
                "id='" + id + '\'' +
                ", nama='" + nama + '\'' +
                ", processed=" + processed +
                ", rankId=" + rankId +
                ", key=" + key +
                ", min=" + min +
                ", sisa=" + sisa +
                '}';
    }
}
