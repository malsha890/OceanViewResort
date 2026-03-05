<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login | OceanView Resort</title>

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }

        body {
            height: 100vh;
            overflow: hidden;
            position: relative;
        }

        /*  Background Slider */
        .background-slider {
            position: fixed;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            z-index: -2;
        }

        .background-slider img {
            position: absolute;
            width: 100%;
            height: 100%;
            object-fit: cover;
            opacity: 0;
            animation: slideShow 24s infinite;
        }

        .background-slider img:nth-child(1) { animation-delay: 0s; }
        .background-slider img:nth-child(2) { animation-delay: 8s; }
        .background-slider img:nth-child(3) { animation-delay: 16s; }

        @keyframes slideShow {
            0% { opacity: 0; }
            8% { opacity: 1; }
            25% { opacity: 1; }
            33% { opacity: 0; }
            100% { opacity: 0; }
        }

        /* Dark overlay */
        .overlay {
            position: fixed;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: -1;
        }

        /* Login Container */
        .login-container {
            width: 380px;
            padding: 40px;
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(15px);
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.3);
            color: white;
            animation: fadeIn 1s ease-in-out;
            position: absolute;
            top: 50%;
            right: 8%;
            transform: translateY(-50%);
        }

        .login-container h2 {
            text-align: center;
            margin-bottom: 20px;
            font-weight: 600;
        }

        /* Welcome Section */
        .welcome-message {
            position: absolute;
            top: 50%;
            left: 8%;
            transform: translateY(-50%);
            color: white;
            max-width: 450px;
            animation: fadeIn 1.2s ease-in-out;
        }

        .welcome-message h1 {
            font-size: 36px;
            margin-bottom: 15px;
        }

        .welcome-message p {
            font-size: 16px;
            line-height: 1.6;
            opacity: 0.9;
        }

        .input-group {
            position: relative;
            margin-bottom: 20px;
        }

        .input-group input {
            width: 100%;
            padding: 12px 40px 12px 12px;
            border: none;
            border-radius: 8px;
            outline: none;
            font-size: 14px;
        }

        .input-group span {
            position: absolute;
            right: 10px;
            top: 12px;
            cursor: pointer;
            font-size: 14px;
            color: #555;
        }

        .login-btn {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 8px;
            background: white;
            color: #007BFF;
            font-weight: 600;
            cursor: pointer;
            transition: 0.3s ease;
        }

        .login-btn:hover {
            background: #007BFF;
            color: white;
            transform: scale(1.03);
        }

        .error {
            margin-top: 15px;
            padding: 10px;
            background: rgba(255, 0, 0, 0.3);
            border-radius: 8px;
            text-align: center;
            font-size: 14px;
        }

        .footer {
            text-align: center;
            margin-top: 20px;
            font-size: 13px;
            opacity: 0.8;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        @media(max-width: 900px){
            .welcome-message{
                display: none;
            }
            .login-container{
                right: 50%;
                transform: translate(50%, -50%);
            }
        }
    </style>

    <script>
        function togglePassword() {
            var passwordField = document.getElementById("password");
            var toggleText = document.getElementById("toggleText");

            if (passwordField.type === "password") {
                passwordField.type = "text";
                toggleText.innerText = "Hide";
            } else {
                passwordField.type = "password";
                toggleText.innerText = "Show";
            }
        }
    </script>
</head>

<body>

<!--  Background Images -->

  <div class="background-slider">
<img src="${pageContext.request.contextPath}/images/resort1.jpg">
<img src="${pageContext.request.contextPath}/images/resort2.jpg">
<img src="${pageContext.request.contextPath}/images/resort3.jpg">

</div>


<div class="overlay"></div>

<!--  Welcome Message -->
<div class="welcome-message">
    <h1>Welcome to OceanView Resort</h1>
    <p>
        Experience elegance, comfort, and world-class hospitality.
        Please log in to access the secure staff management portal.
    </p>
</div>

<!-- Login Box -->
<div class="login-container">
    <h2>🌊 Staff Login</h2>

    <form action="login" method="post">

        <div class="input-group">
            <input type="email" name="email" placeholder="Enter Email" required />
        </div>

        <div class="input-group">
            <input type="password" id="password" name="password"
                   placeholder="Enter Password" required />
            <span onclick="togglePassword()" id="toggleText">Show</span>
        </div>

        <button type="submit" class="login-btn">Login</button>
    </form>

    <% if(request.getAttribute("error") != null) { %>
        <div class="error">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <div class="footer">
        © 2026 OceanView Resort | Secure Staff Portal
    </div>
</div>

</body>
</html>