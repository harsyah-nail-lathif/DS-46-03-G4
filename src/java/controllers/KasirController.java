/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.InventarisBarang;
import models.Kasir;
import models.detailTransaksi;
import models.transaksi;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "KasirController", urlPatterns = {"/KasirController"})
public class KasirController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect ke halaman kasir.jsp jika ada permintaan GET
        response.sendRedirect("kasir.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Inisialisasi objek transaksi
        transaksi tr = new transaksi();
        // Ambil action dari parameter
        String action = request.getParameter("action");

        // Ambil list produk dari session
        Map<String, Map<String, String>> productList = (Map<String, Map<String, String>>) session.getAttribute("productList");
        if (productList == null) {
            productList = new HashMap<>();
        }

        if ("add".equals(action)) {
            // Menambahkan produk berdasarkan kode barang
            String kodeBarang = request.getParameter("kode");
            String quantity = request.getParameter("quantity");

            Kasir kasir = new Kasir();
            Kasir product = kasir.find(kodeBarang); // Cari produk berdasarkan kodeBarang

            if (product != null && quantity != null) {
                // Tambahkan produk ke dalam Map
                Map<String, String> productDetails = new HashMap<>();
                productDetails.put("productCode", product.getKodeBarang());
                productDetails.put("quantity", quantity);
                productDetails.put("price", String.valueOf(product.getPrice()));

                productList.put(product.getKodeBarang(), productDetails);
                session.setAttribute("productList", productList);
            } else {
                request.setAttribute("error", "Produk tidak ditemukan.");
            }
        } else if ("delete".equals(action)) {
            // Menghapus produk dari list
            String kodeBarang = request.getParameter("deleteProduct");
            if (kodeBarang != null) {
                productList.remove(kodeBarang);
                session.setAttribute("productList", productList);
            }
        } else if ("calculate".equals(action)) {

            // Hitung total harga
            double totalAmount = 0;
            for (Map.Entry<String, Map<String, String>> entry : productList.entrySet()) {
                Map<String, String> product = entry.getValue();
//                String kodeBarang = product.get("kodeBarang");
                int qty = Integer.parseInt(product.get("quantity"));
                double prc = Double.parseDouble(product.get("price")); // Gunakan Double untuk price

                // Menambahkan detail transaksi
//                detailTransaksi detail = new detailTransaksi(kodeBarang, qty, prc);
//                tr.tambahDetailTransaksi(detail);
                totalAmount += qty * prc;
                session.setAttribute("totalPrice", totalAmount);
            }

            // Ambil nominal pembayaran
            String paidAmountParam = request.getParameter("paidAmount");
            if (paidAmountParam != null && !paidAmountParam.isEmpty()) {
                try {
                    double paidAmount = Double.parseDouble(paidAmountParam); // Gunakan Double untuk paidAmount
                    double balance = paidAmount - totalAmount;

                    // Simpan kembalian ke session
                    session.setAttribute("balance", balance);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Format nominal uang tidak valid.");
                }
            } else {
                request.setAttribute("error", "Nominal uang harus diisi.");
            }
        } else if ("submitTransaction".equals(action)) {
            // Inisialisasi objek transaksi
            //ArrayList<String> temp = new ArrayList<>();
            ArrayList<detailTransaksi> dtl = new ArrayList<>();
            // Menambahkan detail transaksi berdasarkan productList
            int i = 1;
            for (Map.Entry<String, Map<String, String>> entry : productList.entrySet()) {
                Map<String, String> product = entry.getValue();
                Date tanggalTransaksi = Date.valueOf(LocalDate.now());
                String barangId = product.get("productCode");
                //temp.add(barangId);
                int qBarang = Integer.parseInt(product.get("quantity"));
                double harga = Double.valueOf(product.get("price"));
                detailTransaksi dt = new detailTransaksi(String.valueOf(i), barangId, qBarang, harga);
                dtl.add(dt);
                i++;
                double totalAmount = (double) session.getAttribute("totalPrice");
                tr = new transaksi(tr.getMaxId(), tanggalTransaksi, totalAmount, "kasir");
            }
            tr.setDetailTransaksiList(dtl);
            // Simpan transaksi
            try {
                tr.simpanTransaksi();
                request.setAttribute("successMessage", "Transaksi berhasil disimpan.");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Gagal menyimpan transaksi: " + e.getMessage());
            }

            // Redirect atau forward ke halaman sesuai hasil
            request.getRequestDispatcher("kasir.jsp").forward(request, response);
        }

//        // Redirect ke halaman kasir.jsp
        response.sendRedirect("kasir.jsp");
    }
}
