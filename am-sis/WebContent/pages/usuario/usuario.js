var urlServico = "/am-sis/rest/usuario";
var token = localStorage.getItem("token");
listaUsuarios();

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

function limpar(){
	$('form')[0].reset();
	$('#login').prop('readonly',false);
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
			toaster('Usuário inativado com sucesso.', 2000, 'alert alert-success');
			listaUsuarios();
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}


function listaUsuarios() {
	$.ajax({
		type : "GET",
		url : urlServico,
		headers: { 'token': token },		
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			$('#listaUsuarios').find('tr').remove();
			$.each(response, function(i, val) {
				var td1 = $('<td scope="row"/>');
				td1.append(response[i].login);
				var td2 = $('<td/>');
				td2.append(response[i].nome);
				var td3 = $('<td/>');
				td3.append(response[i].ativo?'Sim':'Não');
				var td4 = $('<td/>');
				td4.append(response[i].acessoStr);
				var tr = $('<tr style="cursor: pointer; cursor: hand" />');
				tr.append(td1);
				tr.append(td2);
				tr.append(td3);
				tr.append(td4);
				$('#listaUsuarios').append(tr);
				tr.bind("click", function() {
					$('#id').val(response[i].id);
					$('#nome').val(response[i].nome);
					$('#login').val(response[i].login);
					$('#login').prop('readonly',true);
					$('#ativo').prop('checked',response[i].ativo)
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
			nome : $('#nome').val(),
			login : $('#login').val(), 
			ativo : $('#ativo').prop('checked')
		}),
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			toaster('Salvo com sucesso Senha Gerada : '+response.senhaStr, 90000, 'alert alert-success');
			limpar();
			listaUsuarios();
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}



