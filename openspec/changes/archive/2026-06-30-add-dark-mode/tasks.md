## 1. Backend — Entidade e Persistência

- [x] 1.1 Adicionar campo `private Boolean darkMode;` em `br.com.am.entidades.Usuario` com `@Column(name = "dark_mode")` e getter/setter correspondentes
- [x] 1.2 Verificar que o Hibernate gera a coluna `dark_mode` (nullable) na tabela `am_usuario` ao subir a aplicação

## 2. Backend — Endpoint de Preferência

- [x] 2.1 Criar método `@POST @Path("/darkmode")` em `UsuarioApp` que recebe `Usuario` (com campo `darkMode`), identifica o usuário pelo token no header, rejeita visitantes com 403, atualiza apenas `darkMode` no banco e retorna HTTP 200 com o objeto atualizado
- [x] 2.2 Garantir que o endpoint retorna HTTP 401 para token inválido/ausente e HTTP 403 para usuário visitante

## 3. Backend — Login retorna darkMode

- [x] 3.1 Verificar que o endpoint de login (`POST /rest/login`) já retorna o campo `darkMode` na resposta (deve ocorrer automaticamente pela serialização JSON do `Usuario`; confirmar e ajustar se necessário)

## 4. Frontend — CSS de Dark Mode

- [x] 4.1 Criar arquivo `src/main/webapp/css/dark-mode.css` com overrides Bootstrap 3: `body.dark`, `.dark .navbar`, `.dark .well`, `.dark .list-group-item`, `.dark .panel`, `.dark table`, `.dark input`, `.dark select`, `.dark textarea`, `.dark .btn-default`, cores de fundo e texto para tema escuro
- [x] 4.2 Incluir `<link rel="stylesheet" href="css/dark-mode.css">` no `<head>` de `index.jsp` e de todas as páginas em `pages/**/*.jsp`
- [x] 4.3 Corrigir botões `btn-warning` (laranja) não aplicando dark mode — adicionar override com `background-image: none !important` para anular gradiente do `bootstrap-theme.min.css` em todos os tipos de botão (`btn-default`, `btn-warning`, `btn-danger`, `btn-primary`, `btn-success`, `btn[disabled]`)
- [x] 4.4 Corrigir `.well` ("Olá, fulano") não aplicando dark mode — adicionar `background-image: none !important` para anular gradiente do Bootstrap Theme
- [x] 4.5 Reescrever paleta do dark mode de tons azulados para tons de grafite (`#1e1e1e` fundo, `#2a2a2a` superfícies, `#3d3d3d` bordas, `#d4d4d4` texto)

## 5. Frontend — JavaScript de Dark Mode

- [x] 5.1 Em `am-sis.js`, adicionar lógica de inicialização: ao carregar página autenticada, ler `localStorage.getItem('darkMode')` e, se `"true"`, adicionar classe `dark` ao `document.body`
- [x] 5.2 Em `am-sis.js`, adicionar função `toggleDarkMode()` que: lê o estado atual, envia `POST /rest/usuario/darkmode` com o novo valor, atualiza `localStorage.darkMode` e aplica/remove classe `dark` no body; em caso de falha, reverte o estado e exibe alerta
- [x] 5.3 Garantir que `localStorage.darkMode` é salvo após login bem-sucedido (junto com token e nome do usuário)
- [x] 5.4 Em `login.jsp`, adicionar botão de toggle (ícone olho) no campo senha via `input-group` do Bootstrap 3, alternando entre `type="password"` e `type="text"` com ícone `glyphicon-eye-open/close`
- [x] 5.5 Em `login.js`, salvar login e senha no `localStorage` após login bem-sucedido e restaurar automaticamente nos campos ao carregar `login.jsp`

## 6. Frontend — Toggle na UI

- [x] 6.1 Adicionar botão/ícone de toggle de dark mode no cabeçalho de `index.jsp` (ao lado do botão "Sair"), chamando `toggleDarkMode()` ao clicar
- [x] 6.2 Adicionar o mesmo toggle no cabeçalho de cada página em `pages/**/*.jsp` que já possua barra de navegação ou área de cabeçalho

## 7. Validação

- [x] 7.1 Testar: login com usuário que tem `darkMode = null` → tema claro aplicado
- [x] 7.2 Testar: acionar toggle → dark mode ativado, preferência salva, persiste após recarregar página
- [x] 7.3 Testar: acionar toggle novamente → tema claro restaurado, preferência salva
- [x] 7.4 Testar: token visitante (`guest-*`) → endpoint `/darkmode` retorna 403
