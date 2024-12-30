package JDBC;

import java.sql.*;

public class JDBC {

    public Connection con;
    Statement stmt;
    public boolean isConnected;
    public String message;

    public JDBC() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_barang", "root", "");
            stmt = con.createStatement();
            isConnected = true;
            message = "DB connected";
        } catch (Exception e) {
            isConnected = false;
            message = e.getMessage();
        }
    }

    public void disconnect() {
        try {
            stmt.close();
            con.close();
            message = "DB disconnected";
        } catch (Exception e) {
            message = e.getMessage();
        }
    }

    public void runQuery(String query) {
        try {
            int result = stmt.executeUpdate(query);
            message = "info: " + result + " rows affected";
        } catch (Exception e) {
            message = e.getMessage();
        }
    }

    public ResultSet getData(String query) {
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(query);
        } catch (Exception e) {
            message = e.getMessage();
        }
        return rs;
    }
}
