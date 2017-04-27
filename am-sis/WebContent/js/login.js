var urlServico = "/am-sis/rest/login";

$('#logar').bind("click", function() {
	logar();
});

$('#limpar').bind("click", function() {
	limpar();
});

function limpar(){
	$('form')[0].reset();
}

function logar() {
	$.ajax({
		type : "POST",
		url : urlServico,
		data : JSON.stringify({
			login : $('#login').val(), 
			senha : $('#senha').val()
		}),
		contentType : "application/json",
		dataType : "json",
		success : function(response) {
			localStorage.setItem("nome", response.nome);
			localStorage.setItem("token", response.token);
			window.location = "index.jsp";
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}