var urlServico = "/am-sis/rest/tipoExpurgo";
var token = localStorage.getItem("token");
listaTiposExpurgo();

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
			toaster('Removido com sucesso.', 2000, 'alert alert-success');
			limpar();
			listaTiposExpurgo();
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}


function listaTiposExpurgo() {
	$.ajax({
		type : "GET",
		url : urlServico,
		headers: { 'token': token },
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#listaExpurgos').find('tr').remove();
			$.each(response, function(i, val) {
				var td = $('<td/>');
				td.append(response[i].descricao);
				var tr = $('<tr style="cursor: pointer; cursor: hand" />');
				tr.append(td);
				$('#listaExpurgos').append(tr);
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
			toaster('Salvo com sucesso.', 2000, 'alert alert-success');
			listaTiposExpurgo();
			limpar();
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}



