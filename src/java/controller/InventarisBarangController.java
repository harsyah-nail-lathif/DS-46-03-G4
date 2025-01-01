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
import java.time.LocalDate;
import java.util.ArrayList;
import models.InventarisBarang;

/**
 *
 * @author jismi
 */
@WebServlet(name = "InventarisBarangController", urlPatterns = {"/inventaris"})
public class InventarisBarangController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String menu = request.getParameter("menu");
        if (menu == null || menu.isEmpty()) {
            response.sendRedirect("inventaris?menu=view");
            return;
        }

        InventarisBarang inventarisModel = new InventarisBarang();

        if ("view".equals(menu)) {
            ArrayList<InventarisBarang> items = inventarisModel.get();
            request.setAttribute("items", items);
            request.getRequestDispatcher("/product/view.jsp").forward(request, response);

        } else if ("add".equals(menu)) {
            request.getRequestDispatcher("/product/add.jsp").forward(request, response);

        } else if ("edit".equals(menu)) {
            String kodeBarang = request.getParameter("kodeBarang");
            InventarisBarang item = inventarisModel.find(kodeBarang);
            if (item != null) {
                request.setAttribute("item", item);
                request.getRequestDispatcher("/product/edit.jsp").forward(request, response);
            } else {
                response.sendRedirect("inventaris?menu=view");
            }

        } else {
            response.sendRedirect("inventaris?menu=view");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String action = request.getParameter("action");
        InventarisBarang inventarisModel = new InventarisBarang();

        if ("add".equals(action)) {
            String kodeBarang = request.getParameter("kodeBarang");
            String namaBarang = request.getParameter("namaBarang");
            int stok = Integer.parseInt(request.getParameter("stok"));
            double hargaBeli = Double.parseDouble(request.getParameter("hargaBeli"));
            double hargaJual = Double.parseDouble(request.getParameter("hargaJual"));
            LocalDate tanggalMasuk = LocalDate.parse(request.getParameter("tanggalMasuk"));

            inventarisModel.setKodeBarang(kodeBarang);
            inventarisModel.setNamaBarang(namaBarang);
            inventarisModel.setStok(stok);
            inventarisModel.setHargaBeli(hargaBeli);
            inventarisModel.setHargaJual(hargaJual);
            inventarisModel.setTanggalMasuk(tanggalMasuk);
            inventarisModel.insert();

        } else if ("edit".equals(action)) {
            String kodeBarang = request.getParameter("kodeBarang");
            String namaBarang = request.getParameter("namaBarang");
            int stok = Integer.parseInt(request.getParameter("stok"));
            double hargaBeli = Double.parseDouble(request.getParameter("hargaBeli"));
            double hargaJual = Double.parseDouble(request.getParameter("hargaJual"));
            LocalDate tanggalMasuk = LocalDate.parse(request.getParameter("tanggalMasuk"));

            inventarisModel.setKodeBarang(kodeBarang);
            inventarisModel.setNamaBarang(namaBarang);
            inventarisModel.setStok(stok);
            inventarisModel.setHargaBeli(hargaBeli);
            inventarisModel.setHargaJual(hargaJual);
            inventarisModel.setTanggalMasuk(tanggalMasuk);
            inventarisModel.update();

        } else if ("delete".equals(action)) {
            String kodeBarang = request.getParameter("kodeBarang");
            inventarisModel.setKodeBarang(kodeBarang);
            inventarisModel.delete();
        }

        response.sendRedirect("inventaris?menu=view");
    }
}
