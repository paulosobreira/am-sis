## ADDED Requirements

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
- **THEN** os arquivos `data/am-sis.mv.db` são criados e o schema é inicializado

### Requirement: Imagem Docker baseada em fat JAR
O sistema SHALL ter uma imagem Docker que usa `openjdk:11-jre-slim` como base e executa o fat JAR via `java -jar`. O `docker-compose.yml` SHALL definir apenas o serviço da aplicação, sem dependências de banco ou ferramentas de administração externas. A porta 8080 do container SHALL ser mapeada para a porta 80 do host.

#### Scenario: Build e execução via Docker
- **WHEN** o usuário executa `docker build` seguido de `docker compose up`
- **THEN** a aplicação sobe em `http://localhost` sem necessidade de serviços adicionais

#### Scenario: Dados persistidos via volume Docker
- **WHEN** o container é reiniciado
- **THEN** os dados do H2 persistem através de volume mapeado para o diretório `./data`
