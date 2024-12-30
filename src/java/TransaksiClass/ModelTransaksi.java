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
 *
 * @author jihad
 * @param <E>
 */
public abstract class ModelTransaksi<E>{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/minimarket";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    private Connection con;
    private boolean isConnected;
    private String message;
    protected String table;
    protected String primaryKey;
    protected String select = "*";
    private String where = "";
    private String join = "";
    private String otherQuery = "";

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            isConnected = true;
            message = "Database connected.";
        } catch (ClassNotFoundException | SQLException e) {
            isConnected = false;
            message = e.getMessage();
        }
    }

    public void disconnect() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            message = e.getMessage();
        }
    }

    public void insert() {
        String cols = "", values = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            for (var field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                if (value != null) {
                    cols += field.getName() + ", ";
                    values += "?, ";
                }
            }
            String query = "INSERT INTO " + table + "(" + cols.substring(0, cols.length() - 2) + ") VALUES (" + values.substring(0, values.length() - 2) + ")";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                int index = 1;
                for (var field : this.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value != null) {
                        ps.setObject(index++, value);
                    }
                }
                int result = ps.executeUpdate();
                message = "Insert successful: " + result + " rows affected.";
            }
        } catch (IllegalAccessException | SQLException e) {
            message = e.getMessage();
        }
    }

    
    abstract E toModel(ResultSet rs);

    public ArrayList<E> get() {
        ArrayList<E> results = new ArrayList<>();
        String query = "SELECT " + select + " FROM " + table;
        if (!join.isEmpty()) query += join;
        if (!where.isEmpty()) query += " WHERE " + where;
        if (!otherQuery.isEmpty()) query += " " + otherQuery;
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                results.add(toModel(rs));
            }
        } catch (SQLException e) {
            message = e.getMessage();
        }
        return results;
    }

    public String getMessage() {
        return message;
    }
    
    public boolean isConnected(){
        return this.isConnected;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public void setOtherQuery(String otherQuery) {
        this.otherQuery = otherQuery;
    }
    
}
