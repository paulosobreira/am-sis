# pesquisa-arquivamento Specification

## Purpose
TBD - created by archiving change generate-initial-docs. Update Purpose after archive.
## Requirements
### Requirement: Pesquisa de arquivamentos com filtros combinados
O sistema SHALL permitir busca de arquivamentos via `POST /rest/pesquisaArquivamento` com token válido. Os filtros disponíveis são: `id` (exato), `tipoArquivamento` (exato), `codigo` (contém, case-insensitive), `descricao` (contém, case-insensitive), `dataReferencia` (exato, mês/ano) e intervalo de `dataExpurgo` (data inicial e final). Filtros não fornecidos são ignorados. Resultados são retornados ordenados por `descricao` ascendente.

#### Scenario: Pesquisa sem filtros retorna todos os arquivamentos
- **WHEN** cliente autenticado envia `POST /rest/pesquisaArquivamento` com corpo vazio ou nulo
- **THEN** o sistema retorna HTTP 200 com lista de todos os arquivamentos ordenados por descrição

#### Scenario: Pesquisa por código (busca parcial)
- **WHEN** cliente envia `POST /rest/pesquisaArquivamento` com campo `codigo` preenchido
- **THEN** o sistema retorna somente arquivamentos cujo código contenha o valor informado (busca anywhere)

#### Scenario: Pesquisa por descrição (busca parcial)
- **WHEN** cliente envia `POST /rest/pesquisaArquivamento` com campo `descricao` preenchido
- **THEN** o sistema retorna somente arquivamentos cuja descrição contenha o valor informado

#### Scenario: Pesquisa por intervalo de data de expurgo
- **WHEN** cliente envia `POST /rest/pesquisaArquivamento` com `dataExpurgoStrINI` e `dataExpurgoStrFIM` preenchidos
- **THEN** o sistema retorna somente arquivamentos com `dataExpurgo` dentro do intervalo (inclusive)

#### Scenario: Pesquisa sem token válido
- **WHEN** cliente envia `POST /rest/pesquisaArquivamento` com token inválido ou ausente
- **THEN** o sistema retorna HTTP 401 com mensagem "Token inválido"

### Requirement: Pesquisa filtra arquivamentos excluídos logicamente
O sistema SHALL excluir da pesquisa os registros marcados com `apagado = true`, retornando apenas arquivamentos ativos.

#### Scenario: Arquivamentos excluídos não aparecem na pesquisa
- **WHEN** cliente realiza pesquisa sem filtro de id específico
- **THEN** o sistema não inclui na resposta registros com `apagado = true`

