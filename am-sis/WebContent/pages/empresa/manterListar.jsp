<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="../../bootstrap/css/bootstrap.min.css">
<link rel="stylesheet"
	href="../../bootstrap/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="../../css/am-sis.css">
<script src="../../jquery/jquery-3.1.1.min.js"></script>
<script src="../../bootstrap/js/bootstrap.min.js"></script>
<link href="../../fileinput/css/fileinput.min.css" media="all" rel="stylesheet" type="text/css" />
<!-- canvas-to-blob.min.js is only needed if you wish to resize images before upload.
     This must be loaded before fileinput.min.js -->
<script src="../../fileinput/js/plugins/canvas-to-blob.min.js" type="text/javascript"></script>
<!-- sortable.min.js is only needed if you wish to sort / rearrange files in initial preview.
     This must be loaded before fileinput.min.js -->
<script src="../../fileinput/js/plugins/sortable.min.js" type="text/javascript"></script>
<!-- purify.min.js is only needed if you wish to purify HTML content in your preview for HTML files.
     This must be loaded before fileinput.min.js -->
<script src="../../fileinput/js/plugins/purify.min.js" type="text/javascript"></script>
<!-- the main fileinput plugin file -->
<script src="../../fileinput/js/fileinput.min.js"></script>
<!-- optionally if you need translation for your language then include 
    locale file as mentioned below -->
<script src="../../fileinput/js/locales/pt-BR.js"></script>

<title>Empresa</title>
</head>
<body>
	<section id="head" class="container">
		<ul class="nav nav-pills" style="float: right; margin-top: 10px">
			<li role="presentation"><a href="../../index.jsp"> <span
					class="glyphicon glyphicon-home" aria-hidden="true"></span> Menu
			</a></li>
		</ul>
		<h3 class="text-muted">
			Am-Sis <small>Empresa</small>
		</h3>
	</section>
	<section class="container">
		<form>
			<input type="text" id="id" class="hidden">
			<div class="form-group">
				<label>Nome</label> <input type="text" class="form-control"
					id="nome" placeholder="Nome" required="required">
			</div>
			<div class="form-group" id="logoSalvo">
				<label>Logo Salvo</label> 
				<img id="logoSalvoImg" class="img-responsive" />
			</div>				
			<div class="form-group" id="logoNovo">
				<label>Logo Novo</label> 
				<input id="input-id" type="file" class="file"  name="file" data-preview-file-type="text" >
			</div>
			<button id="salvar" type="button" class="btn btn-default">
				<span class="glyphicon glyphicon-floppy-disk" aria-hidden="true"></span>
				Salvar
			</button>
			<button id="limpar" type="reset" class="btn btn-default">
				<span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span>
				Limpar
			</button>
			<button id="remover" type="button" class="btn btn-warning pull-right">
				<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
				Remover
			</button>
		</form>
	</section>
	<section class="container">
		<table class="table">
			<caption>Listagem de Empresas</caption>
			<thead>
				<tr>
					<th>Nome</th>
				</tr>
			</thead>
			<tbody id="listaEmpresa">
			</tbody>
		</table>
	</section>
</body>
<script type="text/javascript">
	$("#input-id").fileinput({'showUpload':false, 'previewFileType':'any'});
</script>
<script src="../../js/am-sis.js"></script>
<script src="empresa.js"></script>
</html>
