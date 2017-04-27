<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="../../bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="../../css/am-sis.css">
<link rel="stylesheet"
	href="../../bootstrap/css/bootstrap-theme.min.css">
<script src="../../jquery/jquery-3.1.1.min.js"></script>
<script src="../../bootstrap/js/bootstrap.min.js"></script>
<title>Arquivamento</title>
</head>
<body>
	<section id="head" class="container">
		<ul class="nav nav-pills" style="float: right; margin-top: 10px">
			<li role="presentation"><a href="../../index.jsp"> <span
					class="glyphicon glyphicon-home" aria-hidden="true"></span> Menu
			</a></li>
		</ul>
		<h3 class="text-muted">
			Am-Sis <small>Arquivamento</small>
		</h3>
	</section>
	<section class="container">
		<form>
			<input type="text" id="id" class="hidden"> <input type="text"
				id="versao" class="hidden">
			<div class="row">
				<div class="form-group col-md-2">
					<label>Código</label> <input type="text" class="form-control"
						id="codigo" placeholder="Código">
				</div>
				<div class="form-group col-md-2">
					<label>Logotipo</label> 
					<div class="dropdown" id="logoDD">
						<button class="btn btn-default dropdown-toggle" type="button"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
							<span id="logoLabel">Selecione</span> <span class="caret"></span>
						</button>
						<ul id="logoList" class="dropdown-menu"
							aria-labelledby="dropdownMenu1">
							<li><a onclick="selecionaLogo(1)">StandShow</a></li>
							<li><a onclick="selecionaLogo(2)">2LA</a></li> 
						</ul>
					</div>
				</div>
				<div class="form-group col-md-8">
					<label>Descrição</label> <input type="text" class="form-control"
						id="descricao" placeholder="Descrição">
				</div>
			</div>
			<div class="form-group">
				<label>Observação</label>
				<textarea rows="5" class="form-control" id="observacao"
					placeholder="Observação"></textarea>
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
						class="form-control" id="dataReferencia" placeholder=">Mes Ano"
						required="required">
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
					<label>Data expurgo</label> <input type="date" class="form-control"
						id="dataExpurgo" placeholder=">Data expurgo" required="required">
				</div>
			</div>
			<button id="salvar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>
				Salvar
			</button>
			<button id="limpar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
				Limpar
			</button>
			<button id="pesquisar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
				Pesquisar
			</button>
			<button id="imprimir" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-print" aria-hidden="true"></span>
				Imprimir
			</button>
			<button id="remover" type="button" class="btn btn-warning pull-right">
				<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
				Remover
			</button>
		</form>

	</section>
</body>
<script src="../../js/am-sis.js"></script>
<script src="arquivamento.js"></script>
</html>