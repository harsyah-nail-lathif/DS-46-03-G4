/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Model abstrak untuk semua entitas database.
 *
 * @param <E> Tipe entitas
 */

/**
 *
 * @author LENOVO
 */
public abstract class Model<E> {
    private Connection con;
    private Statement stmt;
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
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_barang", "root", "");
            stmt = con.createStatement();
            isConnected = true;
            message = "Database terkoneksi";
        } catch (ClassNotFoundException | SQLException e) {
            isConnected = false;
            message = e.getMessage();
        }
    }

    public void disconnect() {
        try {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            message = e.getMessage();
        }
    }

    public void insert() {
        try {
            connect();
            String cols = "", values = "";
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                if (value != null) {
                    cols += field.getName() + ", ";
                    values += "'" + value + "', ";
                }
            }
            String query = "INSERT INTO " + table + "(" + cols.substring(0, cols.length() - 2) + ") VALUES (" +
                    values.substring(0, values.length() - 2) + ")";
            int result = stmt.executeUpdate(query);
            message = "Info insert: " + result + " baris berhasil ditambahkan.";
        } catch (IllegalAccessException | SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
        }
    }

    public void update() {
        try {
            connect();
            String values = "";
            Object pkValue = null;
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                if (field.getName().equals(primaryKey)) {
                    pkValue = value;
                } else if (value != null) {
                    values += field.getName() + " = '" + value + "', ";
                }
            }
            String query = "UPDATE " + table + " SET " + values.substring(0, values.length() - 2) +
                    " WHERE " + primaryKey + " = '" + pkValue + "'";
            int result = stmt.executeUpdate(query);
            message = "Info update: " + result + " baris berhasil diperbarui.";
        } catch (IllegalAccessException | SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
        }
    }

    public void delete() {
        try {
            connect();
            Object pkValue = null;
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals(primaryKey)) {
                    pkValue = field.get(this);
                    break;
                }
            }
            String query = "DELETE FROM " + table + " WHERE " + primaryKey + " = '" + pkValue + "'";
            int result = stmt.executeUpdate(query);
            message = "Info delete: " + result + " baris berhasil dihapus.";
        } catch (IllegalAccessException | SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
        }
    }

    public ArrayList<ArrayList<Object>> query(String query) {
        ArrayList<ArrayList<Object>> res = new ArrayList<>();
        try {
            connect();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                ArrayList<Object> row = new ArrayList<>();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getObject(i));
                }
                res.add(row);
            }
        } catch (SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
        }
        return res;
    }

    public ArrayList<E> get() {
        ArrayList<E> res = new ArrayList<>();
        try {
            connect();
            String query = "SELECT " + select + " FROM " + table;
            if (!join.isEmpty()) query += join;
            if (!where.isEmpty()) query += " WHERE " + where;
            if (!otherQuery.isEmpty()) query += " " + otherQuery;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                res.add(toModel(rs));
            }
        } catch (SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
            resetQuery();
        }
        return res;
    }

    public E find(String pkValue) {
        try {
            connect();
            String query = "SELECT " + select + " FROM " + table + " WHERE " + primaryKey + " = '" + pkValue + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return toModel(rs);
            }
        } catch (SQLException e) {
            message = e.getMessage();
        } finally {
            disconnect();
            resetQuery();
        }
        return null;
    }

    private void resetQuery() {
        select = "*";
        where = "";
        join = "";
        otherQuery = "";
    }

    abstract E toModel(ResultSet rs);

    public String getMessage() {
        return message;
    }

    public boolean isConnected() {
        return isConnected;
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
