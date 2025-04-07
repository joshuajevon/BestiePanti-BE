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
        .verification-button {
            display: inline-block;
            padding: 10px 20px;
            margin: 20px 0;
            background-color: #4F46E5;
            color: #ffffff !important;
            text-decoration: none;
            border-radius: 5px;
            font-size: 16px;
            text-align: center;
            cursor: pointer;
            text-decoration: none;
        }
        .verification-button:hover {
            background-color: #3730A3;
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
            <h1>Verifikasi Email Anda</h1>
        </div>
        <div class="email-body">
            <h1>Halo, ${name}!</h1>
            <p><b>JANGAN BERIKAN Tautan Verifikasi ini ke siapapun!</b></p>
            <p>Kami menerima permintaan Anda untuk mengatur ulang email Anda.</p>
            <p>Silakan klik tombol di bawah ini untuk memverifikasi dan mengatur ulang email Anda:</p>

            <a href="${verificationLink}" class="verification-button">Verifikasi Email</a>
            <p>Harap gunakan tautan ini dalam waktu 90s sebelum kedaluwarsa.</p>
        </div>
        <div class="email-footer">
            <p>Jika Anda tidak meminta pengaturan ulang email, abaikan email ini.</p>
            <p>Copyright © 2025 BestiePanti. All rights reserved.</p>
        </div>
    </div>
</body>
</html>