## ADDED Requirements

### Requirement: Criação de usuário com senha gerada automaticamente
O sistema SHALL criar um usuário via `POST /rest/usuario` com token válido. Os campos `nome` e `login` são obrigatórios. A senha é gerada automaticamente pelo sistema (senha aleatória), convertida para MD5 e armazenada. A senha em texto claro é retornada apenas no objeto de resposta da criação (campo `senhaStr`). Novos usuários são criados com `ativo = true`.

#### Scenario: Criação bem-sucedida de usuário
- **WHEN** cliente autenticado envia `POST /rest/usuario` com `nome` e `login` preenchidos e sem `id`
- **THEN** o sistema cria o usuário com senha gerada, `ativo = true`, e retorna HTTP 200 com o objeto incluindo a senha em texto claro no campo `senhaStr`

#### Scenario: Criação sem nome
- **WHEN** cliente envia `POST /rest/usuario` sem `nome`
- **THEN** o sistema retorna HTTP 400 com mensagem "Nome obrigatório."

#### Scenario: Criação sem login
- **WHEN** cliente envia `POST /rest/usuario` sem `login`
- **THEN** o sistema retorna HTTP 400 com mensagem "Login obrigatório."

### Requirement: Atualização de usuário
O sistema SHALL atualizar dados de um usuário existente via `POST /rest/usuario` com `id` presente. A senha também é regerada a cada atualização (gerada aleatoriamente e convertida para MD5).

#### Scenario: Atualização bem-sucedida
- **WHEN** cliente autenticado envia `POST /rest/usuario` com `id`, `nome` e `login` preenchidos
- **THEN** o sistema atualiza o registro com nova senha gerada e retorna HTTP 200

### Requirement: Desativação lógica de usuário
O sistema SHALL desativar um usuário via `DELETE /rest/usuario` marcando `ativo = false` (soft-delete). Visitantes não podem executar esta operação. Requer `id` do usuário no corpo da requisição.

#### Scenario: Desativação bem-sucedida
- **WHEN** cliente autenticado (não visitante) envia `DELETE /rest/usuario` com `id` válido
- **THEN** o sistema define `ativo = false` no usuário e retorna HTTP 200

#### Scenario: Tentativa de desativar usuário sem ID
- **WHEN** cliente envia `DELETE /rest/usuario` sem `id` no corpo
- **THEN** o sistema retorna HTTP 400 com mensagem "Usuário inválido"

#### Scenario: Visitante tenta desativar usuário
- **WHEN** cliente autenticado como visitante envia `DELETE /rest/usuario`
- **THEN** o sistema retorna HTTP 403 com mensagem "Exclusão não permitida"

### Requirement: Listagem de usuários
O sistema SHALL retornar todos os usuários cadastrados (ativos e inativos) via `GET /rest/usuario`, ordenados por nome, para qualquer cliente com token válido.

#### Scenario: Listagem bem-sucedida
- **WHEN** cliente autenticado envia `GET /rest/usuario`
- **THEN** o sistema retorna HTTP 200 com lista de usuários ordenados por nome ascendente
