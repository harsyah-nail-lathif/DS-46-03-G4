package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class LaporanPenjualan untuk mengelola laporan penjualan harian, mingguan, dan bulanan.
 */
public class LaporanPenjualan {

    private String periode;
    private final List<transaksi> transaksiList = new ArrayList<>();
    private final List<transaksi> laporanHarian = new ArrayList<>();
    private final List<transaksi> laporanMingguan = new ArrayList<>();
    private final List<transaksi> laporanBulanan = new ArrayList<>();

    private final String table;
    private final String primaryKey;
    private Connection con;
    private String message;

    public LaporanPenjualan() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_barang", "root", "");
            message = "Database connected.";
        } catch (ClassNotFoundException | SQLException e) {
            message = e.getMessage();
        }
        this.table = "transaksi";
        this.primaryKey = "id";
    }

    // Generate laporan harian
    public void generateLaporanHarian() {
        String query = "SELECT * FROM transaksi WHERE DATE(tanggal_transaksi) = CURRENT_DATE";
        try{
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            laporanHarian.clear();
            while (rs.next()) {
                laporanHarian.add(mapResultSetToTransaksi(rs));
            }
            //logger.log(Level.INFO, "Generated daily report with {0} transactions", laporanHarian.size());
        } catch (SQLException e) {
            //logger.log(Level.SEVERE, "Error generating daily report", e);
            throw new RuntimeException("Error generating daily report", e);
            
        }
    }

    // Generate laporan mingguan
    public void generateLaporanMingguan() {
        String query = "SELECT * FROM transaksi WHERE tanggal_transaksi >= DATE_SUB(CURRENT_DATE(), INTERVAL 7 DAY)";
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            laporanMingguan.clear();
            while (rs.next()) {
                laporanMingguan.add(mapResultSetToTransaksi(rs));
            }
            //logger.log(Level.INFO, "Generated weekly report with {0} transactions", laporanMingguan.size());
        } catch (SQLException e) {
            //logger.log(Level.SEVERE, "Error generating weekly report", e);
            throw new RuntimeException("Error generating weekly report", e);
        }
    }

    // Generate laporan bulanan
    public void generateLaporanBulanan() {
        String query = "SELECT * FROM transaksi WHERE YEAR(tanggal_transaksi) = YEAR(CURRENT_DATE()) " +
                       "AND MONTH(tanggal_transaksi) = MONTH(CURRENT_DATE())";
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            laporanBulanan.clear();
            while (rs.next()) {
                laporanBulanan.add(mapResultSetToTransaksi(rs));
            }
            //logger.log(Level.INFO, "Generated monthly report with {0} transactions", laporanBulanan.size());
        } catch (SQLException e) {
            //logger.log(Level.SEVERE, "Error generating monthly report", e);
            throw new RuntimeException("Error generating monthly report", e);
        }
    }

    // Barang terlaris
    public void barangTerlaris() {
        String query = "SELECT kode_barang, nama_barang, SUM(jumlah) as total_terjual, " +
                       "SUM(total_harga) as total_penjualan FROM detail_transaksi " +
                       "GROUP BY kode_barang, nama_barang ORDER BY total_terjual DESC LIMIT 10";
        try (PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            transaksiList.clear();
            while (rs.next()) {
                transaksi t = new transaksi();
                t.setId(rs.getString("kode_barang"));
                t.setTotalHarga(rs.getDouble("total_penjualan"));
                transaksiList.add(t);
            }
            //logger.log(Level.INFO, "Generated best-selling items report");
        } catch (SQLException e) {
            //logger.log(Level.SEVERE, "Error generating best-selling items report", e);
            throw new RuntimeException("Error generating best-selling items report", e);
        }
    }

    // Map ResultSet ke transaksi
    private transaksi mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        return new transaksi(
                rs.getString("id"),
                rs.getDate("tanggal_transaksi"),
                rs.getDouble("total_harga"),
                rs.getString("kasir_id")
        );
    }

    // Getters and setters
    public String getMessage() {return message; }
    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }
    public List<transaksi> getTransaksiList() { return transaksiList; }
    public List<transaksi> getLaporanHarian() { return laporanHarian; }
    public List<transaksi> getLaporanMingguan() { return laporanMingguan; }
    public List<transaksi> getLaporanBulanan() { return laporanBulanan; }
}
