<%-- 
    Document   : user-created
    Created on : Aug 11, 2018, 12:50:53 PM
    Author     : muhammad
--%>
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <style>
            @media screen {
                /* latin-ext */
                @font-face {
                    font-family: 'Niconne';
                    font-style: normal;
                    font-weight: 400;
                    src: local('Niconne'), local('Niconne-Regular'), url(https://fonts.gstatic.com/s/niconne/v7/w8gaH2QvRug1_rTfnQKn2W4O.woff2) format('woff2');
                }
                /* latin */
                @font-face {
                    font-family: 'Niconne';
                    font-style: normal;
                    font-weight: 400;
                    src: local('Niconne'), local('Niconne-Regular'), url(https://fonts.gstatic.com/s/niconne/v7/w8gaH2QvRug1_rTfnQyn2Q.woff2) format('woff2');
                }
            }
            .content {
                font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
                font-size: 14px;
                line-height: 1.5;
                color: #333;
                background-color: #fff;
                margin: auto;
                max-width: 480px;
                padding: 0 15px;
            }
            .logo {
                font-family: 'Niconne';
                color: #512e90;
            }
            table {
                border-spacing: 0;
                border-collapse: collapse;
            }
            .table {
                width: 100%;
                max-width: 100%;
                margin-bottom: 20px;
            }
            .table-bordered {
                border: 1px solid #ddd;
            }
            .table-bordered>tbody>tr>td, 
            .table-bordered>tfoot>tr>td, 
            .table-bordered>thead>tr>td {
                border: 1px solid #ddd;
            }
            .table>tbody>tr>td, 
            .table>tfoot>tr>td, 
            .table>thead>tr>td {
                padding: 8px;
                vertical-align: middle;
                border-top: 1px solid #ddd;
            }
            .table-striped>tbody>tr:nth-of-type(odd) {
                background-color: #f9f9f9;
            }
            .gray {
                background-color: #f5f5f5;
            }
            a {
                color: #512e90;
                text-decoration: none;
            }
        </style>
    </head>
    <body>
        <div class="content">

            <div class="logo">
                <h1>${appName}</h1>
            </div>
            <p>Sebuah akun pengguna telah dibuat untuk Anda dengan informasi sebagai berikut:</p>
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <td class="gray"><b>Email</b></td>
                        <td>${loginEmail}</td>
                    </tr>
                    <tr>
                        <td class="gray"><b>Username</b></td>
                        <td>${loginUsername}</td>
                    </tr>
                    <tr>
                        <td class="gray"><b>Kata Sandi</b></td>
                        <td>${password}</td>
                    </tr>
                </tbody>
            </table>
            <p>
                Anda dapat masuk ke aplikasi dengan informasi yang tercantum di atas. 
                Harap pastikan Anda mengubah kata sandi setelah login pertama Anda jika Anda merasa perlu. 
                <b><a href="https://securityinabox.org/id/guide/passwords/" target="_blank">Berikut</a></b> adalah beberapa kiat untuk membuat kata sandi yang kuat namun mudah diingat.</p>

            <p>Semoga hari Anda menyenangkan!</p>
        </div>
    </body>
</html>
