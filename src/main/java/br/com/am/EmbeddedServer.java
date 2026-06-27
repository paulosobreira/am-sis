package br.com.am;

import br.com.am.servlet.HibernateServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class EmbeddedServer {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector(); // necessário para criar e vincular o conector HTTP

        Path workDir = Files.createTempDirectory("am-sis-work");
        tomcat.setBaseDir(workDir.toString());

        String webappPath = extractWebapp(workDir);
        Context ctx = tomcat.addWebapp("/am-sis", webappPath);

        // HibernateServlet não é detectado via @WebServlet em fat JAR; registra explicitamente
        Wrapper hibernateWrapper = Tomcat.addServlet(ctx, "HibernateServlet", new HibernateServlet());
        hibernateWrapper.setLoadOnStartup(1);
        ctx.addServletMappingDecoded("/HibernateServlet", "HibernateServlet");

        tomcat.start();
        System.out.println("am-sis iniciado em http://localhost:8080/am-sis");
        tomcat.getServer().await();
    }

    private static String extractWebapp(Path workDir) throws Exception {
        Path webappDir = workDir.resolve("webapp");
        Files.createDirectories(webappDir);

        URL jarUrl = EmbeddedServer.class.getProtectionDomain().getCodeSource().getLocation();
        File jarFile = new File(jarUrl.toURI());

        if (jarFile.isFile()) {
            try (JarFile jar = new JarFile(jarFile)) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith("webapp/") && name.length() > "webapp/".length()) {
                        String relative = name.substring("webapp/".length());
                        Path target = webappDir.resolve(relative);
                        if (entry.isDirectory()) {
                            Files.createDirectories(target);
                        } else {
                            Files.createDirectories(target.getParent());
                            try (InputStream is = jar.getInputStream(entry)) {
                                Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                    }
                }
            }
        } else {
            // Modo IDE (classpath explodido)
            URL webappUrl = EmbeddedServer.class.getResource("/webapp");
            if (webappUrl != null) {
                copyDirectory(Paths.get(webappUrl.toURI()), webappDir);
            }
        }

        return webappDir.toString();
    }

    private static void copyDirectory(Path source, Path target) throws Exception {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Files.createDirectories(target.resolve(source.relativize(dir)));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
