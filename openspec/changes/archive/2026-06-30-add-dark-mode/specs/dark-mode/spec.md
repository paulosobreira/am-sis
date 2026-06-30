## ADDED Requirements

### Requirement: Salvar preferência de dark mode do usuário autenticado
O sistema SHALL persistir a preferência de dark mode do usuário autenticado via `POST /rest/usuario/darkmode`. O campo `darkMode` (boolean) é atualizado na entidade `Usuario` identificada pelo token no header. Usuários visitantes (`guest-*`) não podem salvar preferência. Token inválido retorna HTTP 401.

#### Scenario: Ativar dark mode com sucesso
- **WHEN** usuário autenticado (não visitante) envia `POST /rest/usuario/darkmode` com body `{"darkMode": true}` e token válido no header
- **THEN** o sistema atualiza `darkMode = true` no registro do usuário e retorna HTTP 200 com o objeto `Usuario` atualizado

#### Scenario: Desativar dark mode com sucesso
- **WHEN** usuário autenticado envia `POST /rest/usuario/darkmode` com body `{"darkMode": false}` e token válido
- **THEN** o sistema atualiza `darkMode = false` no registro do usuário e retorna HTTP 200

#### Scenario: Visitante tenta salvar preferência
- **WHEN** cliente autenticado como visitante (`guest-*`) envia `POST /rest/usuario/darkmode`
- **THEN** o sistema retorna HTTP 403

#### Scenario: Token inválido
- **WHEN** cliente envia `POST /rest/usuario/darkmode` com token ausente ou inválido
- **THEN** o sistema retorna HTTP 401

### Requirement: Aplicar dark mode ao carregar página autenticada
O sistema (frontend) SHALL ler a preferência `darkMode` armazenada no `sessionStorage` ao carregar qualquer página autenticada e aplicar a classe CSS `dark` no elemento `<body>` quando a preferência for verdadeira.

#### Scenario: Usuário com dark mode ativado acessa uma página
- **WHEN** página autenticada é carregada e `sessionStorage` contém `darkMode = "true"`
- **THEN** o frontend adiciona a classe `dark` ao `<body>` antes da renderização visível

#### Scenario: Usuário com dark mode desativado acessa uma página
- **WHEN** página autenticada é carregada e `sessionStorage` contém `darkMode = "false"` ou ausente
- **THEN** o frontend não adiciona a classe `dark` ao `<body>`, mantendo o tema claro

### Requirement: Toggle de dark mode na UI
O sistema (frontend) SHALL exibir um controle de toggle de dark mode no cabeçalho de todas as páginas autenticadas. Ao acionar o toggle, o frontend SHALL chamar `POST /rest/usuario/darkmode`, atualizar `sessionStorage` e aplicar/remover a classe `dark` no `<body>` imediatamente.

#### Scenario: Usuário aciona o toggle de dark mode
- **WHEN** usuário clica no botão de toggle de dark mode
- **THEN** o frontend envia `POST /rest/usuario/darkmode` com o novo estado, atualiza `sessionStorage.darkMode` e aplica/remove a classe `dark` no `<body>` imediatamente, sem recarregar a página

#### Scenario: Falha ao salvar preferência no servidor
- **WHEN** a chamada ao endpoint falha (erro de rede ou servidor)
- **THEN** o frontend reverte a classe `dark` ao estado anterior e exibe mensagem de erro via alert
