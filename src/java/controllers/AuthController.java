package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "AuthController", urlPatterns = {"/AuthController"})
public class AuthController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // (#1) Redirect ke halaman login jika pengguna mengakses AuthController tanpa POST
        response.sendRedirect("index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        // (#2) Pengecekan apakah action bernilai "login"
        if ("login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // (#3) Validasi hardcoded untuk user demo
            if ("kasir".equals(username) && "1234".equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", username);
                session.setAttribute("role", "kasir"); // Simpan role kasir
                response.sendRedirect(request.getContextPath() + "/kasir.jsp");
            } else if ("admin".equals(username) && "1234".equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", username);
                session.setAttribute("role", "admin"); // Simpan role admin
                response.sendRedirect(request.getContextPath() + "/inventaris?menu=view");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=1");
            }

        } else if ("logout".equals(action)) {
            // (#4) Proses logout
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Hapus session
            }
            response.sendRedirect(request.getContextPath() + "/index.jsp");

        } else {
            // Redirect ke halaman login jika action tidak valid
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}
