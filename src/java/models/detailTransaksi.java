/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class detailTransaksi tanpa bergantung pada ModelTransaksi
 */
public class detailTransaksi {

    private String id;
    private String barangID;
    private int jumlah;
    private double harga;
    private String table;
    private String primary;
    private Connection con;
    private String message;

    public detailTransaksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_barang", "root", "");
            message = "Database connected.";
        } catch (ClassNotFoundException | SQLException e) {
            message = e.getMessage();
        }
        this.table = "detail_transaksi";
        this.primary = "id";
    }

    public detailTransaksi(String id, String barang, int jumlah, double harga) {
        this.id = id;
        this.barangID = barang;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    // Simpan detail transaksi ke database
    public void simpanDetail(Connection con, String transaksiId) throws SQLException {
        String query = "INSERT INTO " + table + " (transaksi_id, barang, jumlah, harga) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, transaksiId);
            ps.setString(2, barangID);
            ps.setInt(3, jumlah);
            ps.setDouble(4, harga);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Operkan error jika terjadi
        }
    }

    // Mendapatkan semua detail transaksi berdasarkan transaksiId
    public ArrayList<detailTransaksi> getDetailsByTransaksiId(String transaksiId) {
        ArrayList<detailTransaksi> details = new ArrayList<>();
        String query = "SELECT * FROM detail_transaksi WHERE transaksi_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, transaksiId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detailTransaksi detail = new detailTransaksi(
                            rs.getString("id"),
                            rs.getString("barang"),
                            rs.getInt("jumlah"),
                            rs.getDouble("harga")
                    );
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(detailTransaksi.class.getName()).log(Level.SEVERE, null, e);
        }
        return details;
    }

    // Mengubah ResultSet menjadi objek detailTransaksi
    public detailTransaksi toModel(ResultSet rs) {
        try {
            return new detailTransaksi(
                    rs.getString("id"),
                    rs.getString("barang"),
                    rs.getInt("jumlah"),
                    rs.getDouble("harga")
            );
        } catch (SQLException e) {
            Logger.getLogger(detailTransaksi.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getBarangID() {
        return barangID;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double getHarga() {
        return harga;
    }

    public void setBarangID(String barang) {
        this.barangID = barang;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
