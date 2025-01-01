/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author LENOVO
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InventarisBarang extends Model<InventarisBarang> {
    private String kodeBarang;
    private String namaBarang;
    private int stok;
    private double hargaBeli;
    private double hargaJual;
    private LocalDate tanggalMasuk;

    public InventarisBarang() {
        this.table = "inventarisbarang";
        this.primaryKey = "kodeBarang";
    }

    public InventarisBarang(String kodeBarang, String namaBarang, int stok, double hargaBeli, double hargaJual, LocalDate tanggalMasuk) {
        this.table = "inventarisbarang";
        this.primaryKey = "kodeBarang";
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.stok = stok;
        this.hargaBeli = hargaBeli;
        this.hargaJual = hargaJual;
        this.tanggalMasuk = tanggalMasuk;
    }

    @Override
    public InventarisBarang toModel(ResultSet rs) {
        try {
            return new InventarisBarang(
                rs.getString("kodeBarang"),
                rs.getString("namaBarang"),
                rs.getInt("stok"),
                rs.getDouble("hargaBeli"),
                rs.getDouble("hargaJual"),
                rs.getDate("tanggalMasuk").toLocalDate()
            );
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public double getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(double hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public LocalDate getTanggalMasuk() {
        return tanggalMasuk;
    }

    public void setTanggalMasuk(LocalDate tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }
}

