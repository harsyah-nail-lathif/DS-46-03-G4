/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TransaksiClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class detailTransaksi tanpa bergantung pada ModelTransaksi
 */
public class detailTransaksi {
    private int id;
    private String barangID;
    private int jumlah;
    private double harga;
    private final String table = "detail_transaksi";
    private Connection con;
    private String message;

    public detailTransaksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minimarket", "root", "");
            message = "Database connected.";
        } catch (ClassNotFoundException | SQLException e) {
            message = e.getMessage();
        }
    }

    public detailTransaksi(int id, String barang, int jumlah, double harga) {
        this.id = id;
        this.barangID = barang;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    // Simpan detail transaksi ke database
    public void simpanDetail(int transaksiId) {
        String query = "INSERT INTO " + table + " (transaksi_id, barang, jumlah, harga) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, transaksiId);
            ps.setString(2, barangID);
            ps.setInt(3, jumlah);
            ps.setDouble(4, harga);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(detailTransaksi.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // Mendapatkan semua detail transaksi berdasarkan transaksiId
    public ArrayList<detailTransaksi> getDetailsByTransaksiId(int transaksiId) {
        ArrayList<detailTransaksi> details = new ArrayList<>();
        String query = "SELECT * FROM detail_transaksi WHERE transaksi_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, transaksiId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detailTransaksi detail = new detailTransaksi(
                        rs.getInt("id"),
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
                rs.getInt("id"),
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
    public int getId() {
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

