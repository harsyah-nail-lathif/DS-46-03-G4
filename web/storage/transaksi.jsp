<%-- 
    Document   : transaksi
    Created on : 29 Dec 2024, 10.56.24
    Author     : jihad
--%>

<%-- 
    Document   : transaksi
    Created on : 29 Dec 2024, 10.56.24
    Author     : jihad
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="models.transaksi" %>
<% 
    ArrayList<transaksi> transaksiList = (ArrayList<transaksi>) request.getAttribute("transaksi");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daftar Transaksi</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f9;
        }
        .container {
            width: 80%;
            margin: 20px auto;
            background: #ffffff;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h1 {
            text-align: center;
            color: #333333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border: 1px solid #dddddd;
        }
        th {
            background-color: #f2f2f2;
            color: #333333;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        .btn {
            display: inline-block;
            padding: 10px 15px;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin: 5px;
            font-size: 14px;
        }
        .btn-edit {
            background-color: #007bff;
        }
        .btn-edit:hover {
            background-color: #0056b3;
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
        // Simpan posisi scroll sebelum berpindah halaman
        document.addEventListener('click', (e) => {
            if (e.target.tagName === 'A' && e.target.classList.contains('btn-edit')) {
                localStorage.setItem('scrollPosition', window.scrollY);
            }
        });

        // Pulihkan posisi scroll pada halaman load
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
        <h1>Daftar Transaksi</h1>
        <table>
            <thead>
                <tr>
                    <th>ID Transaksi</th>
                    <th>Total Harga</th>
                    <th>Tanggal Transaksi</th>
                    <th>Kasir ID</th>
                    <th>Aksi</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    if (transaksiList != null && !transaksiList.isEmpty()) {
                        for (transaksi t : transaksiList) { 
                %>
                <tr>
                    <td><%= t.getId() %></td>
                    <td><%= t.getTotalHarga() %></td>
                    <td><%= t.getTanggalTransaksi() %></td>
                    <td><%= t.getKasirID() %></td>
                    <td>
                        <a href="transaksiController?action=detail&transaksiId=<%= t.getId() %>" class="btn btn-edit">Lihat Detail</a>
                    </td>
                </tr>
                <% 
                        }
                    } else { 
                %>
                <tr>
                    <td colspan="5" style="text-align: center;">Tidak ada data transaksi.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <div>
            <a href="kasir.jsp" class="back-button">Back</a>
        </div>
    </div>
</body>
</html>

