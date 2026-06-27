# autenticacao Specification

## Purpose
TBD - created by archiving change generate-initial-docs. Update Purpose after archive.
## Requirements
### Requirement: Login com credenciais de administrador
O sistema SHALL autenticar um usuário administrador usando login e senha configurados via `config.properties` (chaves `admin` e `pass`). A senha é comparada com hash MD5. O token gerado é único por sessão e armazenado em memória estática.

#### Scenario: Login de administrador bem-sucedido
- **WHEN** o cliente envia `POST /rest/login` com login e senha corretos do administrador
- **THEN** o sistema retorna HTTP 200 com objeto usuário contendo nome "Administrador" e token de sessão gerado

#### Scenario: Login de administrador com senha incorreta
- **WHEN** o cliente envia `POST /rest/login` com login do administrador e senha incorreta
- **THEN** o sistema não autentica como administrador e tenta autenticação normal no banco de dados

### Requirement: Login como visitante (guest)
O sistema SHALL autenticar qualquer cliente que envie login `"guest"` e senha `"guest"` sem verificação no banco de dados. O token gerado começa com prefixo `"guest-"`. Visitantes têm acesso de leitura mas não podem realizar exclusões.

#### Scenario: Login como visitante
- **WHEN** o cliente envia `POST /rest/login` com login `"guest"` e senha `"guest"`
- **THEN** o sistema retorna HTTP 200 com nome "Visitante" e token com prefixo `"guest-"`

### Requirement: Login com usuário cadastrado no banco
O sistema SHALL autenticar usuários cadastrados na tabela `am_usuario` comparando a senha fornecida (hashed MD5) com o valor armazenado. Somente usuários com `ativo = true` podem receber token. Após login bem-sucedido, o token é salvo no banco e a data de acesso é atualizada.

#### Scenario: Login bem-sucedido de usuário cadastrado
- **WHEN** o cliente envia `POST /rest/login` com login e senha válidos de usuário ativo
- **THEN** o sistema retorna HTTP 200 com objeto usuário contendo token de sessão

#### Scenario: Login com usuário inexistente
- **WHEN** o cliente envia `POST /rest/login` com login não cadastrado
- **THEN** o sistema retorna HTTP 400 com mensagem "Cadastro inválido."

#### Scenario: Login com senha incorreta
- **WHEN** o cliente envia `POST /rest/login` com login válido e senha errada
- **THEN** o sistema retorna HTTP 400 com mensagem "Cadastro inválido."

### Requirement: Validação de token em endpoints protegidos
Todos os endpoints protegidos SHALL verificar o token recebido no header `token`. O sistema aceita tokens de administrador (memória), tokens com prefixo `"guest-"` e tokens de usuários ativos no banco. Token ausente ou inválido resulta em rejeição.

#### Scenario: Requisição com token válido
- **WHEN** um endpoint protegido recebe header `token` com valor válido
- **THEN** o sistema processa a requisição normalmente

#### Scenario: Requisição sem token ou com token inválido
- **WHEN** um endpoint protegido recebe header `token` ausente ou com valor não reconhecido
- **THEN** o sistema retorna HTTP 401 com mensagem "Token inválido"

### Requirement: Restrição de operações para visitante
O sistema SHALL impedir que usuários com papel de visitante realizem operações de exclusão (DELETE). Demais operações de leitura e escrita são permitidas ao visitante.

#### Scenario: Visitante tenta excluir um registro
- **WHEN** um cliente autenticado como visitante envia requisição DELETE para qualquer recurso
- **THEN** o sistema retorna HTTP 403 com mensagem "Exclusão não permitida"

