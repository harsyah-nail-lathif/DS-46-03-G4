<%-- 
    Document   : add.jsp
    Created on : Dec 30, 2024, 3:43:48 PM
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="id">
<head>
    <title>Tambah Barang</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container my-5">
        <div class="card shadow">
            <div class="card-header bg-primary text-white text-center">
                <h2>Tambah Barang</h2>
            </div>
            <div class="card-body">
                <form method="POST" action="inventaris">
                    <input type="hidden" name="action" value="add">
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" name="kodeBarang" placeholder="Kode Barang" required>
                        <label>Kode Barang</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" name="namaBarang" placeholder="Nama Barang" required>
                        <label>Nama Barang</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="number" class="form-control" name="stok" placeholder="Stok" required>
                        <label>Stok</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="number" step="0.01" class="form-control" name="hargaBeli" placeholder="Harga Beli" required>
                        <label>Harga Beli</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="number" step="0.01" class="form-control" name="hargaJual" placeholder="Harga Jual" required>
                        <label>Harga Jual</label>
                    </div>
                    <div class="form-floating mb-3">
                        <input type="date" class="form-control" name="tanggalMasuk" placeholder="Tanggal Masuk" required>
                        <label>Tanggal Masuk</label>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Simpan</button>
                </form>
                <div class="mt-3 text-center">
                    <a href="inventaris?menu=view" class="btn btn-secondary">Kembali ke Daftar Barang</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
