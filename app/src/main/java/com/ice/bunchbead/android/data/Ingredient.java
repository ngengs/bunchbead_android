package com.ice.bunchbead.android.data;

public class Ingredient {
    private String id;
    private String min;
    private String sisa;
    private String satuan;
    private String nama;

    public Ingredient() {
        // Emoty Constructor
    }

    public Ingredient(String min, String sisa, String satuan, String nama) {
        this.min = min;
        this.sisa = sisa;
        this.satuan = satuan;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getSisa() {
        return sisa;
    }

    public void setSisa(String sisa) {
        this.sisa = sisa;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id='" + id + '\'' +
                ", min='" + min + '\'' +
                ", sisa='" + sisa + '\'' +
                ", satuan='" + satuan + '\'' +
                ", nama='" + nama + '\'' +
                '}';
    }
}
