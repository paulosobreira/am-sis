## ADDED Requirements

### Requirement: Geração de relatório PDF de arquivamentos
O sistema SHALL gerar um relatório PDF a partir de uma lista de arquivamentos via `POST /rest/relatorioArquivamento/gerar` com token válido. O relatório é gerado pelo motor BIRT usando o template `arquivamento.rptdesign`. O PDF gerado é armazenado em cache na memória do servidor, identificado por um timestamp. O endpoint retorna o timestamp como chave de acesso ao relatório.

#### Scenario: Geração bem-sucedida de relatório PDF
- **WHEN** cliente autenticado envia `POST /rest/relatorioArquivamento/gerar` com lista não-vazia de arquivamentos
- **THEN** o sistema gera o PDF, armazena em cache e retorna HTTP 200 com o timestamp (chave de acesso)

#### Scenario: Geração com lista vazia
- **WHEN** cliente envia `POST /rest/relatorioArquivamento/gerar` com lista vazia ou nula
- **THEN** o sistema retorna HTTP 400 com mensagem "Relatório Vazio"

#### Scenario: Geração sem token válido
- **WHEN** cliente envia `POST /rest/relatorioArquivamento/gerar` sem token ou com token inválido
- **THEN** o sistema retorna HTTP 401

### Requirement: Download do relatório PDF gerado
O sistema SHALL disponibilizar o PDF previamente gerado via `GET /rest/relatorioArquivamento/imprimir/{timestamp}`, onde `timestamp` é a chave retornada na geração. O relatório fica disponível por no máximo 60 segundos após a geração; após esse período é removido do cache.

#### Scenario: Download bem-sucedido do relatório
- **WHEN** cliente acessa `GET /rest/relatorioArquivamento/imprimir/{timestamp}` com chave válida e dentro do prazo
- **THEN** o sistema retorna HTTP 200 com o conteúdo PDF (`Content-Type: application/pdf`)

#### Scenario: Download com chave expirada ou inválida
- **WHEN** cliente acessa `GET /rest/relatorioArquivamento/imprimir/{timestamp}` com chave inexistente ou expirada
- **THEN** o sistema retorna HTTP 500

### Requirement: Geração de relatório por ID (HTML e PDF)
O sistema SHALL gerar relatório individual para um arquivamento via `GET /rest/relatorioArquivamento/gerarHtml/{id}` (formato HTML) e `GET /rest/relatorioArquivamento/gerarPdf/{id}` (formato PDF). O relatório inclui a logo da empresa vinculada ao arquivamento, se disponível.

#### Scenario: Geração de relatório HTML por ID
- **WHEN** cliente acessa `GET /rest/relatorioArquivamento/gerarHtml/{id}` com ID válido
- **THEN** o sistema retorna HTTP 200 com conteúdo HTML do relatório

#### Scenario: Geração de relatório PDF por ID
- **WHEN** cliente acessa `GET /rest/relatorioArquivamento/gerarPdf/{id}` com ID válido
- **THEN** o sistema retorna HTTP 200 com conteúdo PDF (`Content-Type: application/pdf`)

#### Scenario: ID inexistente
- **WHEN** cliente acessa endpoint de geração com ID não encontrado no banco
- **THEN** o sistema retorna HTTP 400 com mensagem "Arquivamento não encontrado"
