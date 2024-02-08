if ((localStorage.getItem("token") == null && window.location.href
		.indexOf("login.jsp") == -1)) {
	localStorage.setItem("url", window.location.href);
	window.location.href = "/am-sis/login.jsp";
}

btnMobile();

$('#sair').bind("click", function() {
	localStorage.clear();
	window.location = "login.jsp";
});

var nomeUsuario = localStorage.getItem("nome");
if (nomeUsuario) {
	$('#nomeUsuario').html(nomeUsuario);
}

function btnMobile() {
	if (window.innerWidth > window.innerHeight) {
		return;
	}
	var div = $('<div>');
	if ($('#salvar')) {
		$('#salvar').addClass('btn-circle');
		$('#salvar').children().slice(1).detach();
		div.append($('#salvar'));
	}
	if ($('#limpar')) {
		$('#limpar').addClass('btn-circle');
		$('#limpar').children().slice(1).detach();
		div.append($('#limpar'));
	}
	if ($('#pesquisar')) {
		$('#pesquisar').addClass('btn-circle');
		$('#pesquisar').children().slice(1).detach();
		div.append($('#pesquisar'));
	}
	if ($('#imprimir')) {
		$('#imprimir').addClass('btn-circle');
		$('#imprimir').children().slice(1).detach();
		div.append($('#imprimir'));
	}
	if ($('#remover')) {
		$('#remover').addClass('btn-circle');
		$('#remover').children().slice(1).detach();
		div.append($('#remover'));
	}
	div.addClass('floatDiv');

	$('body').append(div);
}

function tratamentoErro(xhRequest) {
	if (xhRequest.status == 401) {
		toaster('Sem sessão voltando ao inicio...', 2000, 'alert alert-danger');
		setTimeout(function() {
			localStorage.clear();
			window.location.href = "/am-sis/login.jsp";
		}, 3000);
	} else {
		console.log(xhRequest.status + '  ' + xhRequest.responseText);
		var erroMsg = xhRequest.responseText;
		if (xhRequest.responseJSON != null
				&& xhRequest.responseJSON.messageString != null) {
			erroMsg = xhRequest.responseJSON.messageString;
		}
		toaster(erroMsg, 3000, 'alert alert-danger');
	}
}

function toaster(msg, tempo, classe) {
	$('#snackbar').remove();
	var toast = $('<div id="snackbar" class="show ' + classe
			+ '" role="alert">' + msg + '</div>');
	$('#head').append(toast);
	setTimeout(function() {
		$('#snackbar').remove();
	}, tempo);
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
	$("button").prop("disabled", true);
	$("a").prop("disabled", true);
	$loading.show();
}).ajaxStop(function() {
	$("button").prop("disabled", false);
	$("a").prop("disabled", false);
	$loading.hide();
});

jQuery(window).on('error', function(e) {
	$loading.hide();
	// This tells jQuery no more ajax Requests are active
	// (this way Global start/stop is triggered again on next Request)
	jQuery.active = 0;
	// Do something to handle the error
});

(function($) {
	$(".ripple-effect").click(function(e) {
		var rippler = $(this);

		// create .ink element if it doesn't exist
		if (rippler.find(".ink").length == 0) {
			rippler.append("<span class='ink'></span>");
		}

		var ink = rippler.find(".ink");

		// prevent quick double clicks
		ink.removeClass("animate");

		// set .ink diametr
		if (!ink.height() && !ink.width()) {
			var d = Math.max(rippler.outerWidth(), rippler.outerHeight());
			ink.css({
				height : d,
				width : d
			});
		}

		// get click coordinates
		var x = e.pageX - rippler.offset().left - ink.width() / 2;
		var y = e.pageY - rippler.offset().top - ink.height() / 2;

		// set .ink position and add class .animate
		ink.css({
			top : y + 'px',
			left : x + 'px'
		}).addClass("animate");
	})
})(jQuery);