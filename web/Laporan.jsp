<%-- 
    Document   : Laporan
    Created on : 1 Jan 2025, 18.48.35
    Author     : jihad
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.LaporanPenjualan"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pilih Laporan Penjualan</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container {
            margin-top: 50px;
        }
        .form-group {
            margin-bottom: 30px;
        }
        .form-control {
            padding: 10px;
            font-size: 16px;
        }
        .btn-primary {
            padding: 10px 20px;
            font-size: 16px;
            width: 100%;
            border-radius: 5px;
        }
        .title {
            text-align: center;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="title">Pilih Jenis Laporan Penjualan</h2>
        <form method="get" action="<%=request.getContextPath()%>/LaporanController">
                
                <label for="jenisLaporan" class="form-label">Jenis Laporan</label>
                <select id="jenisLaporan" name="jenisLaporan" class="form-control" required>
                    <option value="harian">Laporan Harian</option>
                    <option value="mingguan">Laporan Mingguan</option>
                    <option value="bulanan">Laporan Bulanan</option>
                    
                </select>
            <button type="submit" class="btn btn-primary">Tampilkan Laporan</button>
            <a href="kasir.jsp" class="btn btn-secondary mt-3">Kembali</a>
        </form>
    </div>
</body>
</html>

