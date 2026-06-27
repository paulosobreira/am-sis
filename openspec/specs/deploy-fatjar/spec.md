# deploy-fatjar Specification

## Purpose
Empacotamento e execução da aplicação como fat JAR autossuficiente com Tomcat 9 embutido, banco H2 persistente e logging em arquivo. Não requer servidor de aplicação, banco de dados ou configuração externa para rodar. Deploy via Docker também suportado.

## Requirements

### Requirement: Build como fat JAR executável
O sistema SHALL ser empacotado como um único arquivo JAR executável contendo todas as dependências, o servidor Tomcat embutido e o banco H2, produzido pelo comando `mvn clean package`. O artefato gerado SHALL ser executável diretamente via `java -jar am-sis.jar` sem necessidade de servidor de aplicação externo.

#### Scenario: Build bem-sucedido gera fat JAR
- **WHEN** o desenvolvedor executa `mvn clean package`
- **THEN** o arquivo `target/am-sis.jar` é gerado com todas as dependências incluídas e com `Main-Class` definida no `MANIFEST.MF`

#### Scenario: Execução direta do JAR
- **WHEN** o usuário executa `java -jar target/am-sis.jar`
- **THEN** o servidor inicia na porta 8080, o banco H2 é inicializado em `./data/am-sis` e a aplicação fica acessível em `http://localhost:8080/am-sis`

### Requirement: Banco de dados H2 embutido e persistente
O sistema SHALL usar H2 como banco de dados embutido em modo persistente em arquivo (`./data/am-sis`). O schema SHALL ser criado e atualizado automaticamente no boot via `hbm2ddl.auto=update`. Nenhum serviço de banco de dados externo SHALL ser necessário para rodar a aplicação.

#### Scenario: Dados persistem entre reinicializações
- **WHEN** o usuário para e reinicia a aplicação
- **THEN** os dados cadastrados anteriormente continuam disponíveis

#### Scenario: Schema criado automaticamente na primeira execução
- **WHEN** a aplicação é iniciada pela primeira vez em um diretório sem arquivos de banco
- **THEN** o arquivo `data/am-sis.mv.db` é criado e o schema é inicializado com dados seed (usuário admin, empresa padrão)

### Requirement: Logging em arquivo com rotação
O sistema SHALL gravar todos os logs de aplicação em arquivo `logs/am-sis.log` no diretório de trabalho. O arquivo usa rotação por data e tamanho (máximo 10 MB por arquivo, retenção de 7 dias). Logs de bibliotecas de framework (Tomcat, Hibernate, Jersey) são filtrados em nível WARN; logs da aplicação (`br.com.am`) em nível DEBUG. Logs do JUL (Java Util Logging, usado por Tomcat) são redirecionados para o mesmo arquivo via bridge SLF4J.

#### Scenario: Log gravado em arquivo no startup
- **WHEN** a aplicação é iniciada
- **THEN** o arquivo `logs/am-sis.log` é criado (se não existir) e as mensagens de startup são gravadas nele

#### Scenario: Erros de aplicação registrados com stack trace
- **WHEN** ocorre uma exceção não tratada em qualquer componente REST
- **THEN** o stack trace completo é gravado em `logs/am-sis.log` com nível ERROR e timestamp

#### Scenario: Rotação automática de logs
- **WHEN** o arquivo de log atinge 10 MB ou muda o dia
- **THEN** o arquivo atual é renomeado para `logs/am-sis.YYYY-MM-DD.N.log` e um novo arquivo é iniciado

### Requirement: Imagem Docker baseada em fat JAR
O sistema SHALL ter uma imagem Docker que usa `openjdk:11-jre-slim` como base e executa o fat JAR via `java -jar`. O `docker-compose.yml` SHALL definir apenas o serviço da aplicação, sem dependências de banco ou ferramentas externas. A porta 8080 do container SHALL ser mapeada para a porta 80 do host. Dados do H2 e logs são persistidos via volumes.

#### Scenario: Build e execução via Docker
- **WHEN** o usuário executa `docker build` seguido de `docker compose up`
- **THEN** a aplicação sobe em `http://localhost` sem necessidade de serviços adicionais

#### Scenario: Dados e logs persistidos via volume Docker
- **WHEN** o container é reiniciado
- **THEN** os dados do H2 e os logs persistem através de volumes mapeados para `./data` e `./logs`
