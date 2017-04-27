package br.com.am.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.SessionFactory;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;

import br.com.am.recursos.Recursos;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;

/**
 * Servlet implementation class HibernateServlet
 */
@WebServlet("/HibernateServlet")
public class HibernateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SessionFactory sessionFactory;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HibernateServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter printWriter = response.getWriter();
		response.setContentType("text/html");
		printWriter.println("<!doctype html><html><body>");
		String senha = request.getParameter("senha");
		try {
			if (senha == null || !Recursos.getProperties().get("pass").equals(Util.md5(senha))) {
				printWriter.println("<br/><a href='conf.jsp'>Voltar</a>");
				printWriter.println("</body></html>");
				return;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		String tipo = request.getParameter("tipo");

		if (tipo == null) {
			return;
		} else if ("create_schema".equals(tipo)) {
			createSchema(printWriter);
		} else if ("update_schema".equals(tipo)) {
			updateSchema(printWriter);
		}
		printWriter.println("<br/><a href='conf.jsp'>Voltar</a>");
		printWriter.println("</body></html>");
	}

	public void createSchema(PrintWriter printWriter) throws IOException {

		File output = File.createTempFile("create_script", ".sql");
		output.deleteOnExit();
		SchemaExport schemaExport = new SchemaExport();
		schemaExport.setDelimiter(";");
		schemaExport.setFormat(true);
		schemaExport.setOutputFile(output.getAbsolutePath());
		schemaExport.create(EnumSet.of(TargetType.SCRIPT), HibernateUtil.getMetadata());
		String fileContent = new String(Files.readAllBytes(output.toPath()));

		schemaExport = new SchemaExport();
		schemaExport.setDelimiter(";");
		schemaExport.setFormat(true);
		schemaExport.create(EnumSet.of(TargetType.DATABASE), HibernateUtil.getMetadata());

		printWriter.println(fileContent);
	}

	public void updateSchema(PrintWriter printWriter) throws IOException {

		File output = File.createTempFile("update_script", ".sql");
		output.deleteOnExit();
		SchemaUpdate schemaExport = new SchemaUpdate();
		schemaExport.setDelimiter(";");
		schemaExport.setFormat(true);
		schemaExport.setOutputFile(output.getAbsolutePath());
		schemaExport.execute(EnumSet.of(TargetType.SCRIPT), HibernateUtil.getMetadata());
		String fileContent = new String(Files.readAllBytes(output.toPath()));

		schemaExport = new SchemaUpdate();
		schemaExport.setDelimiter(";");
		schemaExport.setFormat(true);
		schemaExport.execute(EnumSet.of(TargetType.DATABASE), HibernateUtil.getMetadata());

		printWriter.println(fileContent);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
