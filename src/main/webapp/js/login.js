var urlServico = "/am-sis/rest/login";

var loginSalvo = localStorage.getItem("loginSalvo");
var senhaSalva = localStorage.getItem("senhaSalva");
if (loginSalvo) $('#login').val(loginSalvo);
if (senhaSalva) $('#senha').val(senhaSalva);

$('#toggleSenha').bind("click", function() {
	var campo = $('#senha');
	var icone = $('#iconeSenha');
	if (campo.attr('type') === 'password') {
		campo.attr('type', 'text');
		icone.removeClass('glyphicon-eye-open').addClass('glyphicon-eye-close');
	} else {
		campo.attr('type', 'password');
		icone.removeClass('glyphicon-eye-close').addClass('glyphicon-eye-open');
	}
});

$('#logar').bind("click", function() {
	logar();
});

$('#logar-limpar').bind("click", function() {
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
			localStorage.setItem("loginSalvo", $('#login').val());
			localStorage.setItem("senhaSalva", $('#senha').val());
			localStorage.setItem("nome", response.nome);
			localStorage.setItem("token", response.token);
			localStorage.setItem("darkMode", response.darkMode === true ? "true" : "false");
			var url = localStorage.getItem("url");
			if( url!=null && url.indexOf("login.jsp")==-1 && !url.endsWith('/')){
				window.location.href = localStorage.getItem("url");
			}else{
				window.location.href = "index.jsp";
			}
		},
		error : function(xhRequest, ErrorText, thrownError) {
			tratamentoErro(xhRequest);
		}
	});
}
