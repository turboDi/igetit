<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
</head>

<body style="background-color: red">
    <div style="text-align: center; padding-top: 20px">
        <h1>Welcome to IGetIt, ${p.username},
        <g:link absolute="true"
                controller="account"
                action="verify"
                params="[key: p.confirmToken, email: p.email]">verify your email</g:link></h1>
    </div>
</body>
</html>