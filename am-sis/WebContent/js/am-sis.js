if( (localStorage.getItem("token")==null && 
		window.location.href.indexOf("login.jsp")==-1)){
	localStorage.setItem("url", window.location.href);
	window.location.href = "/am-sis/login.jsp";
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

(function($) {
    $(".ripple-effect").click(function(e){
        var rippler = $(this);

        // create .ink element if it doesn't exist
        if(rippler.find(".ink").length == 0) {
            rippler.append("<span class='ink'></span>");
        }

        var ink = rippler.find(".ink");

        // prevent quick double clicks
        ink.removeClass("animate");

        // set .ink diametr
        if(!ink.height() && !ink.width())
        {
            var d = Math.max(rippler.outerWidth(), rippler.outerHeight());
            ink.css({height: d, width: d});
        }

        // get click coordinates
        var x = e.pageX - rippler.offset().left - ink.width()/2;
        var y = e.pageY - rippler.offset().top - ink.height()/2;

        // set .ink position and add class .animate
        ink.css({
          top: y+'px',
          left:x+'px'
        }).addClass("animate");
    })
})(jQuery);