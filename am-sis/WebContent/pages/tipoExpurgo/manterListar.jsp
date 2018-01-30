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
<title>Tipo de arquivamento</title>
</head>
<body>
	<section id="head" class="container">
		<ul class="nav nav-pills" style="float: right; margin-top: 10px">
			<li role="presentation"><a href="../../index.jsp"> <span
					class="glyphicon glyphicon-home" aria-hidden="true"></span> Menu
			</a></li>
		</ul>
		<h3 class="text-muted">
			Am-Sis <small>Tipo de expurgo</small>
		</h3>
	</section>
	<section class="container">
		<form>
			<input type="text" id="id" class="hidden">
			<div class="form-group">
				<label>Descrição</label> <input type="text" class="form-control"
					id="descricao" placeholder="Descrição" required="required">
			</div>
			<button id="salvar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>
				<span>Salvar</span>
			</button>
			<button id="limpar" type="reset" class="btn btn-default">
				<span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
				<span>Limpar</span>
			</button>
			<button id="remover" type="button" class="btn btn-warning pull-right">
				<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
				<span>Apagar</span>
			</button>
		</form>
	</section>
	<section class="container">
		<table class="table">
			<caption>Listagem de tipos de expurgo</caption>
			<thead>
				<tr>
					<th>Descrição</th>
				</tr>
			</thead>
			<tbody id="listaExpurgos">
			</tbody>
		</table>
	</section>
</body>
<script src="../../js/am-sis.js?v=<%=Recursos.getProperties().getProperty("versao")%>"></script>
<script src="tipoExpurgo.js?v=<%=Recursos.getProperties().getProperty("versao")%>"></script>
</html>
