<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="KasirClass.Kasir" %>
<%@ page import="KasirClass.ModelKasir" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sales Page JSP</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <%
            // Ambil data produk dari session dan pastikan tipe yang diterima sesuai
            Object sessionProductList = session.getAttribute("productList");

            Map<String, Map<String, String>> productList;
            if (sessionProductList instanceof Map) {
                productList = (Map<String, Map<String, String>>) sessionProductList;
            } else {
                productList = new HashMap<>();
            }

            // Logika untuk menambahkan produk ke dalam map
            if ("POST".equalsIgnoreCase(request.getMethod()) && request.getParameter("kode") != null) {
                String kodeBarang = request.getParameter("kode");
                String quantity = request.getParameter("quantity");

                // Panggil controller untuk mencari produk berdasarkan kodeBarang
                Kasir kasir = new Kasir();
                Kasir product = kasir.findProductByCode(kodeBarang); // Cari produk berdasarkan kodeBarang

                if (product != null && quantity != null) {
                    // Simpan produk dalam Map berdasarkan kode produk
                    Map<String, String> productDetails = new HashMap<>();
                    productDetails.put("productCode", product.getKodeBarang());
                    productDetails.put("quantity", quantity);
                    productDetails.put("price", String.valueOf(product.getPrice()));

                    productList.put(product.getKodeBarang(), productDetails);
                    session.setAttribute("productList", productList);
                } else {
                    out.println("Produk tidak ditemukan.");
                }
            }

            // Menghitung total Amount
            int totalAmount = 0;
            for (Map.Entry<String, Map<String, String>> entry : productList.entrySet()) {
                Map<String, String> product = entry.getValue();
                int qty = Integer.parseInt(product.get("quantity"));
                int prc = Integer.parseInt(product.get("price"));
                totalAmount += qty * prc;
            }
        %>

        <div class="container mt-4">
            <h2 class="text-center mb-4">Inventory Management System - JSP</h2>

            <div class="row">
                <!-- Section 1: Add Product & Product Table -->
                <div class="col-md-6">
                    <div class="card mb-3">
                        <div class="card-header"><strong>Add Products</strong></div>
                        <div class="card-body">
                        <form method="post" action="kasir">
                                <div class="row align-items-center">
                                    <div class="col-md-4">
                                        <label class="fw-bold">Kode Barang</label>
                                        <input type="text" name="kode" class="form-control" placeholder="Masukkan Kode Barang" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="fw-bold">Quantity</label>
                                        <input type="number" name="quantity" class="form-control" value="1" placeholder="Masukkan jumlah barang" required>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="fw-bold">&nbsp;</label>
                                        <button type="submit" class="btn btn-success form-control">Cari</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Product Table -->
                    <div class="card">
                        <div class="card-header"><strong>Products</strong></div>
                        <div class="card-body">
                            <table class="table table-bordered text-center">
                                <thead class="table-light">
                                    <tr>
                                        <th>Action</th>
                                        <th>Product Code</th>
                                        <th>Quantity</th>
                                        <th>Price</th>
                                        <th>Amount</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (Map.Entry<String, Map<String, String>> entry : productList.entrySet()) {
                                            Map<String, String> product = entry.getValue();
                                            int qty = Integer.parseInt(product.get("quantity"));
                                            int prc = Integer.parseInt(product.get("price"));
                                            int amount = qty * prc;
                                    %>
                                    <tr>
                                        <!-- Tombol Delete -->
                                        <td>
                                            <form method="post">
                                                <input type="hidden" name="deleteProduct" value="<%= product.get("productCode")%>">
                                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                            </form>
                                        </td>
                                        <td><%= product.get("productCode")%></td>
                                        <td><%= product.get("quantity")%></td>
                                        <td><%= product.get("price")%></td>
                                        <td><%= amount%></td>
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
                        <div class="card-header"><strong>Summary</strong></div>
                        <div class="card-body">
                            <div class="mb-3 row">
                                <label class="col-sm-4 col-form-label fw-bold">Total Amount</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" value="<%= totalAmount%>" readonly>
                                </div>
                            </div>

                            <form method="post">
                                <div class="mb-3 row">
                                    <label class="col-sm-4 col-form-label fw-bold">Nominal Uang</label>
                                    <div class="col-sm-8">
                                        <input type="number" name="paidAmount" class="form-control" placeholder="Masukkan Uang Bayar" required>
                                    </div>
                                </div>

                                <div class="mb-3 row">
                                    <div class="col-sm-12 text-center">
                                        <button type="submit" class="btn btn-primary">Hitung Kembalian</button>
                                    </div>
                                </div>
                            </form>

                            <%
                                String paidAmountParam = request.getParameter("paidAmount");
                                int paidAmount = 0;
                                int balance = 0;

                                if (paidAmountParam != null && !paidAmountParam.isEmpty()) {
                                    paidAmount = Integer.parseInt(paidAmountParam);
                                    balance = paidAmount - totalAmount;
                                }
                            %>

                            <div class="mb-3 row">
                                <label class="col-sm-4 col-form-label fw-bold">Kembalian</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control" value="<%= balance%>" readonly>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>