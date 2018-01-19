<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%@page import="br.com.am.recursos.Recursos"%>	
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="../../bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="../../css/am-sis.css?v=<%=Recursos.getProperties().getProperty("versao")%>">
<link rel="stylesheet"
	href="../../bootstrap/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="../../css/mdb-btns.css">
<script src="../../jquery/jquery-3.1.1.min.js"></script>
<script src="../../bootstrap/js/bootstrap.min.js"></script>
<title>Cadastro de usuário</title>
</head>
<body>
	<section id="head" class="container">
		<ul class="nav nav-pills" style="float: right; margin-top: 10px">
			<li role="presentation"><a href="../../index.jsp"> <span
					class="glyphicon glyphicon-home" aria-hidden="true"></span> Menu
			</a></li>
		</ul>
		<h3 class="text-muted">
			Am-Sis <small>Cadastro de usuário</small>
		</h3>
	</section>
	<section class="container">
		<form>
			<input type="text" id="id" class="hidden">
			<div class="form-group">
				<label>Nome</label> <input type="text" class="form-control"
					id="nome" placeholder="Nome" required="required">
			</div>
			<div class="form-group">
				<label>Login</label> <input type="text" class="form-control"
					id="login" placeholder="Login" required="required">
			</div>
			<div class="checkbox">
				<label> <input type="checkbox" id="ativo"> <b>Ativo</b>
				</label>
			</div>
			<div class="form-group">
				<label>Acesso</label> <input type="date" class="form-control"
					id="acesso" placeholder="acesso" required="required"
					readonly="readonly">
			</div>
			<button id="salvar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>
				Salvar
			</button>
			<button id="limpar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
				Limpar
			</button>
			<button id="remover" type="button" class="btn btn-warning pull-right">
				<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
				Apagar
			</button>
		</form>
	</section>
	<section class="container">
		<table class="table">
			<caption>Listagem de usuários</caption>
			<thead>
				<tr>
					<th>Login</th>
					<th>Nome</th>
					<th>Ativo</th>
					<th>Acesso</th>
				</tr>
			</thead>
			<tbody id="listaUsuarios">
			</tbody>
		</table>
	</section>
</body>
<script src="../../js/am-sis.js?v=<%=Recursos.getProperties().getProperty("versao")%>"></script>
<script src="usuario.js?v=<%=Recursos.getProperties().getProperty("versao")%>"></script>
</html>
