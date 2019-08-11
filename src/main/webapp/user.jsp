<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>User Java Bean Page</title>
</head>
<body>
<div>
  <p>Id: ${user.id}</p>
  <p>Login: ${user.login}</p>
  <p>Name: ${user.name}</p>
  <p>Email: ${user.email}</p>
  <p>SessionID: ${cookie.JSESSIONID.value}</p>
  <p>User-Agent: ${header["User-Agent"]}</p>
</div>
</body>
</html>
