# gestao-tipos Specification

## Purpose
TBD - created by archiving change generate-initial-docs. Update Purpose after archive.
## Requirements
### Requirement: CRUD de tipo de arquivamento
O sistema SHALL permitir criar, atualizar, excluir e listar tipos de arquivamento via endpoint `/rest/tipoArquivamento`. O campo `descricao` é único e obrigatório. Visitantes não podem excluir.

#### Scenario: Criação de tipo de arquivamento
- **WHEN** cliente autenticado envia `POST /rest/tipoArquivamento` com `descricao` preenchida e sem `id`
- **THEN** o sistema cria o tipo e retorna HTTP 200 com o objeto criado

#### Scenario: Atualização de tipo de arquivamento
- **WHEN** cliente autenticado envia `POST /rest/tipoArquivamento` com `id` e `descricao`
- **THEN** o sistema atualiza o registro e retorna HTTP 200

#### Scenario: Exclusão de tipo de arquivamento por não-visitante
- **WHEN** cliente autenticado (não visitante) envia `DELETE /rest/tipoArquivamento` com `id` válido
- **THEN** o sistema remove o registro e retorna HTTP 200

#### Scenario: Visitante tenta excluir tipo de arquivamento
- **WHEN** cliente autenticado como visitante envia `DELETE /rest/tipoArquivamento`
- **THEN** o sistema retorna HTTP 403 com mensagem "Exclusão não permitida"

#### Scenario: Listagem de tipos de arquivamento
- **WHEN** cliente autenticado envia `GET /rest/tipoArquivamento`
- **THEN** o sistema retorna HTTP 200 com lista de tipos ordenados por descrição ascendente

### Requirement: CRUD de tipo de expurgo
O sistema SHALL permitir criar, atualizar, excluir e listar tipos de expurgo via endpoint `/rest/tipoExpurgo`. O campo `descricao` é único e obrigatório. Visitantes não podem excluir. O comportamento segue o mesmo padrão do tipo de arquivamento.

#### Scenario: Criação de tipo de expurgo
- **WHEN** cliente autenticado envia `POST /rest/tipoExpurgo` com `descricao` preenchida e sem `id`
- **THEN** o sistema cria o tipo de expurgo e retorna HTTP 200 com o objeto criado

#### Scenario: Atualização de tipo de expurgo
- **WHEN** cliente autenticado envia `POST /rest/tipoExpurgo` com `id` e `descricao`
- **THEN** o sistema atualiza o registro e retorna HTTP 200

#### Scenario: Exclusão de tipo de expurgo por não-visitante
- **WHEN** cliente autenticado (não visitante) envia `DELETE /rest/tipoExpurgo` com `id` válido
- **THEN** o sistema remove o registro e retorna HTTP 200

#### Scenario: Visitante tenta excluir tipo de expurgo
- **WHEN** cliente autenticado como visitante envia `DELETE /rest/tipoExpurgo`
- **THEN** o sistema retorna HTTP 403 com mensagem "Exclusão não permitida"

#### Scenario: Listagem de tipos de expurgo
- **WHEN** cliente autenticado envia `GET /rest/tipoExpurgo`
- **THEN** o sistema retorna HTTP 200 com lista de tipos de expurgo

