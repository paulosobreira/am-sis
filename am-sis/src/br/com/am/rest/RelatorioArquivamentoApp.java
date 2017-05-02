package br.com.am.rest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.am.entidades.Arquivamento;
import br.com.am.erros.UsuarioNaoAchadoExection;
import br.com.am.recursos.Recursos;
import br.com.am.util.BirtEngine;
import br.com.am.util.HibernateUtil;

@Path("/relatorioArquivamento")
public class RelatorioArquivamentoApp extends RestApp {

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
			arquivamento.setLogo(
					url + arquivamento.getEmpresa().getArquivo().getId());
			System.out.println(arquivamento.getLogo());
		}

		String reportName = "arquivamento.rptdesign";
		IReportEngine birtReportEngine = BirtEngine.getBirtEngine(context);

		IReportRunnable design;
		try {
			HashMap datasets = new HashMap();
			datasets.put("APP_CONTEXT_KEY_DATA SET", arquivamentos.iterator());
			design = birtReportEngine.openReportDesign(
					Recursos.class.getResourceAsStream(reportName));
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

	@GET
	@Produces("application/pdf")
	public Response getPDF(@HeaderParam("token") String token,
			List<Arquivamento> arquivamentos) {
		String reportName = "arquivamento.rptdesign";
		IReportEngine birtReportEngine = BirtEngine.getBirtEngine(context);
		IReportRunnable design;
		try {
			HashMap datasets = new HashMap();
			// datasets.put("APP_CONTEXT_KEY_DATA SET",
			// arquivamentos.iterator());
			Session session = HibernateUtil.getSessionFactory().openSession();
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
					Recursos.class.getResourceAsStream(reportName));
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

}
