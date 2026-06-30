## ADDED Requirements

### Requirement: Relatório de arquivamento em HTML
O sistema DEVE renderizar relatórios de arquivamento usando Thymeleaf, com template em `src/main/resources/templates/relatorio-arquivamento.html` e engine gerenciada por `br.com.am.util.RelatorioEngine`.

#### Scenario: render relatório via RelatorioEngine
- **WHEN** `RelatorioEngine.renderArquivamento(arquivamentos)` é chamado
- **THEN** o Thymeleaf processa o template `relatorio-arquivamento.html` com a lista de arquivamentos
- **THEN** retorna uma String HTML completa

### Requirement: Endpoint REST /rest/relatorioArquivamento/gerar
O endpoint `POST /rest/relatorioArquivamento/gerar` DEVE aceitar uma lista de objetos `Arquivamento` no corpo da requisição e retornar uma chave numérica (timestamp) para recuperar o HTML renderizado.

#### Scenario: gerar relatório com sucesso
- **WHEN** uma requisição POST com body JSON contendo lista de arquivamentos válidos
- **THEN** o sistema retorna HTTP 200 com uma chave numérica (Long)
- **THEN** o HTML fica disponível em cache por 60 segundos em `/rest/relatorioArquivamento/imprimir/{chave}`

### Requirement: Endpoint REST /rest/relatorioArquivamento/imprimir/{chave}
O endpoint GET deve recuperar o HTML previamente gerado pela chave.

#### Scenario: imprimir relatório existente
- **WHEN** uma requisição GET com chave válida (< 60s desde a geração)
- **THEN** o sistema retorna HTTP 200 com `Content-Type: text/html;charset=UTF-8`
- **THEN** o HTML contém os dados dos arquivamentos formatados

#### Scenario: chave expirada ou inválida
- **WHEN** a chave não existe ou expirou (> 60s)
- **THEN** o sistema retorna HTTP 404

### Requirement: Endpoint REST /rest/relatorioArquivamento/gerarHtml/{id}
O endpoint GET deve buscar um arquivamento pelo ID no banco e renderizar o HTML diretamente.

#### Scenario: gerar HTML por ID
- **WHEN** um ID de arquivamento válido é informado
- **THEN** o sistema busca no banco, renderiza com Thymeleaf e retorna HTTP 200 com HTML

### Requirement: Sem dependências do BIRT
O sistema NÃO DEVE depender de nenhuma classe ou artefato do BIRT para funcionar. Todo código BIRT deve ser removido.

#### Scenario: BIRT classes removed
- **WHEN** o projeto compila
- **THEN** `BirtEngine.java`, `BirtDataSet.java`, `WebReport.java` não devem existir

#### Scenario: BIRT resources removed
- **WHEN** o projeto compila
- **THEN** `arquivamento.rptdesign` e `plugin.xml` não devem existir

#### Scenario: BIRT config removed
- **WHEN** o `config.properties` é carregado
- **THEN** não deve conter propriedades `birtLog` ou `birtLogDir`
