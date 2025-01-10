package controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import models.User;

@WebServlet(name = "AuthController", urlPatterns = {"/AuthController"})
public class AuthController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        User userModel = new User();

        if ("register".equals(action)) {
            String id = request.getParameter("id"); // Gunakan ID dari input form
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            userModel.setId(id);
            userModel.setUsername(username);
            userModel.setPassword(password);
            userModel.setRole(role);
            
            try {
                User user = new User(id, username, password, role);
                if (user.save()) {
                    response.sendRedirect("index.jsp?message=register_success");
                } else {
                    request.setAttribute("error", "Gagal menyimpan data pengguna.");
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Gagal registrasi: " + e.getMessage());
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        }

        if ("login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                User user = User.login(username, password);
                if (user != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user.getUsername());
                    session.setAttribute("role", user.getRole());
                    if ("kasir".equals(user.getRole())) {
                        response.sendRedirect("kasir.jsp");
                    } else if ("inventaris".equals(user.getRole())) {
                        response.sendRedirect("inventaris?menu=view");
                    }
                } else {
                    response.sendRedirect("index.jsp?error=1");
                }
            } catch (SQLException e) {
                request.setAttribute("error", "Login error: " + e.getMessage());
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } else if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/index.jsp?message=logout_success");
        }
    }
}
