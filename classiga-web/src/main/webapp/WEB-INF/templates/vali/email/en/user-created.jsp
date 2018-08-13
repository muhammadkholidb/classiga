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
            <p>A user account has been created for you with the following information:</p>
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
                        <td class="gray"><b>Password</b></td>
                        <td>${password}</td>
                    </tr>
                </tbody>
            </table>
            <p>
                You can login to the application with information listed above. 
                Please make sure you change your password after your first login if you feel need to. 
                <b><a href="http://home.bt.com/tech-gadgets/computing/8-tips-to-make-sure-your-passwords-are-strong-but-simple-11364015494778" target="_blank">Here</a></b> are some tips to create a strong but easy to remember password.</p>
            
            <p>Have a great day!</p>
        </div>
    </body>
</html>
