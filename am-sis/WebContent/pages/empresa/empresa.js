var urlServico = "/am-sis/rest/empresa";
var token = localStorage.getItem("token");
listaEmpresas();

$('#alert').remove();
$('#logoSalvo').hide();

$('#salvar').bind("click", function() {
	salvar();
});

$('#remover').bind("click", function() {
	remover();
});

$('#limpar').bind("click", function() {
	limpar();
});

function limpar() {
	$('form')[0].reset();
	$('#logoSalvoImg').attr('src','');
	$('#logoSalvo').hide();
	
}

function remover() {
	$.ajax({
		type : "DELETE",
		url : urlServico,
		headers : {
			'token' : token
		},
		data : JSON.stringify({
			id : $('#id').val(),
			nome : $('#nome').val()
		}),
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#alert').remove();
			var sucesso = $('<div id="alert" class="alert alert-success" role="alert">Removido com sucesso</div>');
			$('#head').append(sucesso);
			listaEmpresas();
			limpar();
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}

function listaEmpresas() {
	$.ajax({
		type : "GET",
		url : urlServico,
		headers : {
			'token' : token
		},
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#listaEmpresa').find('tr').remove();
			$.each(response, function(i, val) {
				var td = $('<td/>');
				td.append(response[i].nome);
				var tr = $('<tr style="cursor: pointer; cursor: hand" />');
				tr.append(td);
				$('#listaEmpresa').append(tr);
				tr.bind("click", function() {
					$('#alert').remove();
					$('#id').val(response[i].id);
					$('#nome').val(response[i].nome);
					if(response[i].arquivo){
						$('#logoSalvoImg').attr('src','/am-sis/rest/binario/downloadImg?id='+response[i].arquivo.id);
						$('#logoSalvo').show();
					}else{
						$('#logoSalvo').hide();
					}
				});
			});
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}

function salvar() {
	if ($('#input-id')[0].files.length>0) {
		var data = new FormData();
		$.each($('#input-id')[0].files, function(i, file) {
			data.append('file', file);
		});
		$.ajax({
			url : '/am-sis/rest/binario/upload',
			headers : {
				'token' : token
			},
			data : data,
			cache : false,
			contentType : false,
			processData : false,
			type : 'POST',
			success : function(data) {
				salvarEmpresa(data.id);
			},
			error : function(xhRequest, ErrorText, thrownError) {
				tratamentoErro(xhRequest);
			}
		});
	}else{
		salvarEmpresa(null);
	}
}

function salvarEmpresa(idArquivo) {
	var dataObj;
	if(idArquivo){
		dataObj = {
				id : $('#id').val(),
				nome : $('#nome').val(),
				arquivo : { id : idArquivo }
			};
	}else{
		dataObj = {
				id : $('#id').val(),
				nome : $('#nome').val()
			};		
	}
	$.ajax({
			type : "POST",
			url : urlServico,
			headers : {
				'token' : token
			},
			data : JSON.stringify(dataObj),
			contentType : "application/json",
			dataType : "json",
			success : function(response) {
				$('#alert').remove();
				var sucesso = $('<div id="alert" class="alert alert-success" role="alert">Salvo com sucesso</div>');
				$('#head').append(sucesso);
				limpar();
				listaEmpresas();
			},
			error : function(xhRequest, ErrorText, thrownError) {
				tratamentoErro(xhRequest);
			}
		});
}