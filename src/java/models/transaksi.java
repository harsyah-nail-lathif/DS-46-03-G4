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
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_barang", "root", "");
            message = "Database connected.";
        } catch (ClassNotFoundException | SQLException e) {
            message = e.getMessage();
        }
        this.table = "transaksi";
        this.primaryKey = "id";
        this.detailTransaksiList = new ArrayList<>();
    }

    public transaksi(String id, Date tanggalTransaksi, double totalHarga, String kasirID) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_barang", "root", "");
            message = "Database connected.";
        } catch (ClassNotFoundException | SQLException e) {
            message = e.getMessage();
        }
        this.table = "transaksi";
        this.primaryKey = "id";
        this.detailTransaksiList = new ArrayList<>();
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
    public void simpanTransaksi() throws Exception {
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_barang", "root", ""); // Pastikan koneksi tersedia
    try {
        //con.setAutoCommit(false); // Disable autocommit to manage transactions manually

        // Simpan transaksi utama
        String insertTransaksi = "INSERT INTO transaksi (id, tanggal_transaksi, total_harga, kasir_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(insertTransaksi, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, this.id);
        pstmt.setDate(2, this.tanggalTransaksi);
        pstmt.setDouble(3, this.totalHarga);
        pstmt.setString(4, this.kasirID);
        pstmt.executeUpdate();
        
        if(detailTransaksiList.isEmpty()){
            message = "Array detail transaksi kosong";
        }else{
            for (detailTransaksi detail : detailTransaksiList) {
                detail.simpanDetail();
            }
        }
        
        //con.commit(); // Commit transaction
    } catch (SQLException e) {
        //con.rollback(); // Rollback jika terjadi kesalahan
        message = "Gagal menyimpan transaksi: " + e.getMessage();
        throw new Exception("Gagal menyimpan transaksi: " + e.getMessage());
    } finally {
        con.setAutoCommit(true); // Set autocommit back to true
        con.close(); // Pastikan koneksi ditutup
    }
}



    // Mendapatkan semua transaksi dari database
    public ArrayList<transaksi> getAll() {
        ArrayList<transaksi> transaksiList = new ArrayList<>();
        String query = "SELECT * FROM transaksi";
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
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
    public String getMaxId() {
        String query = "SELECT MAX(id) FROM transaksi";
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString(1);
                int nextId = Integer.parseInt(lastId.replaceAll("\\D", "")) + 1;
                return String.format("TR%03d", nextId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "TR001"; // Jika tabel kosong, mulai dari TR001
    }
    
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

    public void setTanggalTransaksi(java.sql.Date tanggalTransaksi) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
