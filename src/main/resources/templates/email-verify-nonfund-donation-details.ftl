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
            <h1>Status Donasi Anda</h1>
        </div>
        <div class="email-body">
            <h1>Halo, ${name}!</h1>
            <p>Berikut status dari donasi yang Anda kirimkan:</p>
            <div class="status">
                <#if status == 'COMPLETED'>
                    <h2 style="color: green">Donasi Anda telah diterima dan terverifikasi!</h2>
                <#else>
                    <h2 style="color: red">Donasi Anda ditolak</h2>
                </#if>
                <#if status == "COMPLETED">
                    <p>Terima kasih atas dukungan Anda! Donasi Anda membantu kami dalam misi kemanusiaan ini.</p>
                <#else>
                    <p>Kami mohon maaf, donasi Anda tidak dapat kami terima pada saat ini.</p>
                </#if>
            </div>

            <p>Detail donasi:</p>
            <ul>
                <li>Tanggal Donasi: ${date}</li>
                <li>Tipe Donasi: ${type}</li>
                <li>Metode Donasi: ${isOnsite}</li>
                <li>Nama Orang: ${pic}</li>
                <li>Nomor Telepon: ${phone}</li>
                <li>Catatan: ${notes}</li>
            </ul>
            
            <p>Hormat kami,<br>Tim Bestie Panti</p>
        </div>
        <div class="email-footer">
            <p>Copyright © 2025 BestiePanti. All rights reserved.</p>
        </div>
    </div>
</body>
</html>