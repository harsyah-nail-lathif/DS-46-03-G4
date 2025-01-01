<%-- 
    Document   : detailTransaksi
    Created on : 29 Dec 2024, 17.41.57
    Author     : jihad
--%>

<%-- 
    Document   : detailTransaksi
    Created on : 29 Dec 2024, 17.41.57
    Author     : jihad
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.transaksi, models.detailTransaksi, java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detail Transaksi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
        }
        .container {
            margin: 20px auto;
            width: 80%;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
        }
        .back-button {
            margin-top: 20px;
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 4px;
        }
        .back-button:hover {
            background-color: #0056b3;
        }
    </style>
    <script>
        // Save scroll position when navigating back
        function goBack() {
            const scrollPosition = window.scrollY;
            localStorage.setItem('scrollPosition', scrollPosition);
            window.history.back();
        }

        // Restore scroll position on page load
        window.addEventListener('load', () => {
            const scrollPosition = localStorage.getItem('scrollPosition');
            if (scrollPosition) {
                window.scrollTo(0, parseInt(scrollPosition));
                localStorage.removeItem('scrollPosition');
            }
        });
    </script>
</head>
<body>
    <div class="container">
        <h1>Detail Transaksi</h1>
        <% 
            transaksi trx = (transaksi) request.getAttribute("detailTransaksi");
            String transaksiId = (String) request.getAttribute("transaksiId");
            if (trx != null && trx.getDetailTransaksiList() != null) {
        %>
        <h3>ID Transaksi: <%= transaksiId %></h3>
        <table>
            <thead>
                <tr>
                    <th>ID Detail</th>
                    <th>Barang</th>
                    <th>Jumlah</th>
                    <th>Harga</th>
                </tr>
            </thead>
            <tbody>
                <% for (detailTransaksi detail : trx.getDetailTransaksiList()) { %>
                <tr>
                    <td><%= detail.getId() %></td>
                    <td><%= detail.getBarangID() %></td>
                    <td><%= detail.getJumlah() %></td>
                    <td><%= detail.getHarga() %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <% } else { %>
        <p>Detail tidak ditemukan atau transaksi tidak valid.</p>
        <% } %>
        <a href="javascript:void(0);" class="back-button" onclick="goBack()">Back</a>
    </div>
</body>
</html>




