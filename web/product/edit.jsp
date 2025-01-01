<%-- 
    Document   : edit.jsp
    Created on : Dec 30, 2024, 3:44:05 PM
    Author     : LENOVO
--%>

<%@page import="models.InventarisBarang"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="id">
<head>
    <title>Edit Barang</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%
        InventarisBarang item = (InventarisBarang) request.getAttribute("item");
        if (item == null) {
    %>
        <div class="container my-5">
            <div class="alert alert-danger text-center">
                <h4>Data barang tidak ditemukan.</h4>
                <a href="inventaris?menu=view" class="btn btn-primary mt-3">Kembali ke Dashboard</a>
            </div>
        </div>
    <%
        } else {
    %>
    <div class="container my-5">
        <div class="card shadow">
            <div class="card-header bg-warning text-dark text-center">
                <h2>Edit Barang</h2>
            </div>
            <div class="card-body">
                <form method="POST" action="inventaris">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="kodeBarang" value="<%= item.getKodeBarang() %>">
                    
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" name="namaBarang" value="<%= item.getNamaBarang() %>" required>
                        <label>Nama Barang</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="number" class="form-control" name="stok" value="<%= item.getStok() %>" required>
                        <label>Stok</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="number" step="0.01" class="form-control" name="hargaBeli" value="<%= item.getHargaBeli() %>" required>
                        <label>Harga Beli</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="number" step="0.01" class="form-control" name="hargaJual" value="<%= item.getHargaJual() %>" required>
                        <label>Harga Jual</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="date" class="form-control" name="tanggalMasuk" value="<%= item.getTanggalMasuk() %>" required>
                        <label>Tanggal Masuk</label>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="inventaris?menu=view" class="btn btn-secondary btn-lg">Kembali</a>
                        <button type="submit" class="btn btn-warning btn-lg">Simpan Perubahan</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <%
        }
    %>
</body>
</html>
