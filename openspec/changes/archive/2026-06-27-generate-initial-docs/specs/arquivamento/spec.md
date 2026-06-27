## ADDED Requirements

### Requirement: Criação de arquivamento
O sistema SHALL permitir a criação de um registro de arquivamento via `POST /rest/arquivamento` com token válido. O registro inclui código, descrição, empresa, tipo de arquivamento e opcionalmente observação, tipo de expurgo e data de expurgo. Ao criar, a versão é inicializada em 1, e a data e login de alteração são registrados automaticamente.

#### Scenario: Criação bem-sucedida
- **WHEN** cliente autenticado envia `POST /rest/arquivamento` com dados válidos e sem `id`
- **THEN** o sistema persiste o registro com versão 1 e retorna HTTP 200 com o objeto criado

#### Scenario: Criação sem token
- **WHEN** cliente envia `POST /rest/arquivamento` sem header `token`
- **THEN** o sistema retorna HTTP 401

### Requirement: Atualização de arquivamento com controle de versão otimista
O sistema SHALL atualizar um registro de arquivamento existente somente se a versão enviada for maior ou igual à versão armazenada no banco. Se a versão enviada for menor (registro foi alterado por outro usuário), a operação é rejeitada. A versão é incrementada em 1 a cada atualização.

#### Scenario: Atualização bem-sucedida
- **WHEN** cliente autenticado envia `POST /rest/arquivamento` com `id` e versão correta
- **THEN** o sistema atualiza o registro, incrementa a versão e retorna HTTP 200

#### Scenario: Conflito de versão (edição concorrente)
- **WHEN** cliente envia `POST /rest/arquivamento` com versão menor que a versão atual do banco
- **THEN** o sistema retorna HTTP 400 com mensagem indicando o login que fez a última alteração

### Requirement: Exclusão lógica (soft-delete) de arquivamento
O sistema SHALL marcar arquivamentos como excluídos (`apagado = true`) em vez de removê-los fisicamente do banco. A operação é realizada via `DELETE /rest/arquivamento` e requer autenticação.

#### Scenario: Exclusão lógica bem-sucedida
- **WHEN** cliente autenticado (não visitante) envia `DELETE /rest/arquivamento` com `id` válido
- **THEN** o sistema marca o campo `apagado` como `true` e retorna HTTP 200

#### Scenario: Visitante tenta excluir arquivamento
- **WHEN** cliente autenticado como visitante envia `DELETE /rest/arquivamento`
- **THEN** o sistema retorna HTTP 403 com mensagem "Exclusão não permitida"

### Requirement: Validação de campos obrigatórios do arquivamento
O sistema SHALL rejeitar criações e atualizações de arquivamento onde `codigo` ou `descricao` estejam ausentes ou em branco. Empresa e tipo de arquivamento também são obrigatórios.

#### Scenario: Criação sem código
- **WHEN** cliente envia `POST /rest/arquivamento` sem o campo `codigo`
- **THEN** o sistema retorna HTTP 400 com mensagem de campo obrigatório

#### Scenario: Criação sem descrição
- **WHEN** cliente envia `POST /rest/arquivamento` sem o campo `descricao`
- **THEN** o sistema retorna HTTP 400 com mensagem de campo obrigatório
