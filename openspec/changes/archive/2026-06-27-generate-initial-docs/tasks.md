## 1. Revisar e validar specs geradas

- [ ] 1.1 Revisar `specs/autenticacao/spec.md` com alguém com conhecimento do domínio para confirmar comportamentos de login e papéis de usuário
- [ ] 1.2 Revisar `specs/arquivamento/spec.md` para confirmar regras de controle de versão otimista e soft-delete
- [ ] 1.3 Revisar `specs/pesquisa-arquivamento/spec.md` para confirmar filtros e comportamento de exclusão lógica na busca
- [ ] 1.4 Revisar `specs/relatorio-arquivamento/spec.md` para confirmar TTL do cache de relatórios e endpoints disponíveis
- [ ] 1.5 Revisar `specs/gestao-empresas/spec.md` para confirmar campos obrigatórios e regras de exclusão
- [ ] 1.6 Revisar `specs/gestao-usuarios/spec.md` para confirmar geração de senha e regra de soft-delete
- [ ] 1.7 Revisar `specs/gestao-tipos/spec.md` para confirmar unicidade de `descricao` em TipoArquivamento e TipoExpurgo
- [ ] 1.8 Revisar `specs/binarios/spec.md` para confirmar limiar de redimensionamento (160.000 pixels) e formatos aceitos

## 2. Publicar specs no diretório principal

- [x] 2.1 Executar `openspec sync` (ou `/opsx:sync`) para mover as specs da mudança para `openspec/specs/`
- [x] 2.2 Verificar que cada spec está em `openspec/specs/<capability>/spec.md`

## 3. Arquivar a mudança

- [x] 3.1 Executar `/opsx:archive` para arquivar a mudança `generate-initial-docs` e marcar como concluída
