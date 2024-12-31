package KasirClass;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Kasir extends ModelKasir<Kasir> {

    private String kodeBarang;
    private String name;
    private double price;

    public Kasir() {
        // (#1.2) Sesuaikan nilai table dan primaryKey
        this.table = "inventarisbarang";
        this.primaryKey = "kodeBarang";
    }

    public Kasir(String kodeBarang, String name, double price) {
        // (#1.3) Sesuaikan nilai table dan primaryKey serta konstruktor dari parameter yang telah diberikan
        this.table = "product";
        this.primaryKey = "id";
        this.kodeBarang = kodeBarang;
        this.name = name;
        this.price = price;
    }

    @Override
    public Kasir toModel(ResultSet rs) {
        try {
            return new Kasir(
                    // (#1.4) Lakukan get resultSet dari tiap parameter kolom yang ada
                    rs.getString("kodeBarang"),
                    rs.getString("namaBarang"),
                    rs.getDouble("hargajual")
            );
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public String getId() {
        return this.kodeBarang;
    }

    public void setId(String kodeBarang) {
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

    public Kasir findByKodeBarang(String kodeBarang) {
        return this.find(kodeBarang);
    }

}
