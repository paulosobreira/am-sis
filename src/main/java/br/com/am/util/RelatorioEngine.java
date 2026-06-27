package br.com.am.util;

import br.com.am.entidades.Arquivamento;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;
import java.util.Locale;

public class RelatorioEngine {

    private static final TemplateEngine ENGINE;

    static {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(true);

        ENGINE = new TemplateEngine();
        ENGINE.setTemplateResolver(resolver);
    }

    public static String renderArquivamento(List<Arquivamento> arquivamentos) {
        Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("arquivamentos", arquivamentos);
        return ENGINE.process("relatorio-arquivamento", ctx);
    }
}
