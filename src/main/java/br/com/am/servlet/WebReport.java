package br.com.am.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import br.com.am.entidades.Arquivamento;
import br.com.am.util.BirtEngine;
import br.com.am.util.HibernateUtil;

@WebServlet("/WebReport")
public class WebReport extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IReportEngine birtReportEngine = null;
	protected static Logger logger = Logger.getLogger("org.eclipse.birt");

	public WebReport() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
		BirtEngine.destroyBirtEngine();
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// get report name and launch the engine
		// resp.setContentType("text/html");
		resp.setContentType("application/pdf");
		resp.setHeader("Content-Disposition", "inline; filename=test.pdf");
		// String reportName = req.getParameter("ReportName");
		String reportName = "arquivamento.rptdesign";
		ServletContext sc = req.getSession().getServletContext();
		this.birtReportEngine = BirtEngine.getBirtEngine(sc);

		IReportRunnable design;
		try {

			HashMap datasets = new HashMap();
			Session session = HibernateUtil.getSession();
			try {
				List list = session.createCriteria(Arquivamento.class)
						.addOrder(Order.asc("descricao")).list();
				datasets.put("BIRT_REPORT_DATA_SET", list.iterator());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
			}

			// Open report design
			design = birtReportEngine.openReportDesign(
					sc.getRealPath("/reports") + "/" + reportName);
			// create task to run and render report
			IRunAndRenderTask task = birtReportEngine
					.createRunAndRenderTask(design);
			task.getAppContext().put("BIRT_VIEWER_HTTPSERVLET_REQUEST", req);

			task.setAppContext(datasets);

			// set output options
			/*
			 * HTMLRenderOption options = new HTMLRenderOption();
			 * options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			 * options.setOutputStream(resp.getOutputStream());
			 * options.setImageHandler(new HTMLServerImageHandler());
			 * options.setBaseImageURL(req.getContextPath()+"/images");
			 * options.setImageDirectory(sc.getRealPath("/images"));
			 */

			PDFRenderOption options = new PDFRenderOption();
			options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
			resp.setHeader("Content-Disposition",
					"inline; filename=\"test.pdf\"");
			options.setOutputStream(resp.getOutputStream());

			task.setRenderOption(options);

			// run report
			task.run();
			task.close();
		} catch (Exception e) {

			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init(ServletConfig sc) throws ServletException {
		this.birtReportEngine = BirtEngine
				.getBirtEngine(sc.getServletContext());
	}

}
