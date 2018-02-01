<%@page import="br.com.am.recursos.Recursos"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="css/mdb-btns.css">
<link rel="stylesheet" href="css/am-sis.css?v=<%=Recursos.getProperties().getProperty("versao")%>">
<script src="jquery/jquery-3.1.1.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<title>Login Am-Sis</title>
</head>
<body>
	<section id="head" class="container">
		<h3 class="text-muted">
			Am-Sis <small>Login</small>
		</h3>
	</section>
	<section class="container">
		<div class="well">Utilize Login:guest e Senha:guest para entrar como visitante</div>
		<form>
			<div class="form-group">
				<label>Login</label> <input type="text" class="form-control"
					id="login" placeholder="Login" required="required">
			</div>
			<div class="form-group">
				<label>Senha</label> <input type="password" class="form-control"
					id="senha" placeholder="Login" required="required">
			</div>			
			<button id="logar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon glyphicon-log-in" aria-hidden="true"></span>
				Login
			</button>
			<button id="logar-limpar" type="reset" class="btn btn-default">
				<span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
				Limpar
			</button>
		</form>
	</section>
	<footer class="footer">
	      <div class="container">
	        <p class="text-muted">Am-Sis Versão: <%=Recursos.getProperties().getProperty("versao")%></p>
	      </div>
	 </footer>		
</body>
<script src="js/am-sis.js?v=<%=Recursos.getProperties().getProperty("versao")%>"></script>
<script src="js/login.js?v=<%=Recursos.getProperties().getProperty("versao")%>"></script>
</html>
