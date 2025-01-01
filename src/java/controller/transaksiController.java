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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import models.InventarisBarang;
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
            transaksi tr = new transaksi();
            tr.setTanggalTransaksi(java.sql.Date.valueOf(LocalDate.now()));
            tr.setKasirID((String) session.getAttribute("user"));

            Map<String, Map<String, String>> productList = (Map<String, Map<String, String>>) session.getAttribute("productList");
            if (productList == null || productList.isEmpty()) {
                request.setAttribute("error", "Keranjang belanja kosong.");
                request.getRequestDispatcher("kasir.jsp").forward(request, response);
                return;
            }

            double totalHarga = 0;
            InventarisBarang inventarisBarang = new InventarisBarang();
            for (Map.Entry<String, Map<String, String>> entry : productList.entrySet()) {
                Map<String, String> product = entry.getValue();
                detailTransaksi detail = new detailTransaksi();
                detail.setBarangID(product.get("productCode"));
                detail.setJumlah(Integer.parseInt(product.get("quantity")));
                detail.setHarga(Double.parseDouble(product.get("price")));
                totalHarga += detail.getJumlah() * detail.getHarga();
                tr.tambahDetailTransaksi(detail);

                // Kurangi stok
                try {
                    inventarisBarang.kurangiStok(product.get("productCode"), Integer.parseInt(product.get("quantity")));
                } catch (SQLException e) {
                    request.setAttribute("error", "Gagal mengurangi stok untuk barang: " + product.get("productCode") + " - " + e.getMessage());
                    request.getRequestDispatcher("kasir.jsp").forward(request, response);
                    return;
                }
            }
            tr.setTotalHarga(totalHarga);

            try {
                tr.simpanTransaksi();
                session.removeAttribute("productList");
                response.sendRedirect("transaksiController?action=view");
            } catch (SQLException e) {
                request.setAttribute("error", "Gagal menyimpan transaksi: " + e.getMessage());
                request.getRequestDispatcher("kasir.jsp").forward(request, response);
            }
        }

    }
}
