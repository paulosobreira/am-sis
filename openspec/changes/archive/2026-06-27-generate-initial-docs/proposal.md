## Why

O projeto `am-sis` (Arquivo Morto) não possui documentação de requisitos ou especificações formais, dificultando a manutenção, integração de novos desenvolvedores e evolução segura do sistema. A engenharia reversa do código existente é o ponto de partida para criar essa base documental.

## What Changes

- Criação de specs documentando os requisitos de cada capacidade identificada no código-fonte
- Criação de proposta, design e tarefas estruturadas para guiar a produção dos documentos
- Nenhuma alteração de código de produção — mudança é exclusivamente documental

## Capabilities

### New Capabilities

- `autenticacao`: Autenticação de usuários via login/senha com geração de token de sessão, incluindo suporte a usuário administrador, visitante (guest) e usuários cadastrados no banco
- `arquivamento`: CRUD do registro de arquivamento (documento/caixa arquivada), incluindo controle de versão otimista, soft-delete e vínculo com empresa, tipo de arquivamento e tipo de expurgo
- `pesquisa-arquivamento`: Busca de arquivamentos com filtros por código, descrição, tipo, data de referência e intervalo de data de expurgo
- `relatorio-arquivamento`: Geração de relatório PDF de arquivamentos usando BIRT, com caching em memória do relatório gerado por sessão
- `gestao-empresas`: CRUD de empresas com controle de acesso (visitantes não podem excluir)
- `gestao-usuarios`: CRUD de usuários com geração automática de senha aleatória e hash MD5
- `gestao-tipos`: CRUD de tipos de arquivamento (`TipoArquivamento`) e tipos de expurgo (`TipoExpurgo`)
- `binarios`: Upload e download de imagens/arquivos binários com redimensionamento automático de imagens grandes

### Modified Capabilities

## Impact

- Afeta apenas arquivos de documentação em `openspec/`
- Nenhum código Java, JSP ou configuração de build é modificado
- As specs geradas servirão de base para futuras mudanças no sistema
