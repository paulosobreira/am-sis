## Context

Atualmente o projeto é empacotado como WAR e requer Tomcat externo e MySQL externo para rodar. O Docker Compose sobe 3 serviços (app + db + phpmyadmin). O código usa Hibernate Criteria API legada (depreciada desde Hibernate 5, removida no 6) e dependências com múltiplas CVEs ativas.

## Goals / Non-Goals

**Goals:**
- Zerar os alertas Dependabot ativos
- Produzir um único artefato JAR executável (`java -jar am-sis.jar`)
- Banco de dados embutido (H2 em modo persistente em arquivo) — sem serviço externo
- Docker Compose com serviço único
- Todos os comportamentos funcionais existentes preservados

**Non-Goals:**
- Migração para Spring Boot ou outro framework
- Mudança de frontend (JSPs continuam)
- Suporte a MySQL em produção (H2 passa a ser o banco único)
- Adição de testes automatizados

## Decisions

**Fat JAR com Tomcat embutido via `maven-shade` + bootstrap manual**
Alternativa considerada: `spring-boot-maven-plugin` (executableWar). Rejeitado — introduziria dependência ao Spring Boot. A abordagem adotada usa `tomcat-embed-core`, `tomcat-embed-jasper` e `tomcat-embed-el` inicializados em um `main()` em `EmbeddedServer.java`. O `maven-shade-plugin` agrega todas as dependências no JAR com `ManifestResourceTransformer` para definir `Main-Class`.

**H2 em modo persistente em arquivo (`./data/am-sis`)**
Alternativa: H2 em memória. Rejeitado — dados seriam perdidos a cada restart. O H2 em arquivo preserva dados entre reinicializações e não requer serviço externo. O dialect H2 é compatível com `hbm2ddl.auto=update`.

**Hibernate 6.x com JPA Criteria API**
Hibernate 6 remove completamente a Criteria API legada (`session.createCriteria()`). Toda query legada precisa ser migrada para `EntityManager.getCriteriaBuilder()` ou JPQL. Esta é a maior parte do esforço de migração.

**`commons-lang` → `commons-lang3`**
Artifact ID diferente (`org.apache.commons:commons-lang3`). Os pacotes mudam de `org.apache.commons.lang` para `org.apache.commons.lang3`. Todos os imports afetados devem ser atualizados.

**MySQL Connector → `com.mysql:mysql-connector-j` 9.x**
O artifact ID oficial mudou. `com.mysql.jdbc.Driver` passa a ser `com.mysql.cj.jdbc.Driver` (mas com H2 embutido o driver MySQL é removido do classpath de runtime).

**jQuery → 3.7.x**
Atualização dos arquivos estáticos em `src/main/webapp/`. Não há mudanças de API que afetam o código existente.

## Risks / Trade-offs

[Hibernate 6 quebra toda a Criteria API legada] → Migração manual query a query; todas as classes `*App.java` são afetadas. Risco de regressão comportamental em filtros compostos.

[H2 não é 100% compatível com MySQL em DDL/tipos] → `hbm2ddl.auto=update` recria as tabelas em H2; tipos `@Lob byte[]` e datas precisam ser validados.

[Fat JAR com BIRT pode ter conflitos de classloader] → BIRT usa OSGi internamente. Se o `maven-shade` causar conflitos, pode ser necessário usar `maven-assembly-plugin` com `zip` ou isolar o BIRT em um classloader separado.

[Dados existentes em MySQL não são migrados] → H2 começa com banco vazio; dados de produção precisam de script de migração manual fora do escopo desta mudança.

## Migration Plan

1. Atualizar `pom.xml` (dependências + plugins + packaging)
2. Migrar código Hibernate (Criteria API legada → JPA Criteria / JPQL)
3. Atualizar imports `commons-lang` → `commons-lang3`
4. Criar `EmbeddedServer.java` com bootstrap do Tomcat embutido
5. Atualizar `persistence.xml` para H2
6. Atualizar Dockerfile e `docker-compose.yml`
7. Atualizar jQuery e `build.sh`
8. Atualizar `README.md`
9. Build e smoke test local

**Rollback:** branch `master` antes do merge conserva o estado WAR+MySQL. Reverter via `git revert` se necessário.

## Open Questions

- O BIRT 4.5.0 é compatível com fat JAR (shade)? Se houver conflitos de OSGi, pode ser necessário `maven-assembly` com launcher customizado.
- A versão do Hibernate 6 compatível com `javax.persistence` (JPA 2.x) ou requer migração para `jakarta.persistence` (JPA 3.x)? Hibernate 6.0+ usa `jakarta.*` — todos os imports JPA precisam mudar de `javax.persistence.*` para `jakarta.persistence.*`.
