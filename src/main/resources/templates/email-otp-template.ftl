<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <title>Template Email</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }
        .email-container {
            max-width: 600px;
            margin: 20px auto;
            background-color: #ffffff;
            border: 1px solid #dddddd;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        .email-header {
            background-color: #4F46E5;
            color: white;
            text-align: center;
            padding: 20px;
        }
        .email-body {
            padding: 20px;
            color: #333333;
        }
        .email-body h1 {
            color: #4F46E5;
        }
        .email-body p {
            line-height: 1.6;
        }
        .email-footer {
            background-color: #f1f1f1;
            color: #666666;
            text-align: center;
            padding: 10px;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="email-container">
        <div class="email-header">
            <h1>OTP Akun Bestie Panti</h1>
        </div>
        <div class="email-body">
            <h1>Halo, ${name}!</h1>
            <p><b>JANGAN BERIKAN KODE OTP ke siapapun!</b></p>
            <p>Kode OTP Anda adalah: <strong>${otp}</strong></p>
            <p>Silakan gunakan kode OTP ini dalam waktu 90 detik.</p>
        </div>
        <div class="email-footer">
            <p>Jika Anda tidak meminta permintaan pengiriman kode OTP, abaikan email ini.</p>
            <p>Copyright © 2025 BestiePanti. All rights reserved.</p>
        </div>
    </div>
</body>
</html>