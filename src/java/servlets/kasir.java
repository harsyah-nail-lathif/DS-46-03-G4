package servlets;

import JDBC.JDBC;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "kasir", urlPatterns = {"/kasir"})
public class kasir extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JDBC db = new JDBC();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (db.isConnected) {
                String menu = request.getParameter("m");
                if ("src".equals(menu)) {
                    // Mendapatkan kode barang dari request
                    int kode = Integer.parseInt(request.getParameter("kode"));

                    // Query untuk mendapatkan harga barang
                    ResultSet rs = db.getData("SELECT harga FROM inventaris_barang WHERE kode = " + kode);

                    // Mengambil hasil query
                    double harga = 0;
                    if (rs.next()) {
                        harga = rs.getDouble("harga");
                    }

                    // Mengirimkan harga kembali ke JSP
                    request.setAttribute("hargaBarang", harga);
                    request.getRequestDispatcher("kasir.jsp").forward(request, response);
                } else if ("del".equals(menu)) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    db.runQuery("DELETE FROM barang WHERE id = " + id);
                } else {
                    String nama = request.getParameter("nama");
                    int jumlah = Integer.parseInt(request.getParameter("jumlah"));
                    double harga = Double.parseDouble(request.getParameter("harga"));
                    db.runQuery("INSERT INTO barang (nama, jumlah, harga) VALUES ('" + nama + "','" + jumlah + "', '" + harga + "')");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            db.disconnect();
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
