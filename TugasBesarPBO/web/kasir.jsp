<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sales Page JSP</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%
        // Inisialisasi ArrayList di sesi atau di halaman
        List<HashMap<String, String>> productList = (List<HashMap<String, String>>) session.getAttribute("productList");

        if (productList == null) {
            productList = new ArrayList<>();
        }

        // Logika untuk menambahkan produk ke dalam list
        if ("POST".equalsIgnoreCase(request.getMethod()) && request.getParameter("productCode") != null) {
            String productCode = request.getParameter("productCode");
            String quantity = request.getParameter("quantity");
            String price = request.getParameter("price");

            if (productCode != null && quantity != null && price != null) {
                HashMap<String, String> product = new HashMap<>();
                product.put("productCode", productCode);
                product.put("quantity", quantity);
                product.put("price", price);

                productList.add(product);
                session.setAttribute("productList", productList);
            }
        }

        // Logika untuk menghapus produk dari list
        if (request.getParameter("deleteProduct") != null) {
            String deleteCode = request.getParameter("deleteProduct");

            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).get("productCode").equals(deleteCode)) {
                    productList.remove(i);
                    break;
                }
            }

            session.setAttribute("productList", productList);
        }

        // Menghitung total Amount
        int totalAmount = 0;
        for (HashMap<String, String> product : productList) {
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
                        <form method="post">
                            <div class="row align-items-center">
                                <div class="col-md-4">
                                    <label class="fw-bold">Product Code</label>
                                    <input type="text" name="productCode" class="form-control" placeholder="Product Code" required>
                                </div>
                                <div class="col-md-3">
                                    <label class="fw-bold">Quantity</label>
                                    <input type="number" name="quantity" class="form-control" value="1" required>
                                </div>
                                <div class="col-md-3">
                                    <label class="fw-bold">Price</label>
                                    <input type="number" name="price" class="form-control" value="1" required>
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
                                    for (HashMap<String, String> product : productList) {
                                        int qty = Integer.parseInt(product.get("quantity"));
                                        int prc = Integer.parseInt(product.get("price"));
                                        int amount = qty * prc;
                                %>
                                    <tr>
                                        <!-- Tombol Delete -->
                                        <td>
                                            <form method="post">
                                                <input type="hidden" name="deleteProduct" value="<%= product.get("productCode") %>">
                                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                            </form>
                                        </td>
                                        <td><%= product.get("productCode") %></td>
                                        <td><%= product.get("quantity") %></td>
                                        <td><%= product.get("price") %></td>
                                        <td><%= amount %></td>
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
                        <!-- Total Amount -->
                        <div class="mb-3 row">
                            <label class="col-sm-4 col-form-label fw-bold">Total Amount</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" value="<%= totalAmount %>" readonly>
                            </div>
                        </div>

                        <!-- Nominal Uang -->
                        <form method="post">
                            <div class="mb-3 row">
                                <label class="col-sm-4 col-form-label fw-bold">Nominal Uang</label>
                                <div class="col-sm-8">
                                    <input type="number" name="paidAmount" class="form-control" placeholder="Masukkan Uang Bayar" required>
                                </div>
                            </div>

                            <!-- Tombol untuk Submit -->
                            <div class="mb-3 row">
                                <div class="col-sm-12 text-center">
                                    <button type="submit" class="btn btn-primary">Hitung Kembalian</button>
                                </div>
                            </div>
                        </form>

                        <%
                            // Mengambil nominal yang dibayar dari form
                            String paidAmountParam = request.getParameter("paidAmount");
                            int paidAmount = 0;
                            int balance = 0;

                            if (paidAmountParam != null && !paidAmountParam.isEmpty()) {
                                paidAmount = Integer.parseInt(paidAmountParam);
                                balance = paidAmount - totalAmount;
                            }
                        %>

                        <!-- Kembalian -->
                        <div class="mb-3 row">
                            <label class="col-sm-4 col-form-label fw-bold">Kembalian</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" value="<%= balance %>" readonly>
                            </div>
                        </div>

                        <!-- Tombol Cetak Struk -->
                        <div class="d-flex justify-content-center">
                            <form method="post" action="printInvoice.jsp" target="_blank">
                                <button type="submit" class="btn btn-success">Cetak Struk</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
    <!-- Bootstrap Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
