var token = localStorage.getItem("token");

$('#tipoArquivamentoDD').on('show.bs.dropdown', function() {
	listaTiposArquivamento();
});

$('#tipoExpurgoDD').on('show.bs.dropdown', function() {
	listaTiposExpurgo();
});

$('#pesquisar').bind("click", function() {
	pesquisaArquivamento();
});

$('#limpar').bind("click", function() {
	limpar();
});

$('#imprimir').bind("click", function() {
	imprimir();
});

function limpar() {
	$('form')[0].reset();
	$('#tipoArquivamentoLabel').removeData('tipoArquivamento');
	$('#tipoArquivamentoLabel').html('Selecione');
	$('#tipoExpurgoLabel').removeData('tipoExpurgo');
	$('#tipoExpurgoLabel').html('Selecione');
	$('#listaArquvamentos').find('tr').remove();
}

function pesquisaArquivamento() {
	var urlServico = "/am-sis/rest/pesquisaArquivamento";
	$.ajax({
		type : "POST",
		url : urlServico,
		headers: { 'token': token },
		data : JSON.stringify({
			id : $('#sequencia').val(),
			codigo : $('#codigo').val(),
			descricao : $('#descricao').val(),
			tipoArquivamento : $('#tipoArquivamentoLabel').data('tipoArquivamento'),
			tipoExpurgo : $('#tipoExpurgoLabel').data('tipoExpurgo'),
			dataReferenciaStr : $('#dataReferencia').val(),
			dataExpurgoStrINI : $('#dataExpurgoStrINI').val(),
			dataExpurgoStrFIM : $('#dataExpurgoStrFIM').val()
		}),		
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#listaArquvamentos').find('tr').remove();
			if(response.length==0){
				toaster('Nenhum arquivamento encontrado.', 2000, 'alert alert-warning');
				return;
			}
			$.each(response, function(i, val) {
				var td2 = $('<td/>');
				td2.append(response[i].codigo);
				var td3 = $('<td/>');
				td3.append(response[i].descricao);
				var td4 = $('<td/>');
				td4.addClass('hidden-xs');
				td4.append(response[i].tipoArquivamento.descricao);
				var td5 = $('<td/>');
				td5.addClass('hidden-xs');
				if(response[i].dataReferenciaStr){
					td5.append(response[i].dataReferenciaStr);
				}
				var tr = $('<tr class="selecionaArquivamento"/>');
				tr.append(td2);
				tr.append(td3);
				tr.append(td4);
				tr.append(td5);
				$('#listaArquvamentos').append(tr);
				tr.data('arquivamento', response[i]);
				tr.bind("click", function() {
					window.location = "manter.jsp?id=" + response[i].id;
				});
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

function imprimir() {
	var array = new Array();
	$.each($('.selecionaArquivamento'), function() {
		array.push($(this).data('arquivamento'));
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
				window.open(urlServico+"/imprimir/"+response);
			},
			error : function(xhRequest, ErrorText, thrownError) {
				tratamentoErro(xhRequest);
			}
		});
}
