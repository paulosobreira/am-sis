if( (window.location.pathname.indexOf("login.jsp")==-1)
		&& localStorage.getItem("token")==null){
	localStorage.setItem("url", window.location);
	window.location = "/am-sis/login.jsp";
}

$('#sair').bind("click", function() {
	localStorage.clear();
	window.location = "login.jsp";
});

var nomeUsuario = localStorage.getItem("nome");
if (nomeUsuario) {
	$('#nomeUsuario').html(nomeUsuario);
}

function tratamentoErro(xhRequest) {
	var erro;
	$('#alert').remove();
	if (xhRequest.status == 401) {
		erro = $('<div id="alert" class="alert alert-warning" role="alert">Sessão expirada <a href="../../login.jsp">Re-logar?</a></div>');
	} else {
		erro = $('<div id="alert" class="alert alert-danger" role="alert">Erro no servidor : '
				+ xhRequest.status + '  ' + xhRequest.responseText + '</div>');
	}
	erro.focus();
	$('#head').append(erro);
	$("html, body").animate({
		scrollTop : 0
	}, "slow");
}

function getParameter(val) {
	var result = null, tmp = [];
	var items = location.search.substr(1).split("&");
	for (var index = 0; index < items.length; index++) {
		tmp = items[index].split("=");
		if (tmp[0] === val)
			result = decodeURIComponent(tmp[1]);
	}
	return result;
}

var loader = $('<div class="loader"></div>');
$('body').prepend(loader);
var $loading = loader.hide();
$(document).ajaxStart(function() {
	$("button").prop("disabled",true);
	$("a").prop("disabled",true);
	$loading.show();
}).ajaxStop(function() {
	$("button").prop("disabled",false);
	$("a").prop("disabled",false);
	$loading.hide();
});

jQuery(window).on('error', function(e) {
	$loading.hide();
	// This tells jQuery no more ajax Requests are active
	// (this way Global start/stop is triggered again on next Request)
	jQuery.active = 0;
	// Do something to handle the error
});