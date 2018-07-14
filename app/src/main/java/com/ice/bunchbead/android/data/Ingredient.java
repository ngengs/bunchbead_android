package com.ice.bunchbead.android.data;

public class Ingredient {
    private String id;
    private Double min;
    private Double sisa;
    private String satuan;
    private String nama;
    private Long harga;

    public Ingredient() {
        // Emoty Constructor
    }

    public Ingredient(Double min, Double sisa, String satuan, String nama, Long harga) {
        this.min = min;
        this.sisa = sisa;
        this.satuan = satuan;
        this.nama = nama;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getHarga() {
        return harga;
    }

    public void setHarga(Long harga) {
        this.harga = harga;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id='" + id + '\'' +
                ", min='" + min + '\'' +
                ", sisa='" + sisa + '\'' +
                ", satuan='" + satuan + '\'' +
                ", nama='" + nama + '\'' +
                ", harga='" + harga + '\'' +
                '}';
    }
}
