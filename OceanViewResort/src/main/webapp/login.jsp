<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - OceanView Resort</title>

    <style>
        body {
            font-family: Arial;
            background-color: #f4f6f9;
        }

        .login-box {
            width: 350px;
            margin: 100px auto;
            padding: 30px;
            background: white;
            box-shadow: 0 0 10px #ccc;
            border-radius: 8px;
        }

        input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
        }

        button {
            width: 100%;
            padding: 10px;
            background-color: #007BFF;
            color: white;
            border: none;
            cursor: pointer;
        }

        .error {
            color: red;
            text-align: center;
        }

        .show-password {
            font-size: 14px;
        }
    </style>

    <script>
        function togglePassword() {
            var passwordField = document.getElementById("password");

            if (passwordField.type === "password") {
                passwordField.type = "text";
            } else {
                passwordField.type = "password";
            }
        }
    </script>
</head>

<body>

<div class="login-box">
    <h2>OceanView Login</h2>

    <form action="login" method="post">

        <input type="email" name="email" placeholder="Enter Email" required />

        <input type="password" id="password" name="password"
               placeholder="Enter Password" required />

        <div class="show-password">
            <input type="checkbox" onclick="togglePassword()">
            Show Password
        </div>

        <br>

        <button type="submit">Login</button>
    </form>

    <% if(request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>

</div>

</body>
</html>

