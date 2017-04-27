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
<link rel="stylesheet" href="css/am-sis.css">
<script src="jquery/jquery-3.1.1.min.js"></script>
<title>Am-Sis</title>
</head>
<body>
	<section class="container">
		<ul class="nav nav-pills" style="float: right; margin-top: 10px">
			<li id="sair" role="presentation"><a href="#"> <span
					class="glyphicon glyphicon-off" aria-hidden="true"></span> Sair
			</a></li>
		</ul>
		<h3 class="text-muted">Am-Sis</h3>
	</section>
	<section class="container">
		<div class="well well-sm">
			<h3>
				Olá, <b id="nomeUsuario"></b>
			</h3>
		</div>
		<div class="list-group">
			<a href="pages/arquivamento/manter.jsp" class="list-group-item">
				<button type="button" class="btn btn-default btn-block">Cadastra
					Arquivamento</button>
			</a> 
			<a href="pages/arquivamento/pesquisar.jsp" class="list-group-item">
				<button type="button" class="btn btn-default btn-block">Pesquisar
					Arquivamento</button>
			</a>
			<a href="pages/empresa/manterListar.jsp" class="list-group-item">
				<button type="button" class="btn btn-default btn-block">Empresa</button>
			</a> 			 
			<a href="pages/tipoArquivamento/manterListar.jsp"
				class="list-group-item">
				<button type="button" class="btn btn-default btn-block">Tipo de
					arquivamento</button>
			</a> 
			<a href="pages/tipoExpurgo/manterListar.jsp" class="list-group-item">
				<button type="button" class="btn btn-default btn-block">Tipo de
					expurgo</button>
			</a> 
			<a href="pages/usuario/manterListar.jsp" class="list-group-item">
				<button type="button" class="btn btn-default btn-block">Usuários</button>
			</a>
		</div>
	</section>
	<footer class="footer">
	      <div class="container">
	        <p class="text-muted">Am-Sis Versão: <%=Recursos.getProperties().getProperty("versao")%></p>
	      </div>
	 </footer>	
</body>
<script src="js/am-sis.js"></script>
</html>