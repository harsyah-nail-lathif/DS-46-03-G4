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
 * Class transaksi tanpa bergantung pada ModelTransaksi
 */
public class transaksi {
    private String id;
    private Date tanggalTransaksi;
    private double totalHarga;
    private String kasirID;
    private ArrayList<detailTransaksi> detailTransaksiList;
    private final String table;
    private final String primaryKey;
    private Connection con;
    private String message;

    public transaksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/minimarket", "root", "");
            message = "Database connected.";
        } catch (ClassNotFoundException | SQLException e) {
            message = e.getMessage();
        }
        this.table = "transaksi";
        this.primaryKey = "id";
        this.detailTransaksiList = new ArrayList<>();
    }

    public transaksi(String id, Date tanggalTransaksi, double totalHarga, String kasirID) {
        this();
        this.id = id;
        this.tanggalTransaksi = tanggalTransaksi;
        this.totalHarga = totalHarga;
        this.kasirID = kasirID;
    }

    // Menambahkan detail transaksi
    public void tambahDetailTransaksi(detailTransaksi detail) {
        this.detailTransaksiList.add(detail);
    }

    // Simpan transaksi utama dan detail ke database
    public void simpanTransaksi(Connection connection) throws SQLException {
        String insertQuery = "INSERT INTO " + table + " (tanggal_transaksi, total_harga, kasir_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, tanggalTransaksi);
            ps.setDouble(2, totalHarga);
            ps.setString(3, kasirID);
            ps.executeUpdate();

            // Mendapatkan ID transaksi yang baru disimpan
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getString(1);
                }
            }
        }

        // Simpan semua detail transaksi
        for (detailTransaksi detail : detailTransaksiList) {
            detail.simpanDetail(id);
        }
    }

    // Mendapatkan semua transaksi dari database
    public ArrayList<transaksi> getAll() {
        ArrayList<transaksi> transaksiList = new ArrayList<>();
        String query = "SELECT * FROM transaksi";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                transaksi tr = new transaksi(
                        rs.getString("id"),
                        rs.getDate("tanggal_transaksi"),
                        rs.getDouble("total_harga"),
                        rs.getString("kasir_id")
                );
                transaksiList.add(tr);
            }
        } catch (SQLException e) {
            Logger.getLogger(transaksi.class.getName()).log(Level.SEVERE, null, e);
        }
        return transaksiList;
    }

    // Mengubah ResultSet menjadi objek transaksi
    public transaksi toModel(ResultSet rs) {
        try {
            return new transaksi(
                rs.getString("id"),
                rs.getDate("tanggal_transaksi"),
                rs.getDouble("total_harga"),
                rs.getString("kasir_id")
            );
        } catch (SQLException e) {
            Logger.getLogger(transaksi.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public Date getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public String getKasirID() {
        return kasirID;
    }

    public ArrayList<detailTransaksi> getDetailTransaksiList() {
        return detailTransaksiList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTanggalTransaksi(Date tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public void setKasirID(String kasirID) {
        this.kasirID = kasirID;
    }

    public void setDetailTransaksiList(ArrayList<detailTransaksi> detailTransaksiList) {
        this.detailTransaksiList = detailTransaksiList;
    }
}
