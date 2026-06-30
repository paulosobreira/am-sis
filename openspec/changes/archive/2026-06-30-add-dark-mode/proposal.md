## Why

A aplicação não oferece controle de tema visual por usuário, forçando todos a usar o tema claro independentemente de preferência. Implementar dark mode com persistência por usuário melhora a experiência de uso, especialmente em ambientes com pouca iluminação.

## What Changes

- Novo campo `darkMode` (boolean) na entidade `Usuario`, persistido no banco via Hibernate (coluna `am_usuario.dark_mode`).
- Novo endpoint `POST /rest/usuario/darkmode` que salva a preferência de dark mode do usuário autenticado.
- Atualização do endpoint `POST /rest/login` para retornar o campo `darkMode` na resposta.
- Novo CSS de dark mode adicionado às páginas JSP via folha de estilos dedicada.
- Toggle de dark mode na UI (ícone/botão no cabeçalho) que chama o endpoint e aplica/remove o tema imediatamente.
- Ao carregar qualquer página autenticada, o frontend lê `darkMode` do usuário logado e aplica o tema.

## Capabilities

### New Capabilities

- `dark-mode`: Preferência visual de dark mode por usuário — ativação/desativação via UI com persistência no banco de dados.

### Modified Capabilities

- `gestao-usuarios`: Entidade `Usuario` passa a ter o campo `darkMode`; endpoint de login retorna essa preferência na resposta.

## Impact

- **Entidade JPA**: `br.com.am.entidades.Usuario` — novo campo `darkMode`.
- **REST**: Novo recurso `UsuarioRestApp` ou método adicional em `UsuarioApp` para salvar preferência.
- **Frontend (JSP/JS)**: Todas as páginas autenticadas precisam verificar e aplicar o tema; toggle no cabeçalho compartilhado.
- **CSS**: Novo arquivo de estilos de dark mode sobrescrevendo variáveis Bootstrap 3.
- **Banco**: Coluna `dark_mode` adicionada automaticamente via `hbm2ddl.auto=update`.
- Nenhuma dependência externa nova necessária.
