## 1. Atualizar dependências no pom.xml

- [x] 1.1 Alterar `<packaging>war</packaging>` para `<packaging>jar</packaging>` e remover dependência `javax.servlet-api` com `scope=provided`
- [x] 1.2 Substituir `org.hibernate:hibernate-core` 5.6.15 por `org.hibernate.orm:hibernate-core` 6.x
- [x] 1.3 Substituir `org.hibernate:hibernate-entitymanager` por `org.hibernate.orm:hibernate-core` (unificado no Hibernate 6)
- [x] 1.4 Substituir `mysql:mysql-connector-java` por H2 embutido (`com.h2database:h2`) — MySQL removido pois H2 é o banco único
- [x] 1.5 Substituir `commons-lang:commons-lang` 2.6 por `org.apache.commons:commons-lang3` 3.x
- [x] 1.6 Adicionar dependências de Tomcat embutido: `tomcat-embed-core`, `tomcat-embed-jasper`, `tomcat-embed-el`
- [x] 1.7 Adicionar dependência H2: `com.h2database:h2`
- [x] 1.8 Substituir `maven-war-plugin` por `maven-shade-plugin` configurado com `Main-Class=br.com.am.EmbeddedServer` e `ServicesResourceTransformer`
- [x] 1.9 Remover dependência `javassist` (incorporado no Hibernate 6)

## 2. Migrar imports javax → jakarta e commons-lang

- [x] 2.1 Substituir todos os imports `javax.persistence.*` por `jakarta.persistence.*` em todas as entidades e `HibernateUtil.java`
- [x] 2.2 ~~Substituir imports `javax.servlet.*` por `jakarta.servlet.*`~~ — PULADO: BIRT 4.5.0 usa `javax.servlet.*`; manter Tomcat 9 embutido (javax.servlet) para compatibilidade
- [x] 2.3 ~~Substituir imports `javax.ws.rs.*` por `jakarta.ws.rs.*`~~ — PULADO: manter Jersey 2.x (javax.ws.rs) por compatibilidade com BIRT/Tomcat 9
- [x] 2.4 ~~Atualizar Jersey para versão 3.x~~ — PULADO: Jersey 3.x requer Tomcat 10+ (jakarta.servlet); incompatível com BIRT 4.5.0
- [x] 2.5 Substituir imports `org.apache.commons.lang.*` por `org.apache.commons.lang3.*` em todos os arquivos afetados (`escapeHtml` → `escapeHtml4`)

## 3. Migrar Criteria API legada do Hibernate → JPA Criteria / JPQL

- [x] 3.1 Refatorar `ArquivamentoApp.java` — substituir `session.createCriteria()` e `Restrictions.*` por `session.get()` direto
- [x] 3.2 Refatorar `PesquisaArquivamentoApp.java` — migrar todos os filtros dinâmicos para `CriteriaBuilder` com predicados (JPA Criteria API)
- [x] 3.3 Refatorar `RelatorioArquivamentoApp.java` — migrar queries por ID e listagem para JPQL
- [x] 3.4 Refatorar `EmpresaApp.java` — migrar listagem para JPQL
- [x] 3.5 Refatorar `TipoArquivamentoApp.java` — migrar listagem para JPQL
- [x] 3.6 Refatorar `TipoExpurgoApp.java` — migrar listagem para JPQL
- [x] 3.7 Refatorar `UsuarioApp.java` — migrar busca por ID e listagem para JPQL
- [x] 3.8 Refatorar `LoginApp.java` — migrar busca por login para JPQL; corrigir `session.update()` → `atualizar()`; `BinarioApp.java` — migrar busca por ID para JPQL
- [x] 3.9 Refatorar `RestApp.java` — substituir `session.update()` por `session.merge()` e `session.delete(session.find())` por `session.get()` + `session.remove()`

## 4. Criar servidor Tomcat embutido

- [x] 4.1 Criar `src/main/java/br/com/am/EmbeddedServer.java` com método `main()` que inicializa Tomcat embutido, extrai webapp do JAR para diretório temporário e registra `HibernateServlet` explicitamente (anotação `@WebServlet` não é escaneada em fat JAR)
- [x] 4.2 Simplificar `HibernateServlet`: remover `SchemaExport`, thread com sleep de 10s, `@WebServlet`; adicionar verificação de existência antes de inserir dados seed; schema gerenciado por `hbm2ddl.auto=update`
- [ ] 4.3 Validar que BIRT inicializa corretamente no contexto do Tomcat embutido (checar conflito de classloader com shade) — **validação manual**

## 5. Configurar H2 como banco embutido

- [x] 5.1 Atualizar `src/main/resources/META-INF/persistence.xml`: URL para `jdbc:h2:file:./data/am-sis`, driver `org.h2.Driver`, dialect `org.hibernate.dialect.H2Dialect`
- [x] 5.2 Remover referência ao driver MySQL do `persistence.xml`; migrar namespace de `javax.persistence` para `jakarta.persistence` (JPA 3.0)
- [ ] 5.3 Validar que `@Lob byte[]` em `Binario.java` funciona corretamente com H2 — **validação manual**

## 6. Atualizar Docker e scripts

- [x] 6.1 Reescrever `am-sis.dockerfile`: base `openjdk:11-jre-slim`, copiar `target/am-sis.jar`, expor porta 8080, `CMD ["java", "-jar", "am-sis.jar"]`
- [x] 6.2 Reescrever `docker-compose.yml`: somente serviço `am-sis`, volume para `./data`, porta `80:8080`
- [x] 6.3 Simplificar `build.sh`: remover `mvn war:war`

## 7. Atualizar frontend e recursos estáticos

- [ ] 7.1 Atualizar jQuery para versão 3.7.x nos arquivos estáticos em `src/main/webapp/` — **atualização manual de arquivos estáticos**

## 8. Atualizar documentação

- [x] 8.1 Reescrever `README.md` com instruções de build (`mvn clean package`), execução local (`java -jar`) e deploy Docker

## 9. Validação final

- [x] 9.1 Executar `mvn clean package` e verificar que `target/am-sis.jar` é gerado sem erros
- [ ] 9.2 Executar `java -jar target/am-sis.jar` e verificar que aplicação sobe na porta 8080
- [ ] 9.3 Testar fluxo básico: login, criação de arquivamento, pesquisa e geração de relatório
- [ ] 9.4 Executar `./build.sh` e verificar que container Docker sobe e aplicação responde em `http://localhost`
