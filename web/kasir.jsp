<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="models.Kasir" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Kasir - Inventory Management</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <%
            // Gunakan session langsung tanpa deklarasi ulang
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect("index.jsp");
                return;
            }

            // Ambil data produk dari session
            Map<String, Map<String, String>> productList = (Map<String, Map<String, String>>) session.getAttribute("productList");
            if (productList == null) {
                productList = new HashMap<>();
            }

            // Hitung total amount hanya sekali
            double totalAmount = 0;
            for (Map.Entry<String, Map<String, String>> entry : productList.entrySet()) {
                Map<String, String> product = entry.getValue();
                int qty = Integer.parseInt(product.get("quantity"));
                double prc = Double.parseDouble(product.get("price"));
                totalAmount += qty * prc; // Kalkulasi total amount
            }

            // Hitung kembalian jika ada
            double balance = 0;
            String paidAmountParam = request.getParameter("paidAmount");
            if (paidAmountParam != null && !paidAmountParam.isEmpty()) {
                double paidAmount = Double.parseDouble(paidAmountParam);
                balance = paidAmount - totalAmount;
            }

            // Simpan productList ke session
            session.setAttribute("productList", productList);
        %>

        <!-- result.jsp -->
        <% if (request.getAttribute("successMessage") != null) {%>
        <div class="alert alert-success">
            <%= request.getAttribute("successMessage")%>
        </div>
        <% } %>

        <% if (request.getAttribute("errorMessage") != null) {%>
        <div class="alert alert-danger">
            <%= request.getAttribute("errorMessage")%>
        </div>
        <% } %>


        <div class="container mt-4">
            <h2 class="text-center mb-4">Halaman Kasir</h2>

            <div class="row">
                <!-- Section 1: Add Product & Product Table -->
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-header"><strong>Tambah Produk</strong></div>
                        <div class="card-body">
                            <form method="post" action="KasirController">
                                <input type="hidden" name="action" value="add">
                                <div class="row align-items-center">
                                    <div class="col-md-4">
                                        <label class="fw-bold">Kode Barang</label>
                                        <input type="text" name="kode" class="form-control" placeholder="Masukkan Kode Barang" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="fw-bold">Quantity</label>
                                        <input type="number" name="quantity" class="form-control" value="1" placeholder="Masukkan Jumlah Barang" required>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="fw-bold">&nbsp;</label>
                                        <button type="submit" class="btn btn-success form-control">Add</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Product Table -->
                    <div class="card">
                        <div class="card-header"><strong>Produk</strong></div>
                        <div class="card-body">
                            <table class="table table-bordered text-center">
                                <thead class="table-light">
                                    <tr>
                                        <th>Action</th>
                                        <th>Kode Barang</th>
                                        <th>Quantity</th>
                                        <th>Harga</th>
                                        <th>Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (Map.Entry<String, Map<String, String>> entry : productList.entrySet()) {
                                            Map<String, String> product = entry.getValue();
                                            int qty = Integer.parseInt(product.get("quantity"));
                                            double prc = Double.parseDouble(product.get("price"));
                                            double amount = qty * prc;
                                    %>
                                    <tr>
                                        <!-- Tombol Delete -->
                                        <td>
                                            <form method="post" action="KasirController">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="deleteProduct" value="<%= product.get("productCode")%>">
                                                <button type="submit" class="btn btn-danger btn-sm">Hapus</button>
                                            </form>
                                        </td>
                                        <td><%= product.get("productCode")%></td>
                                        <td><%= product.get("quantity")%></td>
                                        <td><%= String.format("%.2f", prc)%></td> <!-- Harga -->
                                        <td><%= String.format("%.2f", amount)%></td> <!-- Total -->
                                    </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Section 2: Summary -->
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-header"><strong>Ringkasan</strong></div>
                        <div class="card-body">
                            <div class="mb-3 row">
                                <label class="col-sm-4 col-form-label fw-bold">Total Harga</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" value="<%= String.format("%.2f", totalAmount)%>" readonly>
                                </div>
                            </div>

                            <form method="post" action="KasirController">
                                <input type="hidden" name="action" value="calculate">
                                <div class="mb-3 row">
                                    <label class="col-sm-4 col-form-label fw-bold">Uang Bayar</label>
                                    <div class="col-sm-8">
                                        <input type="number" name="paidAmount" class="form-control" step="0.01" placeholder="Masukkan Uang Bayar" required>
                                    </div>
                                </div>
                                <div class="mb-3 row">
                                    <div class="col-sm-12 text-center">
                                        <button type="submit" class="btn btn-primary">Hitung Kembalian</button>
                                    </div>
                                </div>
                            </form>

                            <div class="mb-3 row">
                                <label class="col-sm-4 col-form-label fw-bold">Kembalian</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" value="<%= session.getAttribute("balance") != null ? String.format("%.2f", session.getAttribute("balance")) : "0.00"%>" readonly>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="text-end mb-3">
                        <form method="POST" action="<%= request.getContextPath()%>/AuthController" style="display: inline;">
                            <input type="hidden" name="action" value="logout">
                            <button type="submit" class="btn btn-danger">Logout</button>
                        </form>
                        <a href="transaksiController?action=view" class="btn btn-primary">Lihat Transaksi</a>
                    </div>
                    <div class="text-end mb-3">
                        <form method="POST" action="KasirController">
                            <input type="hidden" name="action" value="submitTransaction">
                            <input type="hidden" name="productList" value="<%= productList%>"> <!-- Menambahkan productList ke form -->
                            <button type="submit" class="btn btn-primary mt-3">Submit Transaksi</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
