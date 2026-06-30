## Context

A aplicação am-sis é um WAR Java EE rodando em Tomcat 9 + JDK 11, sem Spring. A camada REST usa JAX-RS (Jersey 2.27) e a persistência é feita via Hibernate 5.6/JPA. O frontend usa JSPs com Bootstrap 3 + jQuery; toda interação de dados é via AJAX para `/rest/*`. A autenticação é baseada em token salvo na entidade `Usuario` no banco.

Atualmente, `Usuario` não possui nenhum campo de preferência de UI. O token é retornado no login e salvo no `sessionStorage` pelo frontend. Não há estado de sessão no servidor.

## Goals / Non-Goals

**Goals:**
- Adicionar campo `darkMode` (Boolean) na entidade JPA `Usuario`, persistido como coluna `dark_mode` na tabela `am_usuario`.
- Expor endpoint `POST /rest/usuario/darkmode` para salvar a preferência do usuário autenticado.
- Retornar o campo `darkMode` no response do login (`POST /rest/login`) para que o frontend aplique o tema ao carregar.
- Aplicar CSS de dark mode automaticamente ao carregar qualquer página autenticada.
- Adicionar toggle de dark mode no cabeçalho do `index.jsp` e nas páginas internas.

**Non-Goals:**
- Dark mode no `login.jsp` ou `conf.jsp`.
- Temas além de claro/escuro.
- Preferência por URL param ou cookie (a persistência é exclusivamente no banco via usuário autenticado).

## Decisions

### D1 — Campo booleano nullable na entidade

`darkMode` será `Boolean` (objeto, não primitivo) com `nullable = true` e default `false` na lógica de negócio. Isso evita NOT NULL constraint em colunas já existentes e permite tratar `null` como `false` no frontend.

**Alternativa**: campo primitivo `boolean` com `columnDefinition = "TINYINT(1) DEFAULT 0"`. Descartada porque exigiria migração manual em bancos existentes; com `hbm2ddl.auto=update` a coluna nullable é adicionada sem problema.

### D2 — Endpoint dedicado `POST /rest/usuario/darkmode`

Em vez de reutilizar `POST /rest/usuario` (que regera senha a cada chamada), cria-se um endpoint dedicado que atualiza apenas o campo `darkMode` do usuário autenticado, identificado pelo token no header. Isso evita efeito colateral de regerar senha ao salvar preferência de tema.

**Alternativa**: adicionar `darkMode` ao payload de `POST /rest/usuario`. Descartada porque regerar senha a cada troca de tema é impraticável.

### D3 — CSS de dark mode via arquivo separado + classe `dark` no `<body>`

O dark mode é aplicado adicionando a classe `dark` no `<body>` via JavaScript. Um arquivo `css/dark-mode.css` define overrides de variáveis Bootstrap 3 e seletores com `.dark`. Isso mantém o CSS padrão intacto e permite toggle limpo via `classList.toggle`.

**Alternativa**: usar CSS custom properties (variáveis CSS). Compatível com Bootstrap 3 apenas parcialmente; a abordagem de classe é mais robusta com o stack atual.

### D4 — Estado de dark mode no `sessionStorage`

Ao fazer login, a resposta já inclui `darkMode`. O JS armazena esse valor em `sessionStorage` junto com token e nome. Cada página lê `sessionStorage` ao carregar e aplica a classe. Quando o toggle é acionado, chama o endpoint e atualiza `sessionStorage`.

**Alternativa**: re-consultar o backend a cada carregamento de página. Descartada por adicionar latência desnecessária.

## Risks / Trade-offs

- **Bootstrap 3 + dark mode**: Bootstrap 3 não tem suporte nativo a dark mode. Os overrides de CSS serão manuais e podem não cobrir todos os componentes imediatamente. → Mitigação: cobrir os elementos principais usados nas páginas existentes (navbar, well, list-group, table, input, btn).
- **`hbm2ddl.auto=update` + coluna nova**: A coluna `dark_mode` será adicionada automaticamente no boot. Registros existentes terão `null`, tratado como `false`. → Sem risco de perda de dados.
- **Token guest**: Usuários visitantes (`guest-*`) não possuem registro no banco, portanto o endpoint `darkmode` deve retornar 403 para eles. → Mitigação: verificar `getVisitante()` antes de salvar.

## Migration Plan

1. Deploy do WAR: Hibernate adiciona coluna `dark_mode` automaticamente.
2. Usuários existentes terão `darkMode = null` (equivalente a `false` — tema claro).
3. Nenhum script de migração necessário.
4. Rollback: remover coluna `dark_mode` manualmente se necessário (sem impacto funcional, pois o campo é nullable e tratado com default).

## Open Questions

- Nenhuma questão em aberto.
