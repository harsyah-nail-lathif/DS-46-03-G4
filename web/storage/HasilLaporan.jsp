<%-- 
    Document   : HasilLaporan
    Created on : 1 Jan 2025, 22.37.20
    Author     : jihad
--%>

<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.LaporanPenjualan, models.transaksi, java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hasil Laporan Penjualan</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h2 class="mb-4">Hasil Laporan Penjualan</h2>

        <!-- Informasi kategori laporan -->
        <p><strong>Kategori Laporan:</strong> ${kategori}</p>

        <!-- Tabel hasil laporan -->
        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th>ID Transaksi</th>
                    <th>Tanggal Transaksi</th>
                    <th>Total Harga</th>
                    <th>ID Kasir</th>
                </tr>
            </thead>
            <tbody>
            <% 
                LaporanPenjualan lp = (LaporanPenjualan)request.getAttribute("laporan");
                List<transaksi> tArr = new ArrayList<>();
                boolean found = false;

                // Check which report list is not empty
                if (!lp.getLaporanHarian().isEmpty()) {
                    tArr = lp.getLaporanHarian();
                    found = true;
                } else if (!lp.getLaporanBulanan().isEmpty()) {
                    tArr = lp.getLaporanBulanan();
                    found = true;
                } else if (!lp.getLaporanMingguan().isEmpty()) {
                    tArr = lp.getLaporanMingguan();
                    found = true;
                } else if (!lp.getTransaksiList().isEmpty()) {
                    tArr = lp.getTransaksiList();
                    found = true;
                } else {
            %>
                <tr><td colspan="4">Tidak Ada List Laporan</td></tr>
            <% 
                }

                // If found a valid report, loop through the transactions
                if (found) {
                    for (transaksi tr : tArr) {
            %>
                <tr>
                    <td><%= tr.getId() %></td>
                    <td><%= tr.getTanggalTransaksi() %></td>
                    <td><%= tr.getTotalHarga() %></td>
                    <td><%= tr.getKasirID() %></td>
                </tr>
            <% 
                    }
                }
            %>
        </tbody>
        </table>

        <a href="Laporan.jsp" class="btn btn-secondary mt-3">Kembali</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

