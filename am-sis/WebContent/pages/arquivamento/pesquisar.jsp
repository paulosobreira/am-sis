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
<title>Pesquisar arquivamento</title>
</head>
<body>
	<section id="head" class="container">
		<ul class="nav nav-pills" style="float: right; margin-top: 10px">
			<li role="presentation"><a href="../../index.jsp"> <span
					class="glyphicon glyphicon-home" aria-hidden="true"></span> Menu
			</a></li>
		</ul>
		<h3 class="text-muted">
			Am-Sis <small>Pesquisar arquivamento</small>
		</h3>
	</section>
	<section class="container">
		<form>
			<input type="text" id="id" class="hidden">
			<div class="row">
				<div class="form-group col-md-4">
					<label>Código</label> <input type="text" class="form-control"
						id="codigo" placeholder="Código">
				</div>
				<div class="form-group col-md-8">
					<label>Descrição</label> <input type="text" class="form-control"
						id="descricao" placeholder="Descrição">
				</div>
			</div>
			<div class="row">
				<div class="form-group col-md-4">
					<label>Tipo de arquivamento</label>
					<div class="dropdown" id="tipoArquivamentoDD">
						<button class="btn btn-default dropdown-toggle" type="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
							<span id="tipoArquivamentoLabel">Selecione</span> <span
								class="caret"></span>
						</button>
						<ul id="tipoArquivamentoList" class="dropdown-menu"
							aria-labelledby="dropdownMenu1">
						</ul>
					</div>
				</div>
				<div class="form-group col-md-8">
					<label>Data referêcia</label> <input type="month"
						class="form-control" id="dataReferencia" placeholder=">Mes Ano">
				</div>
			</div>
			<div class="row">
				<div class="form-group col-md-4">
					<label>Tipo de expurgo</label>
					<div class="dropdown" id="tipoExpurgoDD">
						<button class="btn btn-default dropdown-toggle" type="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
							<span id="tipoExpurgoLabel">Selecione</span> <span class="caret"></span>
						</button>
						<ul id="tipoExpurgoList" class="dropdown-menu"
							aria-labelledby="dropdownMenu1">
						</ul>
					</div>
				</div>
				<div class="form-group col-md-8">
					<label>Data expurgo</label>
					<div class="row">
						<div class="form-group col-md-6">
							<input type="date" class="form-control" id="dataExpurgoStrINI"
								placeholder=">Data expurgo inicio">
						</div>
						<div class="form-group col-md-6">
							<input type="date" class="form-control" id="dataExpurgoStrFIM"
								placeholder=">Data expurgo fim">
						</div>
					</div>
				</div>
			</div>
			<button id="pesquisar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
				Pesquisar
			</button>
			<button id="limpar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
				Limpar
			</button>
			<button id="imprimir" type="button" class="btn btn-default hidden">
				<span class="glyphicon glyphicon-print" aria-hidden="true"></span>
				Imprimir
			</button>
		</form>
	</section>
	<section class="container">
		<table class="table">
			<caption>Listagem de Arquivos</caption>
			<thead>
				<tr>
					<th>Código</th>
					<th>Descrição</th>
					<th class="hidden-xs">Tipo Arquivamento</th>
					<th class="hidden-xs">Data Referência</th>
				</tr>
			</thead>
			<tbody id="listaArquvamentos">

			</tbody>
		</table>
	</section>
</body>
<script src="../../js/am-sis.js?v=<%=Recursos.getProperties().getProperty("versao")%>"></script>
<script src="pesquisarArquivamento.js?v=<%=Recursos.getProperties().getProperty("versao")%>"></script>
</html>