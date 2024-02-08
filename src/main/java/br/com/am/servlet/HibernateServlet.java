package br.com.am.servlet;

import br.com.am.entidades.Empresa;
import br.com.am.entidades.TipoArquivamento;
import br.com.am.entidades.TipoExpurgo;
import br.com.am.util.HibernateUtil;
import br.com.am.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EnumSet;

/**
 * Servlet implementation class HibernateServlet
 */
@WebServlet(value = "/HibernateServlet", loadOnStartup = 1)
public class HibernateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SessionFactory sessionFactory;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HibernateServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        createSchema(null);
                        Session session = HibernateUtil.getSession();
                        Transaction transaction = session.beginTransaction();
                        Empresa empresa = new Empresa();
                        empresa.setNome("Am-Sis");
                        session.persist(empresa);
                        TipoArquivamento tipoArquivamento = new TipoArquivamento();
                        tipoArquivamento.setDescricao("Remessas");
                        session.persist(tipoArquivamento);
                        TipoExpurgo tipoExpurgo = new TipoExpurgo();
                        tipoExpurgo.setDescricao("Lixo");
                        session.persist(tipoExpurgo);
                        transaction.commit();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
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

        String tipo = request.getParameter("tipo");
        try {
            if (tipo == null) {
                return;
            } else if ("create_schema".equals(tipo)) {
                createSchema(printWriter);
            }
        } catch (Exception e) {
            printWriter.println(e.getMessage());
        }

        printWriter.println("<br/><a href='conf.jsp'>Voltar</a>");
        printWriter.println("</body></html>");
    }


    private void createSchema(PrintWriter printWriter)
            throws Exception {
        SchemaExport export = new SchemaExport();
        export.create(EnumSet.of(TargetType.DATABASE), getMetaData().buildMetadata());
    }

    private MetadataSources getMetaData() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(getClass().getResourceAsStream("/META-INF/persistence.xml"));
        NodeList list = doc.getElementsByTagName("property");
        String url = null, pass = null, user = null, driver = null;
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String attr = element.getAttribute("name");
                if ("javax.persistence.jdbc.url".equals(attr)) {
                    url = element.getAttribute("value");
                } else if ("javax.persistence.jdbc.user".equals(attr)) {
                    user = element.getAttribute("value");
                } else if ("javax.persistence.jdbc.password".equals(attr)) {
                    pass = element.getAttribute("value");
                } else if ("javax.persistence.jdbc.driver".equals(attr)) {
                    driver = element.getAttribute("value");
                }
            }
        }
        Class.forName(driver);
        Connection connection =
                DriverManager.getConnection(url, user, pass);
        MetadataSources metadata = new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect")
                        .applySetting(AvailableSettings.CONNECTION_PROVIDER, new MyConnectionProvider(connection))
                        .build());

        metadata.addAnnotatedClass(br.com.am.entidades.Usuario.class);
        metadata.addAnnotatedClass(br.com.am.entidades.TipoArquivamento.class);
        metadata.addAnnotatedClass(br.com.am.entidades.TipoExpurgo.class);
        metadata.addAnnotatedClass(br.com.am.entidades.Arquivamento.class);
        metadata.addAnnotatedClass(br.com.am.entidades.Empresa.class);
        metadata.addAnnotatedClass(br.com.am.entidades.Binario.class);
        return metadata;
    }

    private static class MyConnectionProvider implements ConnectionProvider {
        private final Connection connection;

        public MyConnectionProvider(Connection connection) {
            this.connection = connection;
        }

        @Override
        public boolean isUnwrappableAs(Class unwrapType) {
            return false;
        }

        @Override
        public <T> T unwrap(Class<T> unwrapType) {
            return null;
        }

        @Override
        public Connection getConnection() {
            return connection; // Interesting part here
        }

        @Override
        public void closeConnection(Connection conn) throws SQLException {
        }

        @Override
        public boolean supportsAggressiveRelease() {
            return true;
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
