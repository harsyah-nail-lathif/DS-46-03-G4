<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="id">
    <head>
        <title>Registrasi</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container my-5">
            <div class="card shadow">
                <div class="card-header bg-success text-white text-center">
                    <h2>Registrasi</h2>
                </div>
                <div class="card-body">
                    <form method="POST" action="<%= request.getContextPath()%>/AuthController">
                        <input type="hidden" name="action" value="register">
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" id="id" name="id" placeholder="Masukkan ID (contoh: KSR001)" required>
                            <label for="id">ID</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="text" class="form-control" name="username" placeholder="Username" required>
                            <label>Username</label>
                        </div>
                        <div class="form-floating mb-3">
                            <input type="password" class="form-control" name="password" placeholder="Password" required>
                            <label>Password</label>
                        </div>
                        <div class="form-floating mb-3">
                            <select class="form-control" name="role" required>
                                <option value="kasir">Kasir</option>
                                <option value="inventaris">Inventaris</option>
                            </select>
                            <label>Role</label>
                        </div>
                        <button type="submit" class="btn btn-success w-100">Register</button>
                    </form>
                    <% if (request.getAttribute("error") != null) {%>
                    <div class="alert alert-danger mt-3 text-center">
                        <%= request.getAttribute("error")%>
                    </div>
                    <% }%>
                </div>
                <div class="card-footer text-center">
                    <!-- Tombol Login -->
                    <a href="<%= request.getContextPath()%>/index.jsp" class="btn btn-primary">Login</a>
                </div>
            </div>
        </div>
    </body>
</html>
