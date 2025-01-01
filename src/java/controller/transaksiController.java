/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import models.detailTransaksi;
import models.transaksi;

/**
 *
 * @author jismi
 */
@WebServlet(name = "transaksiController", urlPatterns = {"/transaksiController"})
public class transaksiController extends HttpServlet {
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        transaksi tr = new transaksi();

        if ("detail".equals(action)) {
            // Menampilkan detail transaksi
            String transaksiId = request.getParameter("transaksiId");
            if (transaksiId != null && !transaksiId.isEmpty()) {
                detailTransaksi dt = new detailTransaksi();
                ArrayList<detailTransaksi> detailList = dt.getDetailsByTransaksiId(transaksiId);
                tr.setDetailTransaksiList(detailList);
                request.setAttribute("detailTransaksi", tr);
                request.setAttribute("transaksiId", transaksiId);
                request.getRequestDispatcher("/storage/detailTransaksi.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "ID transaksi diperlukan.");
                response.sendRedirect("kasir.jsp");
            }
        } else {
            // Menampilkan semua transaksi
            ArrayList<transaksi> trArr = tr.getAll();
            request.setAttribute("transaksi", trArr);
            request.getRequestDispatcher("/storage/transaksi.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        if ("submitTransaction".equals(action)) {
            // Ambil data transaksi
            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
            String kasirID = (String) session.getAttribute("user");
            // Ambil tanggal transaksi sebagai java.util.Date
            Date tanggalTransaksiUtil = new Date(); // Ini menghasilkan java.util.Date

// Konversi ke java.sql.Date
            java.sql.Date tanggalTransaksi = new java.sql.Date(tanggalTransaksiUtil.getTime());

// Atur tanggal transaksi ke objek transaksi
            transaksi tr = new transaksi();
            tr.setTanggalTransaksi(tanggalTransaksi); // Gunakan java.sql.Date

            tr.setTotalHarga(totalAmount);
            tr.setKasirID(kasirID);

            // Ambil daftar barang dari session
            Map<String, Map<String, String>> productList = (Map<String, Map<String, String>>) session.getAttribute("productList");
            if (productList != null) {
                for (Map.Entry<String, Map<String, String>> entry : productList.entrySet()) {
                    Map<String, String> product = entry.getValue();
                    detailTransaksi detail = new detailTransaksi();
                    detail.setBarangID(product.get("productCode"));
                    detail.setJumlah(Integer.parseInt(product.get("quantity")));
                    detail.setHarga(Double.parseDouble(product.get("price")));
                    tr.tambahDetailTransaksi(detail);
                }
            }

            // Simpan transaksi ke database
            try {
                tr.simpanTransaksi();
                session.removeAttribute("productList"); // Reset keranjang belanja setelah transaksi selesai
                response.sendRedirect("transaksiController?action=view"); // Redirect ke halaman transaksi
            } catch (SQLException e) {
                request.setAttribute("error", "Gagal menyimpan transaksi: " + e.getMessage());
                request.getRequestDispatcher("kasir.jsp").forward(request, response);
            }
        }
    }
}
