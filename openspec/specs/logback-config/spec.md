# Logback Configuration

## Purpose

Configurar o Logback como sistema único de logging da aplicação, com saída para arquivo com rotação, console para desenvolvimento, e integração com JUL-to-SLF4J para capturar logs do Tomcat e demais bibliotecas.

## Requirements

### Requirement: Logback configuration file
O sistema DEVE incluir um arquivo `logback.xml` em `src/main/resources/` que configure o Logback como único mecanismo de logging.

#### Scenario: logback.xml exists on classpath
- **WHEN** a aplicação inicia
- **THEN** o Logback deve carregar `logback.xml` do classpath automaticamente
- **THEN** nenhum warning de "No appender configured" deve aparecer

### Requirement: File appender with rotation
O Logback DEVE gravar logs em arquivo com rotação baseada em tamanho e data, mantendo um histórico de no máximo 30 dias ou 500MB por arquivo.

#### Scenario: log file is created
- **WHEN** a aplicação grava uma mensagem de log
- **THEN** o arquivo `logs/am-sis.log` deve ser criado com a mensagem

#### Scenario: log rotation by size
- **WHEN** o arquivo de log atinge 500MB
- **THEN** o Logback deve rotacionar o arquivo com sufixo de data/hora

#### Scenario: log cleanup
- **WHEN** um arquivo de log rotacionado tem mais de 30 dias
- **THEN** o Logback deve removê-lo automaticamente

### Requirement: Log levels per package
O Logback DEVE permitir configurar níveis de log diferentes por pacote, com suporte a herança de nível. O nível global DEVE ser WARN, o pacote `br.com.am` DEVE ser INFO.

#### Scenario: application logs at INFO level
- **WHEN** o código em `br.com.am` grava `log.info()`
- **THEN** a mensagem aparece no arquivo de log

#### Scenario: third-party logs at WARN level
- **WHEN** uma biblioteca terceira (ex: Tomcat interno) grava `log.debug()`
- **THEN** a mensagem NÃO aparece no arquivo de log (nível WARN global)

### Requirement: JUL-to-SLF4J bridge integration
O sistema DEVE redirecionar todo logging de `java.util.logging` (usado por Tomcat) para SLF4J/Logback, garantindo que logs do Catalina (Tomcat) e demais bibliotecas que usam JUL apareçam no mesmo arquivo `logs/am-sis.log`.

#### Scenario: Tomcat logs appear in Logback file
- **WHEN** o Tomcat embutido inicializa
- **THEN** mensagens do Catalina/JUL devem aparecer em `logs/am-sis.log`

#### Scenario: bridge installed before Tomcat starts
- **WHEN** a aplicação inicia (`EmbeddedServer.main()`)
- **THEN** `SLF4JBridgeHandler.install()` deve ser chamado antes de `tomcat.start()`

### Requirement: Console appender for development
Em ambiente de desenvolvimento, o Logback DEVE exibir logs no console com formato colorido para facilitar debug.

#### Scenario: console output
- **WHEN** a aplicação roda
- **THEN** logs devem aparecer no stdout com cores e formato legível
