package KasirClass;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Kasir extends ModelKasir<Kasir> {
    private String kodeBarang;
    private String name;
    private double price;

    public Kasir() {
        this.table = "inventarisbarang";  // Menggunakan tabel inventarisbarang
        this.primaryKey = "kodebarang";   // Primary key adalah kodeBarang
    }
    
    public Kasir(String kodeBarang, String name, double price) {
        this.table = "inventarisbarang";  // Pastikan menggunakan tabel inventarisbarang
        this.primaryKey = "kodebarang";
        this.kodeBarang = kodeBarang;
        this.name = name;
        this.price = price;
    }

    @Override
    public Kasir toModel(ResultSet rs) {
        try {
            return new Kasir(
                rs.getString("kodebarang"),
                rs.getString("namabarang"),
                rs.getDouble("hargabeli")
            );
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Getter dan Setter
    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Menambahkan fungsi pencarian barang berdasarkan kodeBarang
    public Kasir findProductByCode(String kodeBarang) {
        try {
            return find(kodeBarang);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
