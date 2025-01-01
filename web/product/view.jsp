<%-- 
    Document   : view
    Created on : Dec 30, 2024, 3:43:14 PM
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.InventarisBarang"%>
<%@page import="java.util.ArrayList"%>
<%
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("user") == null) {
        response.sendRedirect("../index.jsp");
        return;
    }

    ArrayList<InventarisBarang> items = (ArrayList<InventarisBarang>) request.getAttribute("items");
%>
<!DOCTYPE html>
<html lang="id">
<head>
    <title>Dashboard Inventaris</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container my-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="text-primary">Daftar Inventaris Barang</h2>
            <a href="<%= request.getContextPath() %>/inventaris?menu=add" class="btn btn-success btn-lg">Tambah Barang</a>
        </div>
        <div class="text-end mb-3">
            <form method="POST" action="<%= request.getContextPath() %>/AuthController" style="display: inline;">
                <input type="hidden" name="action" value="logout">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Kode Barang</th>
                        <th>Nama Barang</th>
                        <th>Stok</th>
                        <th>Harga Beli</th>
                        <th>Harga Jual</th>
                        <th>Tanggal Masuk</th>
                        <th>Aksi</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (items != null && !items.isEmpty()) {
                            for (InventarisBarang item : items) {
                    %>
                    <tr>
                        <td><%= item.getKodeBarang() %></td>
                        <td><%= item.getNamaBarang() %></td>
                        <td><%= item.getStok() %></td>
                        <td><%= item.getHargaBeli() %></td>
                        <td><%= item.getHargaJual() %></td>
                        <td><%= item.getTanggalMasuk() %></td>
                        <td>
                            <a href="<%= request.getContextPath() %>/inventaris?menu=edit&kodeBarang=<%= item.getKodeBarang() %>" class="btn btn-warning btn-sm">Edit</a>
                            <form method="POST" action="<%= request.getContextPath() %>/inventaris" style="display: inline;">
                                <input type="hidden" name="action" value="delete" />
                                <input type="hidden" name="kodeBarang" value="<%= item.getKodeBarang() %>" />
                                <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Apakah Anda yakin ingin menghapus barang ini?');">Hapus</button>
                            </form>
                        </td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr>
                        <td colspan="7" class="text-center">Tidak ada data barang.</td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>