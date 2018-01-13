var urlServico = "/am-sis/rest/arquivamento";
var token = localStorage.getItem("token");

$('#empresaDD').on('show.bs.dropdown', function() {
	listaEmpresa();
});

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
	$('#empresaLabel').removeData('empresa');
	$('#empresaLabel').html('Selecione');
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
					toaster('Removido com sucesso.', 2000, 'alert alert-success');
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
					descricao : $('#descricao').val(),
					observacao : $('#observacao').val(),
					tipoArquivamento : $('#tipoArquivamentoLabel').data('tipoArquivamento'),
					tipoExpurgo : $('#tipoExpurgoLabel').data('tipoExpurgo'),
					empresa : $('#empresaLabel').data('empresa'),
					dataExpurgoStr : $('#dataExpurgo').val(),
					dataReferenciaStr : $('#dataReferencia').val()
				}),
				contentType : "application/json",
				dataType : "json",
				success : function(response) {
					toaster('Salvo com sucesso.', 2000, 'alert alert-success');
					sucesso.focus();
					limpar();
				},
				error : function(xhRequest, ErrorText, thrownError) {
					tratamentoErro(xhRequest);
				}
			});
}

function listaEmpresa() {
	var urlServico = "/am-sis/rest/empresa";
	$.ajax({
		type : "GET",
		url : urlServico,
		headers: { 'token': token },
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			if(response.length==0){
				toaster('Empresa não cadastrada.', 2000, 'alert alert-warning');
				return;
			}
			$('#empresaList').find('li').remove();
			$.each(response, function(i, val) {
				var li = $('<li><a>' + response[i].nome + '</a></li>');
				li.bind("click", function() {
					$('#empresaLabel').data('empresa' , response[i]);
					$('#empresaLabel').html(response[i].nome);
				});
				$('#empresaList').append(li);
			});
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
				toaster('Tipo de arquivamento não cadastrado.', 2000, 'alert alert-warning');
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
				toaster('Tipo de expurgo não cadastrado.', 2000, 'alert alert-warning');
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
				toaster('Arquivamento não encontrado.', 2000, 'alert alert-warning');
				return;
			}
			$('#id').val(response[0].id);
			$('#versao').val(response[0].versao);
			$('#codigo').val(response[0].codigo);
			$('#descricao').val(response[0].descricao);
			$('#observacao').val(response[0].observacao);
			$('#tipoArquivamentoLabel').data('tipoArquivamento',response[0].tipoArquivamento);
			$('#tipoArquivamentoLabel').html(response[0].tipoArquivamento.descricao);
			$('#empresaLabel').data('empresa',response[0].empresa);
			$('#empresaLabel').html(response[0].empresa.nome);
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
		empresa : $('#empresaLabel').data('empresa'),
		tipoExpurgo : $('#tipoExpurgoLabel').data('tipoExpurgo'),
		dataExpurgoStr : $('#dataExpurgo').val(),
		dataReferenciaStr : $('#dataReferencia').val()
	});
	debugger;
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