# relatorio-arquivamento Specification

## Purpose
Geração de relatório HTML imprimível de arquivamentos. O relatório exibe logo da empresa, código, descrição, observação e data de referência em layout de duas cópias por página, gerado pelo motor Thymeleaf a partir do template `relatorio-arquivamento.html`. O usuário imprime ou salva em PDF diretamente pelo browser (Ctrl+P).

## Requirements

### Requirement: Geração de relatório HTML de lista de arquivamentos
O sistema SHALL gerar um relatório HTML a partir de uma lista de arquivamentos via `POST /rest/relatorioArquivamento/gerar`. O HTML gerado é armazenado em cache na memória do servidor, identificado por um timestamp. O endpoint retorna o timestamp como chave de acesso ao relatório.

#### Scenario: Geração bem-sucedida de relatório
- **WHEN** cliente envia `POST /rest/relatorioArquivamento/gerar` com lista não-vazia de arquivamentos
- **THEN** o sistema gera o HTML via Thymeleaf, armazena em cache e retorna HTTP 200 com o timestamp (chave de acesso)

#### Scenario: Geração com lista vazia
- **WHEN** cliente envia `POST /rest/relatorioArquivamento/gerar` com lista vazia ou nula
- **THEN** o sistema retorna HTTP 400 com mensagem "Relatório Vazio"

### Requirement: Acesso ao relatório gerado por chave
O sistema SHALL disponibilizar o HTML previamente gerado via `GET /rest/relatorioArquivamento/imprimir/{timestamp}`, onde `timestamp` é a chave retornada na geração. O relatório fica disponível por no máximo 60 segundos; após esse período é removido do cache.

#### Scenario: Acesso bem-sucedido ao relatório
- **WHEN** cliente acessa `GET /rest/relatorioArquivamento/imprimir/{timestamp}` com chave válida e dentro do prazo
- **THEN** o sistema retorna HTTP 200 com o conteúdo HTML (`Content-Type: text/html`)

#### Scenario: Acesso com chave expirada ou inválida
- **WHEN** cliente acessa `GET /rest/relatorioArquivamento/imprimir/{timestamp}` com chave inexistente ou expirada
- **THEN** o sistema retorna HTTP 404

### Requirement: Geração de relatório por ID
O sistema SHALL gerar relatório individual para um arquivamento via `GET /rest/relatorioArquivamento/gerarHtml/{id}` e `GET /rest/relatorioArquivamento/gerarPdf/{id}`. Ambos os endpoints retornam HTML; o usuário usa o browser para salvar em PDF. O relatório inclui a logo da empresa vinculada ao arquivamento, se disponível.

#### Scenario: Geração de relatório por ID
- **WHEN** cliente acessa `GET /rest/relatorioArquivamento/gerarHtml/{id}` ou `gerarPdf/{id}` com ID válido
- **THEN** o sistema retorna HTTP 200 com conteúdo HTML imprimível (`Content-Type: text/html`)

#### Scenario: ID inexistente
- **WHEN** cliente acessa endpoint de geração com ID não encontrado no banco
- **THEN** o sistema retorna HTTP 404 com mensagem "Arquivamento não encontrado"

### Requirement: Inclusão de logo da empresa no relatório
O sistema SHALL incluir a imagem de logo da empresa vinculada ao arquivamento no relatório, se a empresa possuir `idArquivo` cadastrado. A URL da imagem é construída apontando para `GET /rest/binario/downloadImg?id={idArquivo}`.

#### Scenario: Relatório com logo
- **WHEN** o arquivamento possui empresa com `idArquivo` não nulo
- **THEN** o relatório exibe a imagem via URL do endpoint de download de binário

#### Scenario: Relatório sem logo
- **WHEN** o arquivamento não possui empresa ou a empresa não tem `idArquivo`
- **THEN** o relatório exibe o espaço reservado sem imagem, sem erro
