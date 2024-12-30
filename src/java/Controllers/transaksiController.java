/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import TransaksiClass.detailTransaksi;
import TransaksiClass.transaksi;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author jihad
 */
public class transaksiController extends HttpServlet {

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
            out.println("<title>Servlet transaksiController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet transaksiController at " + request.getContextPath() + "</h1>");
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
    String action = request.getParameter("action");
    transaksi tr = new transaksi();

    if ("detail".equals(action)) {
        String transaksiId = request.getParameter("transaksiId");
        if (transaksiId != null && !transaksiId.isEmpty()) {
            try {
                int id = Integer.parseInt(transaksiId);
                detailTransaksi dt = new detailTransaksi();
                ArrayList<detailTransaksi> detailList = dt.getDetailsByTransaksiId(id);
                tr.setDetailTransaksiList(detailList);
                request.setAttribute("detailTransaksi", tr);
                request.setAttribute("transaksiId", transaksiId);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID transaksi tidak valid.");
            }
        } else {
            request.setAttribute("error", "ID transaksi diperlukan.");
        }
        request.getRequestDispatcher("storage/detailTransaksi.jsp").forward(request, response);
    } else {
        ArrayList<transaksi> trArr = tr.getAll();
        request.setAttribute("transaksi", trArr);
        request.getRequestDispatcher("storage/transaksi.jsp").forward(request, response);
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
