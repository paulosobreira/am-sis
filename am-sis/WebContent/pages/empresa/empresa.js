var urlServico = "/am-sis/rest/empresa";
var token = localStorage.getItem("token");
listaEmpresas();

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
			toaster('Removido com sucesso.', 2000, 'alert alert-success');
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
					$('#id').val(response[i].id);
					$('#nome').val(response[i].nome);
					if(response[i].idArquivo){
						$('#logoSalvoImg').attr('src','/am-sis/rest/binario/downloadImg?id='+response[i].idArquivo);
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

function salvarEmpresa(idArq) {
	var dataObj;
	if(idArq){
		dataObj = {
				id : $('#id').val(),
				nome : $('#nome').val(),
				idArquivo : idArq
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
				toaster('Salvo com sucesso.', 2000, 'alert alert-success');
				limpar();
				listaEmpresas();
			},
			error : function(xhRequest, ErrorText, thrownError) {
				tratamentoErro(xhRequest);
			}
		});
}