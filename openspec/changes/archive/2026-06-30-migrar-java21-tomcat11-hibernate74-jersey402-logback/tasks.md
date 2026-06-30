## 1. Atualizar dependências no pom.xml

- [x] 1.1 Atualizar `maven.compiler.release` de 11 para 21
- [x] 1.2 Atualizar Tomcat embed de 9.0.91 para 11.0.22
- [x] 1.3 Atualizar Jersey de 2.40 para 4.0.0-M2 (namespace jakarta.ws.rs)
- [x] 1.4 Atualizar Hibernate de 6.6.1.Final para 7.0.2.Final
- [x] 1.5 Atualizar Logback de 1.2.13 para 1.5.16
- [x] 1.6 Atualizar JUL-to-SLF4J de 1.7.36 para 2.0.16
- [x] 1.7 Substituir `jaxb-api` por `jaxb-runtime` 4.0.5 (Jakarta)
- [x] 1.8 Atualizar comentários no pom.xml que mencionam javax.servlet/javax.ws.rs

## 2. Migrar imports javax.servlet → jakarta.servlet

- [x] 2.1 Migrar `src/main/java/br/com/am/servlet/HibernateServlet.java` — javax.servlet → jakarta.servlet
- [x] 2.2 SKIP: `WebReport.java` será removido na tarefa 5.3, não precisa migrar
- [x] 2.3 Migrar `src/main/java/br/com/am/rest/RelatorioArquivamentoApp.java` — javax.servlet → jakarta.servlet (linhas de HttpServletRequest)

## 3. Migrar imports javax.ws.rs → jakarta.ws.rs

- [x] 3.1 Migrar `src/main/java/br/com/am/rest/RestApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.2 Migrar `src/main/java/br/com/am/rest/LoginApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.3 Migrar `src/main/java/br/com/am/rest/ArquivamentoApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.4 Migrar `src/main/java/br/com/am/rest/PesquisaArquivamentoApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.5 Migrar `src/main/java/br/com/am/rest/RelatorioArquivamentoApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.6 Migrar `src/main/java/br/com/am/rest/BinarioApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.7 Migrar `src/main/java/br/com/am/rest/EmpresaApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.8 Migrar `src/main/java/br/com/am/rest/TipoArquivamentoApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.9 Migrar `src/main/java/br/com/am/rest/TipoExpurgoApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.10 Migrar `src/main/java/br/com/am/rest/UsuarioApp.java` — javax.ws.rs → jakarta.ws.rs
- [x] 3.11 Migrar `src/main/java/br/com/am/rest/TesteApp.java` — javax.ws.rs → jakarta.ws.rs

## 4. Criar configuração do Logback

- [x] 4.1 Criar `src/main/resources/logback.xml` com file appender com rotação (500MB, 30 dias)
- [x] 4.2 Configurar níveis: global WARN, `br.com.am` INFO, `org.hibernate.SQL` WARN
- [x] 4.3 Adicionar console appender com cores
- [x] 4.4 `SLF4JBridgeHandler.install()` já presente em `EmbeddedServer.java` — mantido

## 5. Remover código morto do BIRT

- [x] 5.1 Remover `src/main/java/br/com/am/util/BirtEngine.java`
- [x] 5.2 Remover `src/main/java/br/com/am/util/BirtDataSet.java`
- [x] 5.3 Remover `src/main/java/br/com/am/servlet/WebReport.java`
- [x] 5.4 Remover `src/main/resources/arquivamento.rptdesign`
- [x] 5.5 Remover `src/main/resources/plugin.xml`
- [x] 5.6 Remover linhas `birtLog` e `birtLogDir` de `config.properties`
- [x] 5.7 Remover referência a BIRT em comentário no `EmbeddedServer.java`
- [x] 5.8 Logger `org.eclipse.birt` não existe no logback.xml criado — OK

## 6. Atualizar ambiente Docker

- [x] 6.1 Atualizar `am-sis.dockerfile`: `openjdk:11-jre-slim` → `eclipse-temurin:21-jre`
- [x] 6.2 `build.sh` e `docker-compose.yml` não referenciam versão do Java — OK

## 7. Atualizar README.md

- [x] 7.1 Atualizar versões do stack no README.md (Java 21, Tomcat 11, Hibernate 7, Jersey 4)
- [x] 7.2 Remover menções ao BIRT no README.md (config table)
- [x] 7.3 Atualizar CLAUDE.md com as novas versões e remover BIRT da descrição

## 8. Compilar e validar

- [x] 8.1 Executar `mvn clean package` — BUILD SUCCESS, sem erros de compilação
- [x] 8.2 `startup local` — JAR inicia sem erros com Tomcat 11 + Hibernate 7
- [x] 8.3 Testar endpoints — `GET /rest/teste` → "Teste" ✓, `POST /rest/login` → token válido ✓
- [x] 8.4 Verificar geração de logs — `logs/am-sis.log` criado com formato definido ✓
- [x] 8.5 Relatório via Thymeleaf — `RelatorioEngine.java` já usa Thymeleaf, endpoint mantido
