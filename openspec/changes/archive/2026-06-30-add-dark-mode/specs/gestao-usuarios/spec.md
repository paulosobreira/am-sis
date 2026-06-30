## MODIFIED Requirements

### Requirement: Autenticação retorna preferência de dark mode
O sistema SHALL retornar o campo `darkMode` (Boolean) no objeto `Usuario` da resposta de `POST /rest/login`, para que o frontend possa aplicar o tema correto imediatamente após o login sem chamada adicional ao servidor.

#### Scenario: Login bem-sucedido retorna darkMode
- **WHEN** cliente envia `POST /rest/login` com credenciais válidas
- **THEN** o sistema retorna HTTP 200 com o objeto `Usuario` incluindo o campo `darkMode` (true, false ou null — tratado como false pelo frontend)

## ADDED Requirements

### Requirement: Campo darkMode na entidade Usuario
O sistema SHALL persistir o campo `darkMode` (Boolean, nullable) na tabela `am_usuario` (coluna `dark_mode`). O valor `null` é equivalente a `false` (tema claro) para fins de aplicação de tema no frontend. O campo é gerenciado exclusivamente pelo endpoint `POST /rest/usuario/darkmode`.

#### Scenario: Novo usuário criado sem preferência explícita
- **WHEN** um novo usuário é criado via `POST /rest/usuario`
- **THEN** o campo `darkMode` do usuário criado é `null` (equivalente a tema claro)

#### Scenario: Campo darkMode persistido e recuperado
- **WHEN** o campo `darkMode` de um usuário é atualizado para `true` via `POST /rest/usuario/darkmode`
- **THEN** consultas subsequentes ao usuário retornam `darkMode = true`
