/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package KasirClass;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author jismi
 */
@WebServlet(name = "KasirController", urlPatterns = {"/KasirController"})
public class KasirController extends HttpServlet {

    // Instansiasi objek Kasir
    private Kasir kasir;

    @Override
    public void init() throws ServletException {
        kasir = new Kasir();  // Membuat objek Kasir
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String kodeBarang = request.getParameter("kode");

        if (kodeBarang != null) {
            Kasir foundKasir = kasir.findProductByCode(kodeBarang);  // Cari barang berdasarkan kodeBarang
            if (foundKasir != null) {
                // Kirimkan hasil pencarian ke halaman JSP
                request.setAttribute("kasir", foundKasir);
                request.getRequestDispatcher("/kasir.jsp").forward(request, response);
            } else {
                response.getWriter().println("Barang tidak ditemukan!");
            }
        } else {
            response.getWriter().println("Kode barang tidak diberikan!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Ambil data dari form
        String kodeBarang = request.getParameter("kodeBarang");
        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        if (priceStr != null && !priceStr.trim().isEmpty()) {
            double price = Double.parseDouble(priceStr.trim());
            // Lakukan proses lainnya
        } else {
            // Tangani kasus jika harga tidak diberikan atau kosong
            double price = 0;
            response.getWriter().println("Harga tidak valid!");
        }
        double price = 0;

        // Membuat objek Kasir baru dan menyimpan data barang
        Kasir newKasir = new Kasir(kodeBarang, name, price);

        // Logika untuk menyimpan data ke database bisa ditambahkan di sini
        // Contoh: kasir.save(newKasir);
        response.sendRedirect("kasir.jsp");
    }

    @Override
    public void destroy() {
        // Cleanup resources, jika diperlukan
    }
}
