## Why

O projeto possui 12 alertas de segurança ativos no Dependabot (incluindo vulnerabilidades críticas de RCE e SQL Injection) e depende de uma pilha WAR + Tomcat externo + MySQL externo que dificulta o deploy e o desenvolvimento local. A migração para fat JAR com servidor e banco embutidos reduz a superfície de ataque, simplifica o ciclo de build/run e elimina dependências de infraestrutura externa.

## What Changes

- **Dependências atualizadas** para corrigir todas as vulnerabilidades ativas:
  - `org.hibernate:hibernate-core` 5.6.15 → `org.hibernate.orm:hibernate-core` 6.x (corrige SQL Injection — alertas #9 e #17)
  - `mysql:mysql-connector-java` 8.0.33 → `com.mysql:mysql-connector-j` 9.x (corrige alertas #1, #3–#7, #15)
  - `commons-lang:commons-lang` 2.6 → `org.apache.commons:commons-lang3` 3.x (corrige alerta #16)
  - jQuery no frontend atualizado para ≥ 3.7 (corrige alerta #14)
- **BREAKING: Empacotamento WAR → fat JAR** com Tomcat embutido (via `tomcat-embed-core`)
- **BREAKING: Banco de dados MySQL → H2 embutido** (modo persistente em arquivo local)
- **Docker simplificado**: imagem base `openjdk:11-jre-slim`, executa `java -jar am-sis.jar` em vez de WAR em Tomcat
- `docker-compose.yml` remove serviços `db` e `phpmyadmin`, mantém apenas o serviço da aplicação
- `README.md` atualizado com novas instruções de build, run e deploy

## Capabilities

### New Capabilities

- `deploy-fatjar`: Empacotamento e execução como fat JAR com Tomcat e H2 embutidos — documenta como buildar, rodar localmente e via Docker

### Modified Capabilities

- `autenticacao`: Sem mudança de requisito — implementação interna inalterada
- `arquivamento`: Sem mudança de requisito — persistência migrada para H2, comportamentos inalterados

## Impact

- `pom.xml`: substituição de dependências, adição de plugin `maven-shade` ou `spring-boot-maven-plugin` (shade approach), mudança de `<packaging>war</packaging>` para `jar`
- `src/main/java/.../servlet/HibernateServlet.java`: substituído por inicialização programática do Tomcat embutido
- `src/main/resources/META-INF/persistence.xml`: URL JDBC e dialect atualizados para H2
- `am-sis.dockerfile`: imagem base e `CMD` alterados
- `docker-compose.yml`: remoção de `db` e `phpmyadmin`
- `build.sh`: simplificado — sem `mvn war:war` separado
- `README.md`: reescrito
- Todo o código Java que usa Hibernate Criteria API legada precisa ser migrado para JPA Criteria API (Hibernate 6 removeu a API legada)
