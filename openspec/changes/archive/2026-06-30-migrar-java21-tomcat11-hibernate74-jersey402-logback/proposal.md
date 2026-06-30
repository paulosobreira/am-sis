## Why

O projeto está com dependências parcialmente migradas para Jakarta EE (JPA já usa `jakarta.persistence.*`) enquanto JAX-RS e Servlet ainda usam `javax.*`, criando uma configuração híbrida insustentável. A migração completa para Java 21 + Tomcat 11 + Hibernate 7.4 + Jersey 4.0.2 unifica o stack sob Jakarta EE 10, elimina dependências legadas (`javax.*`), e adiciona o Logback como sistema único de logging.

## What Changes

- **Java 21**: atualizar `maven.compiler.release` de 11 para 21 e a imagem Docker de `openjdk:11-jre-slim` para `eclipse-temurin:21-jre`
- **Tomcat 9 → 11**: migrar de Tomcat 9 (javax.servlet) para Tomcat 11 (jakarta.servlet)
- **Jersey 2.x (javax.ws.rs) → Jersey 4.0.2 (jakarta.ws.rs)**: migrar JAX-RS de javax para jakarta
- **Hibernate 6.6 → 7.4**: atualizar Hibernate ORM para versão compatível com Jakarta EE 10
- **Logback**: substituir Logback 1.2.x por versão compatível com Java 21 e consolidar configuração com `logback.xml`
- **JUL→SLF4J bridge**: manter o redirecionamento de `java.util.logging` para Logback via SLF4J
- **Migrar imports**: alterar todos os imports `javax.ws.rs.*` → `jakarta.ws.rs.*` e `javax.servlet.*` → `jakarta.servlet.*` no código Java e JSPs
- **Jakarta Servlet API**: atualizar o `EmbeddedServer.java` para usar `jakarta.servlet` com Tomcat 11 embutido
- **Remover BIRT**: eliminar todo código morto do BIRT (`BirtEngine.java`, `BirtDataSet.java`, `WebReport.java`, `plugin.xml`, `arquivamento.rptdesign`, propriedades `birtLog*` em `config.properties`) — a engine de relatório já é Thymeleaf via `RelatorioEngine.java`
- **BREAKING**: a API e URLs do Tomcat embedded mudam (`tomcat-embed-core` 11.x com suporte a Jakarta)
- **BREAKING**: todos os endpoints REST sob `@Path` continuam com o mesmo caminho, mas exigem container Jakarta

## Capabilities

### New Capabilities
- `logback-config`: configuração centralizada do Logback com `logback.xml`, rotação de logs e níveis por pacote
- `thymeleaf-report`: relatórios de arquivamento usando Thymeleaf (já implementado em `RelatorioEngine.java` — remover dependências BIRT residuais)

### Modified Capabilities
<!-- No existing specs to modify -->

## Impact

- **Código fonte**: todos os arquivos Java com imports `javax.servlet.*` e `javax.ws.rs.*` (cerca de 12 arquivos) precisam ser migrados para `jakarta.*`
- **pom.xml**: atualizar versões de todas as dependências e propriedades do compilador
- **Dockerfile**: atualizar imagem base de JDK 11 para JDK 21
- **JSPs/HTML**: verificar se há referências implícitas a servlets javax (TAG files, TLDs)
- **Hibernate**: ajustar configuração de persistência se houver diferenças de API entre 6.6 e 7.4
- **BIRT**: remover código morto (`BirtEngine.java`, `BirtDataSet.java`, `WebReport.java`, `plugin.xml`, `arquivamento.rptdesign`, `birtLog*`)
