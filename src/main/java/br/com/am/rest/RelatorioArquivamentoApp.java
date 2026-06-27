package br.com.am.rest;

import br.com.am.entidades.Arquivamento;
import br.com.am.util.HibernateUtil;
import br.com.am.util.RelatorioEngine;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/relatorioArquivamento")
public class RelatorioArquivamentoApp extends br.com.am.rest.RestApp {

	private static final Logger log = LoggerFactory.getLogger(RelatorioArquivamentoApp.class);

	@Context
	private HttpServletRequest servletRequest;

	private static final Map<Long, String> relatorios = new HashMap<>();

	@POST
	@Path("/gerar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response gerar(@HeaderParam("token") String token,
			List<Arquivamento> arquivamentos) {
		if (arquivamentos == null || arquivamentos.isEmpty()) {
			return Response.status(400).entity("Relatório Vazio").build();
		}
		limpaRelatoriosAntigos();

		String baseUrl = extrairBaseUrl();
		for (Arquivamento arq : arquivamentos) {
			if (arq.getEmpresa() != null && arq.getEmpresa().getIdArquivo() != null) {
				arq.setLogo(baseUrl + arq.getEmpresa().getIdArquivo());
			}
		}

		try {
			String html = RelatorioEngine.renderArquivamento(arquivamentos);
			Long chave = System.currentTimeMillis();
			relatorios.put(chave, html);
			return Response.status(200).entity(chave).build();
		} catch (Exception e) {
			log.error("Erro ao gerar relatório", e);
			return Response.status(500).entity("Erro gerando relatório").build();
		}
	}

	@GET
	@Path("/imprimir/{chave}")
	@Produces("text/html;charset=UTF-8")
	public Response imprimir(@PathParam("chave") Long chave) {
		String html = relatorios.get(chave);
		if (html == null) {
			return Response.status(404).entity("Relatório não encontrado ou expirado").build();
		}
		return Response.ok(html, "text/html;charset=UTF-8").build();
	}

	@GET
	@Path("/gerarHtml/{id}")
	@Produces("text/html;charset=UTF-8")
	public Response gerarHtmlPorId(@HeaderParam("token") String token,
			@PathParam("id") String id) {
		if (id == null) {
			return Response.status(400).entity("ID inválido").build();
		}
		return gerarHtmlInterno(Long.parseLong(id));
	}

	@GET
	@Path("/gerarPdf/{id}")
	@Produces("text/html;charset=UTF-8")
	public Response gerarPDfPorId(@HeaderParam("token") String token,
			@PathParam("id") String id) {
		if (id == null) {
			return Response.status(400).entity("ID inválido").build();
		}
		return gerarHtmlInterno(Long.parseLong(id));
	}

	private Response gerarHtmlInterno(Long id) {
		String baseUrl = extrairBaseUrl();
		Session session = HibernateUtil.getSession();
		List<Arquivamento> arquivamentos;
		try {
			arquivamentos = session.createQuery(
					"FROM Arquivamento WHERE id = :id", Arquivamento.class)
					.setParameter("id", id)
					.getResultList();
		} finally {
			session.close();
		}
		if (arquivamentos == null || arquivamentos.isEmpty()) {
			return Response.status(404).entity("Arquivamento não encontrado").build();
		}
		for (Arquivamento arq : arquivamentos) {
			if (arq.getEmpresa() != null && arq.getEmpresa().getIdArquivo() != null) {
				arq.setLogo(baseUrl + arq.getEmpresa().getIdArquivo());
			}
		}
		try {
			String html = RelatorioEngine.renderArquivamento(arquivamentos);
			return Response.ok(html, "text/html;charset=UTF-8").build();
		} catch (Exception e) {
			log.error("Erro ao gerar relatório", e);
			return Response.status(500).entity("Erro gerando relatório").build();
		}
	}

	private String extrairBaseUrl() {
		String url = servletRequest.getRequestURL().toString();
		int idx = url.indexOf("rest");
		return (idx >= 0 ? url.substring(0, idx) : url) + "rest/binario/downloadImg?id=";
	}

	private void limpaRelatoriosAntigos() {
		long agora = System.currentTimeMillis();
		relatorios.entrySet().removeIf(e -> agora - e.getKey() > 60_000);
	}
}
