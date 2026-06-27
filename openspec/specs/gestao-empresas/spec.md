# gestao-empresas Specification

## Purpose
TBD - created by archiving change generate-initial-docs. Update Purpose after archive.
## Requirements
### Requirement: Criação e atualização de empresa
O sistema SHALL permitir criar ou atualizar empresas via `POST /rest/empresa` com token válido. Se o campo `id` não estiver presente, o registro é criado; caso contrário, é atualizado. O campo `nome` é obrigatório.

#### Scenario: Criação de empresa bem-sucedida
- **WHEN** cliente autenticado envia `POST /rest/empresa` com `nome` preenchido e sem `id`
- **THEN** o sistema persiste a empresa e retorna HTTP 200 com o objeto criado

#### Scenario: Atualização de empresa existente
- **WHEN** cliente autenticado envia `POST /rest/empresa` com `id` e `nome` preenchidos
- **THEN** o sistema atualiza o registro e retorna HTTP 200 com o objeto atualizado

#### Scenario: Criação sem token válido
- **WHEN** cliente envia `POST /rest/empresa` sem token ou com token inválido
- **THEN** o sistema retorna HTTP 401

### Requirement: Exclusão de empresa
O sistema SHALL excluir fisicamente uma empresa via `DELETE /rest/empresa`. Visitantes não têm permissão para excluir. É necessário fornecer o `id` da empresa no corpo da requisição.

#### Scenario: Exclusão bem-sucedida por usuário não-visitante
- **WHEN** cliente autenticado (não visitante) envia `DELETE /rest/empresa` com `id` válido
- **THEN** o sistema remove o registro e retorna HTTP 200

#### Scenario: Visitante tenta excluir empresa
- **WHEN** cliente autenticado como visitante envia `DELETE /rest/empresa`
- **THEN** o sistema retorna HTTP 403 com mensagem "Exclusão não permitida"

### Requirement: Listagem de empresas
O sistema SHALL retornar a lista de empresas cadastradas via `GET /rest/empresa`, ordenadas por nome, para qualquer cliente com token válido.

#### Scenario: Listagem bem-sucedida
- **WHEN** cliente autenticado envia `GET /rest/empresa`
- **THEN** o sistema retorna HTTP 200 com lista de empresas ordenadas por nome ascendente

