var urlServico = "/am-sis/rest/tipoArquivamento";
var token = localStorage.getItem("token");
listaTiposArquivamento();
$('#alert').remove();

$('#salvar').bind("click", function() {
	salvar();
});

$('#remover').bind("click", function() {
	remover();
});

function limpar(){
	$('form')[0].reset();
}

function remover() {
	$.ajax({
		type : "DELETE",
		url : urlServico,
		headers: { 'token': token },
		data : JSON.stringify({
			id : $('#id').val(),
			descricao : $('#descricao').val()
		}),
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#alert').remove();
			var sucesso = $('<div id="alert" class="alert alert-success" role="alert">Removido com sucesso</div>');
			$('#head').append(sucesso);
			listaTiposArquivamento();
			limpar();
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}


function listaTiposArquivamento() {
	$.ajax({
		type : "GET",
		url : urlServico,
		headers: { 'token': token },
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#listaArquivamentos').find('tr').remove();
			$.each(response, function(i, val) {
				var td = $('<td/>');
				td.append(response[i].descricao);
				var tr = $('<tr style="cursor: pointer; cursor: hand" />');
				tr.append(td);
				$('#listaArquivamentos').append(tr);
				tr.bind("click", function() {
					$('#id').val(response[i].id);
					$('#descricao').val(response[i].descricao);
				});
			});
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
			descricao : $('#descricao').val()
		}),
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#alert').remove();
			var sucesso = $('<div id="alert" class="alert alert-success" role="alert">Salvo com sucesso</div>');
			$('#head').append(sucesso);
			limpar();
			listaTiposArquivamento();
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}



