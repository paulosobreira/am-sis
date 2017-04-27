var urlServico = "/am-sis/rest/arquivamento";
var token = localStorage.getItem("token");
$('#alert').remove();

$('#tipoArquivamentoDD').on('show.bs.dropdown', function() {
	listaTiposArquivamento();
});

$('#tipoExpurgoDD').on('show.bs.dropdown', function() {
	listaTiposExpurgo();
});

$('#salvar').bind("click", function() {
	salvar();
});

$('#remover').bind("click", function() {
	remover();
	limpar();
});

$('#limpar').bind("click", function() {
	limpar();
});

$('#pesquisar').bind("click", function() {
	window.location = "pesquisar.jsp";
});


$('#imprimir').bind("click", function() {
	imprimir();
});

var id = getParameter('id');
if(id){
	pesquisaArquivamento(id);
}

function limpar() {
	$('form')[0].reset();
	$('#tipoArquivamentoLabel').removeData('tipoArquivamento');
	$('#tipoArquivamentoLabel').html('Selecione');
	$('#tipoExpurgoLabel').removeData('tipoExpurgo');
	$('#tipoExpurgoLabel').html('Selecione');
	$('#logoLabel').removeData('logo');
	$('#logoLabel').html('Selecione');
}

function remover() {
		$.ajax({
				type : "DELETE",
				url : urlServico,
				headers: { 'token': token },
				data : JSON.stringify({
					id : $('#id').val()
				}),
				contentType : "application/json",
				dataType : "json",
				success : function(response) {
					$('#alert').remove();
					var sucesso = $('<div id="alert" class="alert alert-success" role="alert">Removido com sucesso</div>');
					$('#head').append(sucesso);
				},
				error : function(xhRequest, ErrorText, thrownError) {
					tratamentoErro(xhRequest);
				}
			});
}

function salvar() {
		$.ajax({
				type : "POST",
				url : urlServico,
				headers: { 'token': token },
				data : JSON.stringify({
					id : $('#id').val(),
					versao : $('#versao').val(),
					codigo : $('#codigo').val(),
					logo : $('#logoLabel').data('logo'),
					descricao : $('#descricao').val(),
					observacao : $('#observacao').val(),
					tipoArquivamento : $('#tipoArquivamentoLabel').data('tipoArquivamento'),
					tipoExpurgo : $('#tipoExpurgoLabel').data('tipoExpurgo'),
					dataExpurgoStr : $('#dataExpurgo').val(),
					dataReferenciaStr : $('#dataReferencia').val()
				}),
				contentType : "application/json",
				dataType : "json",
				success : function(response) {
					$('#alert').remove();
					var sucesso = $('<div id="alert" class="alert alert-success" role="alert">Salvo com sucesso.</div>');
					$('#head').append(sucesso);
					sucesso.focus();
					limpar();
				},
				error : function(xhRequest, ErrorText, thrownError) {
					tratamentoErro(xhRequest);
				}
			});
}

function listaTiposArquivamento() {
	var urlServico = "/am-sis/rest/tipoArquivamento";
	$.ajax({
		type : "GET",
		url : urlServico,
		headers: { 'token': token },
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			if(response.length==0){
				$('#alert').remove();
				var alerta = $('<div id="alert" class="alert alert-warning" role="alert">Tipo de arquivamento não cadastrado</div>');
				$('#head').append(alerta);
				return;
			}
			$('#tipoArquivamentoList').find('li').remove();
			$.each(response, function(i, val) {
				var li = $('<li><a>' + response[i].descricao + '</a></li>');
				li.bind("click", function() {
					$('#tipoArquivamentoLabel').data('tipoArquivamento' , response[i]);
					$('#tipoArquivamentoLabel').html(response[i].descricao);
				});
				$('#tipoArquivamentoList').append(li);
			});
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}

function listaTiposExpurgo() {
	var urlServico = "/am-sis/rest/tipoExpurgo";
	$.ajax({
		type : "GET",
		url : urlServico,
		headers: { 'token': token },
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			if(response.length==0){
				$('#alert').remove();
				var alerta = $('<div id="alert" class="alert alert-warning" role="alert">Tipo de expurgo não cadastrado</div>');
				$('#head').append(alerta);
				return;
			}
			$('#tipoExpurgoList').find('li').remove();
			$.each(response, function(i, val) {
				var li = $('<li><a>' + response[i].descricao + '</a></li>');
				li.bind("click", function() {
					$('#tipoExpurgoLabel').data('tipoExpurgo', response[i]);
					$('#tipoExpurgoLabel').html(response[i].descricao);
				});
				$('#tipoExpurgoList').append(li);
			});
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}


function pesquisaArquivamento(idP) {
	var urlServico = "/am-sis/rest/pesquisaArquivamento";
	$.ajax({
		type : "POST",
		url : urlServico,
		headers: { 'token': token },
		data : JSON.stringify({
			id : idP
		}),		
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#listaArquvamentos').find('tr').remove();
			if(response.length==0){
				$('#alert').remove();
				var alerta = $('<div id="alert" class="alert alert-warning" role="alert">Arquivamento não encontrado</div>');
				$('#head').append(alerta);
				return;
			}
			$('#id').val(response[0].id);
			$('#versao').val(response[0].versao);
			$('#codigo').val(response[0].codigo);
			$('#descricao').val(response[0].descricao);
			$('#observacao').val(response[0].observacao);
			$('#tipoArquivamentoLabel').data('tipoArquivamento',response[0].tipoArquivamento);
			$('#tipoArquivamentoLabel').html(response[0].tipoArquivamento.descricao);
			if(response[0].tipoExpurgo){
				$('#tipoExpurgoLabel').data('tipoExpurgo',response[0].tipoExpurgo);
				$('#tipoExpurgoLabel').html(response[0].tipoExpurgo.descricao);
			}
			$('#dataReferencia').val(response[0].dataReferenciaStr)
			$('#dataExpurgo').val(response[0].dataExpurgoStr);
			selecionaLogo(response[0].logo);
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}

function imprimir() {
	var array = new Array();
	array.push({
		id : $('#id').val(),
		codigo : $('#codigo').val(),
		logo : $('#logoLabel').data('logo'),
		descricao : $('#descricao').val(),
		observacao : $('#observacao').val(),
		tipoArquivamento : $('#tipoArquivamentoLabel').data('tipoArquivamento'),
		tipoExpurgo : $('#tipoExpurgoLabel').data('tipoExpurgo'),
		dataExpurgoStr : $('#dataExpurgo').val(),
		dataReferenciaStr : $('#dataReferencia').val()
	});
	var urlServico = "/am-sis/rest/relatorioArquivamento";
	$.ajax({
			type : "POST",
			url : urlServico+"/gerar",
			data : JSON.stringify(array),
			contentType : "application/json",
			dataType : "json",			
			headers: { 'token': token },
			success : function(response) {
				//alert(response);
				window.open(urlServico+"/imprimir/"+response);
			},
			error : function(xhRequest, ErrorText, thrownError) {
				tratamentoErro(xhRequest);
			}
		});
}

function selecionaLogo(logo){
	$('#logoLabel').data('logo', logo);
	if(logo==1){
		$('#logoLabel').html("StandShow");	
	}
	if(logo==2){
		$('#logoLabel').html("2LA");	
	}	
}