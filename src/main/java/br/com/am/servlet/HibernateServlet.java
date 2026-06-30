package br.com.am.servlet;

import br.com.am.entidades.Empresa;
import br.com.am.entidades.TipoArquivamento;
import br.com.am.entidades.TipoExpurgo;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

public class HibernateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public HibernateServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Session session = HibernateUtil.getSession();
            try {
                // Schema é criado automaticamente via hbm2ddl.auto=update
                // Insere dados iniciais apenas se o banco estiver vazio
                Long empresaCount = session.createQuery(
                        "SELECT COUNT(e) FROM Empresa e", Long.class).uniqueResult();
                if (empresaCount == 0) {
                    Transaction tx = session.beginTransaction();
                    Empresa empresa = new Empresa();
                    empresa.setNome("Am-Sis");
                    session.persist(empresa);
                    TipoArquivamento tipoArquivamento = new TipoArquivamento();
                    tipoArquivamento.setDescricao("Remessas");
                    session.persist(tipoArquivamento);
                    TipoExpurgo tipoExpurgo = new TipoExpurgo();
                    tipoExpurgo.setDescricao("Lixo");
                    session.persist(tipoExpurgo);
                    tx.commit();
                }
            } finally {
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();
        response.setContentType("text/html");
        printWriter.println("<!doctype html><html><body>");
        String senha = request.getParameter("senha");
        try {
            if (senha == null || !br.com.am.recursos.Recursos.getProperties().get("pass").equals(Util.md5(senha))) {
                printWriter.println("<br/><a href='conf.jsp'>Voltar</a>");
                printWriter.println("</body></html>");
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            printWriter.println(e.getMessage());
        }

        printWriter.println("<br/><a href='conf.jsp'>Voltar</a>");
        printWriter.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
