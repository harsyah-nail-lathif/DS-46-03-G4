/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.LaporanPenjualan;

/**
 *
 * @author jihad
 */
@WebServlet(name = "LaporanController", urlPatterns = {"/LaporanController"})
public class LaporanController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LaporanController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LaporanController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jenis = request.getParameter("jenisLaporan");
        //response.getWriter().println(jenis);
        LaporanPenjualan lp = new LaporanPenjualan();
        //response.getWriter().println(lp.getMessage());
        try{
            if(jenis.equals("harian")){
                lp.generateLaporanHarian();
            }else if(jenis.equals("mingguan")){
                lp.generateLaporanMingguan();
            }else if(jenis.equals("bulanan")){
                lp.generateLaporanBulanan();
            }else{
                lp.barangTerlaris();
            }
            request.setAttribute("laporan", lp);
            request.getRequestDispatcher("storage/HasilLaporan.jsp").forward(request, response);
        }catch(Exception e){
            response.getWriter().println("ERR: \n"+e.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LaporanPenjualan laporanPenjualan = new LaporanPenjualan();
        String jenisLaporan = (String)request.getParameter("jenisLaporan");
        /*
        if (jenisLaporan == null || "".equals(jenisLaporan)) {
            response.getWriter().println("is null");
            response.sendRedirect(request.getContextPath() + "storage/Laporan.jsp");
        }
        */

        try {
            response.getWriter().println("inisiasi objek");
            response.getWriter().println("parameter:"+jenisLaporan);
            if("harian".equals(jenisLaporan)) {
                response.getWriter().println("selected harian");
                /*
                laporanPenjualan.generateLaporanHarian();
                request.setAttribute("laporan", laporanPenjualan.getLaporanHarian());
                request.setAttribute("judul", "Laporan Harian");
                */
            }else if ("mingguan".equals(jenisLaporan)) {
                laporanPenjualan.generateLaporanMingguan();
                request.setAttribute("laporan", laporanPenjualan.getLaporanMingguan());
                request.setAttribute("judul", "Laporan Mingguan");
            }else if ("bulanan".equals(jenisLaporan)) {
                laporanPenjualan.generateLaporanBulanan();
                request.setAttribute("laporan", laporanPenjualan.getLaporanBulanan());
                request.setAttribute("judul", "Laporan Bulanan");
            }else if ("terlaris".equals(jenisLaporan)) {
                laporanPenjualan.barangTerlaris();
                request.setAttribute("laporan", laporanPenjualan.getTransaksiList());
                request.setAttribute("judul", "Barang Terlaris");
            }else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Jenis laporan tidak valid");
                return;
            }

            request.getRequestDispatcher("storage/hasil_laporan.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new ServletException("Gagal memproses laporan", e);
        
        }
    
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
