## Context

A aplicação am-sis atualmente roda em Tomcat 9 + JDK 11 com uma mistura de namespaces: JPA já foi migrada para `jakarta.persistence.*` (Hibernate 6.6) enquanto JAX-RS (Jersey 2.40) e Servlet API (Tomcat 9 embutido) ainda usam `javax.*`. O Logback está presente (1.2.13) mas sem configuração dedicada (`logback.xml`).

A migração para Jakarta EE 10 (Java 21, Tomcat 11, Jersey 4.0.2, Hibernate 7.4) elimina essa hibridização e permite aproveitar os recursos das versões mais recentes.

## Goals / Non-Goals

**Goals:**
- Migrar todo o código de `javax.servlet.*` para `jakarta.servlet.*`
- Migrar todo o código de `javax.ws.rs.*` para `jakarta.ws.rs.*`
- Atualizar Tomcat embutido de 9.0.x para 11.x
- Atualizar Jersey de 2.40 para 4.0.2
- Atualizar Hibernate de 6.6.1.Final para 7.4.x
- Atualizar Java de 11 para 21
- Criar configuração centralizada do Logback (`logback.xml`)
- Atualizar imagem Docker para JDK 21
- Manter compatibilidade total de API REST (mesmos endpoints, mesmos contratos)
- Remover todo código morto do BIRT (a engine de relatório já é Thymeleaf)

**Non-Goals:**
- Não alterar a lógica de negócio ou o schema do banco
- Não refatorar o frontend JSP/Bootstrap
- Não migrar para Spring Boot ou outro framework
- Não alterar endpoints ou formato das respostas REST

## Decisions

### 1. Substituir dependências javax.* por Jakarta EE 10 equivalentes
- **Contexto:** Tomcat 11 elimina suporte a `javax.servlet`, exigindo `jakarta.servlet`. Jersey 4.x usa `jakarta.ws.rs`. Hibernate 7.4 já está no namespace Jakarta.
- **Decisão:** Migrar todas as dependências e imports do namespace `javax` para `jakarta` em um único commit.
- **Alternativa considerada:** Usar bridges de transformação de bytecode (ex: `jakarta.servlet-api` com Tomcat 9). Rejeitada por adicionar complexidade desnecessária e não resolver o problema de versões futuras.

### 2. Atualização in-place de imports (javax.* → jakarta.*)
- **Contexto:** A mudança de namespace é mecânica e bem definida: `javax.ws.rs` → `jakarta.ws.rs`, `javax.servlet` → `jakarta.servlet`.
- **Decisão:** Usar substituição textual em massa nos arquivos fonte (Java e JSP) por ser previsível e segura. Não há mudança de API além do namespace.
- **Alternativa considerada:** Ferramentas automáticas como OpenRewrite. Rejeitada por overhead de setup para um número pequeno de arquivos (~12 Java + JSPs).

### 3. Dockerfile: openjdk:11-jre-slim → eclipse-temurin:21-jre
- **Contexto:** `openjdk:11-jre-slim` está obsoleto e não tem tag para JDK 21. A imagem `eclipse-temurin` é o sucessor mantido pela Adoptium (Eclipse Foundation).
- **Decisão:** Usar `eclipse-temurin:21-jre` como imagem base.
- **Alternativa considerada:** `amazoncorretto:21` ou `ibm-semeru:21`. Temurin foi escolhido por ser o padrão da comunidade e ter ampla adoção.

### 4. Logback.xml na raiz do classpath
- **Contexto:** Logback procura `logback.xml` no classpath automaticamente. Atualmente não existe configuração explícita.
- **Decisão:** Criar `src/main/resources/logback.xml` com appender de arquivo com rotação, nível INFO para a aplicação e WARN para bibliotecas de terceiros.
- **Alternativa considerada:** `logback-spring.xml` (Spring-specific). Rejeitada pois o projeto não usa Spring.

### 5. Hibernate 7.4: manter hbm2ddl=update
- **Contexto:** Hibernate 7.4 muda a implementação interna do schema management, mas mantém a propriedade `hibernate.hbm2ddl.auto`.
- **Decisão:** Manter `update` e validar em staging antes de promover para produção.

### 6. Remover BIRT (código morto)
- **Contexto:** O BIRT já foi substituído pelo Thymeleaf como engine de relatório (`RelatorioEngine.java` + `templates/relatorio-arquivamento.html`), mas código morto permanece: `BirtEngine.java`, `BirtDataSet.java`, `WebReport.java`, `plugin.xml`, `arquivamento.rptdesign`, e propriedades `birtLog*` em `config.properties`.
- **Decisão:** Remover todos esses arquivos e referências. O `RelatorioArquivamentoApp.java` já usa exclusivamente `RelatorioEngine.renderArquivamento()` (Thymeleaf) — nenhuma adaptação no endpoint é necessária.

### 7. Versões exatas das dependências
- **Contexto:** É necessário garantir compatibilidade entre as novas versões.
- **Decisão:**
  - Tomcat embed: `11.0.x` (última disponível no Maven Central)
  - Jersey: `4.0.2` (jakarta.ws.rs)
  - Hibernate ORM: `7.4.x` (última disponível)
  - Logback: `1.5.x` (compatível com Java 21)
  - JUL-to-SLF4J: `2.0.x` (alinhado com SLF4J 2.x)
  - Maven compiler: `21` como release target

## Risks / Trade-offs

- **[Regressão] Tomcat 11 pode mudar comportamento de JSP** → Mitigação: testes manuais em todas as páginas JSP após a migração.
- **[Logback] Configuração incorreta pode mascarar erros** → Mitigação: configurar nível DEBUG para o pacote `br.com.am` durante validação.
- **[Hibernate] hbm2ddl=update pode gerar schema incorreto** → Mitigação: validar DDL gerado contra schema de produção em ambiente de staging antes do deploy.
- **[Rollback] Sem rollback simples** → Mitigação: manter o JAR atual (pré-migração) como fallback no deploy. A mudança é all-in (Jakarta EE é incompatível com javax na mesma JVM).
