package br.com.am.rest;

import br.com.am.entidades.Arquivamento;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.util.BirtEngine;
import br.com.am.util.HibernateUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.birt.report.engine.api.*;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.util.*;

@Path("/relatorioArquivamento")
public class RelatorioArquivamentoApp extends br.com.am.rest.RestApp {

	@Context
	private ServletContext context;
	@Context
	private HttpServletRequest servletRequest;

	private static final Map<Long, byte[]> relatorios = new HashMap<Long, byte[]>();

	private Response processaValidacoes(Arquivamento arquivamento,
			String token) {
		try {
			validaToken(token);
		} catch (UsuarioNaoAchadoExection e1) {
			return Response.status(401)
					.entity(StringEscapeUtils.escapeHtml("Token inválido"))
					.type(MediaType.APPLICATION_JSON).build();
		}
		return null;
	}

	@POST
	@Path("/gerar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response gerar(@HeaderParam("token") String token,
			List<Arquivamento> arquivamentos) {
		if (arquivamentos == null || arquivamentos.isEmpty()) {
			return Response.status(400).entity("Relaório Vazio").build();
		}
		limpaRealtoriosAntigos();

		String url = servletRequest.getRequestURL().toString();
		url = url.split("rest")[0];
		url = url + "rest/binario/downloadImg?id=";
		// System.out.println("url :" + url);
		for (Iterator iterator = arquivamentos.iterator(); iterator
				.hasNext();) {
			Arquivamento arquivamento = (Arquivamento) iterator.next();
			arquivamento
					.setLogo(url + arquivamento.getEmpresa().getIdArquivo());
			System.out.println(arquivamento.getLogo());
		}

		String reportName = "arquivamento.rptdesign";
		IReportEngine birtReportEngine = BirtEngine.getBirtEngine(context);

		IReportRunnable design;
		try {
			HashMap datasets = new HashMap();
			datasets.put("APP_CONTEXT_KEY_DATA SET", arquivamentos.iterator());
			design = birtReportEngine.openReportDesign(
					br.com.am.recursos.Recursos.class.getResourceAsStream(reportName));
			IRunAndRenderTask task = birtReportEngine
					.createRunAndRenderTask(design);
			task.setAppContext(datasets);
			PDFRenderOption options = new PDFRenderOption();
			options.setSupportedImageFormats("PNG;JPG;BMP");
			options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			options.setOutputStream(arrayOutputStream);

			task.setRenderOption(options);
			// HashMap parms = new HashMap();
			// parms.put("imageURI", url);
			// task.setParameterValues(parms);

			task.run();
			task.close();
			byte[] byteArray = arrayOutputStream.toByteArray();
			Long currentTimeMillis = System.currentTimeMillis();
			relatorios.put(currentTimeMillis, byteArray);
			return Response.status(200).entity(currentTimeMillis).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(400).entity("Erro Gerando relatorio").build();
	}

	private void limpaRealtoriosAntigos() {
		Set<Long> remover = new HashSet<Long>();
		Set<Long> keySet = relatorios.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			Long longR = (Long) iterator.next();
			if (System.currentTimeMillis() - longR > 60000) {
				remover.add(longR);
			}
		}
		for (Iterator iterator = remover.iterator(); iterator.hasNext();) {
			Long long1 = (Long) iterator.next();
			relatorios.remove(long1);
		}
	}

	@GET
	@Path("/imprimir/{currentTimeMillis}")
	@Produces("application/pdf")
	public Response imprimir(
			@PathParam("currentTimeMillis") Long currentTimeMillis) {
		try {
			return Response
					.ok(relatorios.get(currentTimeMillis), "application/pdf")
					// .header("content-disposition", "attachment; filename =
					// doc.pdf")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(500).entity("Erro Imprimindo relatorio").build();
	}

	// @GET
	// @Produces("application/pdf")
	public Response getPDF(@HeaderParam("token") String token,
			List<Arquivamento> arquivamentos) {
		String reportName = "arquivamento.rptdesign";
		IReportEngine birtReportEngine = BirtEngine.getBirtEngine(context);
		IReportRunnable design;
		try {
			HashMap datasets = new HashMap();
			// datasets.put("APP_CONTEXT_KEY_DATA SET",
			// arquivamentos.iterator());
			Session session = HibernateUtil.getSession();
			try {
				List list = session.createCriteria(Arquivamento.class)
						.addOrder(Order.asc("descricao")).list();
				datasets.put("APP_CONTEXT_KEY_DATA SET", list.iterator());

			} finally {
				session.close();
			}
			// design =
			// birtReportEngine.openReportDesign(context.getRealPath("/reports")
			// + "/" + reportName);

			design = birtReportEngine.openReportDesign(
					br.com.am.recursos.Recursos.class.getResourceAsStream(reportName));
			IRunAndRenderTask task = birtReportEngine
					.createRunAndRenderTask(design);
			task.setAppContext(datasets);
			PDFRenderOption options = new PDFRenderOption();
			options.setSupportedImageFormats("PNG;JPG;BMP");
			options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			options.setOutputStream(arrayOutputStream);

			task.setRenderOption(options);

			// run report
			task.run();
			task.close();
			return Response
					.ok(arrayOutputStream.toByteArray(), "application/pdf")
					// .header("content-disposition",
					// "attachment; filename = doc.pdf")
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(500).entity("Erro Gerando relatorio")
				.type(MediaType.TEXT_HTML).build();
	}

	@GET
	@Path("/gerarHtml/{id}")
	@Produces("text/html")
	public Response gerarHtmlPorId(@HeaderParam("token") String token,
			@PathParam("id") String id) {
		if (id == null) {
			return Response.status(400).entity("Relaório Vazio").build();
		}
		limpaRealtoriosAntigos();
		synchronized (relatorios) {
			String url = servletRequest.getRequestURL().toString();
			url = url.split("rest")[0];
			url = url + "rest/binario/downloadImg?id=";
			Session session = HibernateUtil.getSession();
			List<Arquivamento> arquivamentos = null;
			try {
				Criteria criteria = session.createCriteria(Arquivamento.class);
				criteria.add(Restrictions.eq("id", new Long(id)));
				arquivamentos = criteria.list();
			} finally {
				session.close();
			}
			if (arquivamentos == null || arquivamentos.isEmpty()) {
				return Response.status(400)
						.entity("Arquivamento não encontrado").build();
			}
			for (Iterator iterator = arquivamentos.iterator(); iterator
					.hasNext();) {
				Arquivamento arquivamento = (Arquivamento) iterator.next();
				if (arquivamento.getEmpresa().getIdArquivo() != null) {
					arquivamento.setLogo(
							url + arquivamento.getEmpresa().getIdArquivo());
				}
			}

			String reportName = "/arquivamento.rptdesign";
			IReportEngine birtReportEngine;

			IReportRunnable design;
			try {
				birtReportEngine = BirtEngine.getBirtEngine(context);
				HashMap datasets = new HashMap();
				datasets.put("APP_CONTEXT_KEY_DATA SET",
						arquivamentos.iterator());
				design = birtReportEngine.openReportDesign(
						br.com.am.recursos.Recursos.class.getResourceAsStream(reportName));
				IRunAndRenderTask task = birtReportEngine
						.createRunAndRenderTask(design);
				task.setAppContext(datasets);
				HTMLRenderOption options = new HTMLRenderOption();
				options.setSupportedImageFormats("PNG;JPG;BMP");
				options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				options.setOutputStream(arrayOutputStream);

				task.setRenderOption(options);
				// HashMap parms = new HashMap();
				// parms.put("imageURI", url);
				// task.setParameterValues(parms);

				task.run();
				task.close();
				return Response.ok(arrayOutputStream.toByteArray(), "text/html")
						// .header("content-disposition",
						// "attachment; filename = doc.pdf")
						.build();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				BirtEngine.destroyBirtEngine();
			}
		}
		return Response.status(400).entity("Erro Gerando relatorio").build();
	}

	@GET
	@Path("/gerarPdf/{id}")
	@Produces("application/pdf")
	public Response gerarPDfPorId(@HeaderParam("token") String token,
			@PathParam("id") String id) {
		if (id == null) {
			return Response.status(400).entity("Relaório Vazio").build();
		}
		limpaRealtoriosAntigos();
		synchronized (relatorios) {
			String url = servletRequest.getRequestURL().toString();
			url = url.split("rest")[0];
			url = url + "rest/binario/downloadImg?id=";
			Session session = HibernateUtil.getSession();
			List<Arquivamento> arquivamentos = null;
			try {
				Criteria criteria = session.createCriteria(Arquivamento.class);
				criteria.add(Restrictions.eq("id", new Long(id)));
				arquivamentos = criteria.list();
			} finally {
				session.close();
			}
			if (arquivamentos == null || arquivamentos.isEmpty()) {
				return Response.status(400)
						.entity("Arquivamento não encontrado").build();
			}
			for (Iterator iterator = arquivamentos.iterator(); iterator
					.hasNext();) {
				Arquivamento arquivamento = (Arquivamento) iterator.next();
				if (arquivamento.getEmpresa().getIdArquivo() != null) {
					arquivamento.setLogo(
							url + arquivamento.getEmpresa().getIdArquivo());
				}
			}

			String reportName = "arquivamento.rptdesign";
			IReportEngine birtReportEngine;

			IReportRunnable design;
			try {
				birtReportEngine = BirtEngine.getBirtEngine(context);
				HashMap datasets = new HashMap();
				datasets.put("APP_CONTEXT_KEY_DATA SET",
						arquivamentos.iterator());
				design = birtReportEngine.openReportDesign(
						br.com.am.recursos.Recursos.class.getResourceAsStream(reportName));
				IRunAndRenderTask task = birtReportEngine
						.createRunAndRenderTask(design);
				task.setAppContext(datasets);
				PDFRenderOption options = new PDFRenderOption();
				options.setSupportedImageFormats("PNG;JPG;BMP");
				options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				options.setOutputStream(arrayOutputStream);

				task.setRenderOption(options);
				// HashMap parms = new HashMap();
				// parms.put("imageURI", url);
				// task.setParameterValues(parms);

				task.run();
				task.close();
				return Response
						.ok(arrayOutputStream.toByteArray(), "application/pdf")
						// .header("content-disposition",
						// "attachment; filename = doc.pdf")
						.build();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				BirtEngine.destroyBirtEngine();
			}
		}
		return Response.status(400).entity("Erro Gerando relatorio").build();
	}

}
