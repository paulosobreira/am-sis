## Context

O projeto `am-sis` é uma aplicação Java EE (WAR) para gestão de arquivo morto (Arquivo Morto). Tecnologias em uso: Java 11, JAX-RS (Jersey 2.27), Hibernate 5.6 (ORM), MySQL, BIRT 4.5 para relatórios e Bootstrap no frontend. O código existe e funciona, mas nunca teve documentação formal de requisitos. Esta mudança é puramente documental.

## Goals / Non-Goals

**Goals:**
- Criar specs legíveis e testáveis para cada capacidade funcional identificada no código
- Estabelecer a base para que futuras mudanças possam referenciar e modificar requisitos existentes
- Documentar comportamentos não-óbvios (controle de versão otimista, soft-delete, papéis de usuário, cache de relatório)

**Non-Goals:**
- Alterar qualquer código de produção
- Documentar detalhes de infraestrutura (deploy, Docker, banco de dados)
- Criar documentação de API estilo Swagger/OpenAPI — o foco são requisitos de comportamento
- Cobrir código utilitário interno (`Util`, `Dia`, `FormatDate`, etc.)

## Decisions

**Granularidade das specs**: Uma spec por endpoint/recurso REST (`ArquivamentoApp`, `BinarioApp`, etc.). Alternativa seria agrupar por domínio, mas a correspondência 1:1 com as classes facilita manutenção futura.

**Linguagem das specs**: Português, alinhado ao idioma do código e da equipe.

**Cobertura**: Documentar apenas comportamentos observáveis na API REST, não implementações internas do Hibernate ou BIRT. Regras de negócio implícitas no código (ex: soft-delete, versão otimista) são explicitadas como requisitos.

**Escopo do papel de usuário**: O sistema tem 3 papéis distintos inferidos do código — Administrador (token estático), Visitante (prefixo `guest-`) e Usuário comum (token de banco). Esses papéis são documentados na spec de autenticação e referenciados nas demais.

## Risks / Trade-offs

[Especificações baseadas em código podem omitir intenções originais] → As specs devem ser revisadas por alguém com conhecimento do domínio antes de serem usadas como base para mudanças críticas.

[Código usa Hibernate Criteria API legada (depreciada)] → As specs documentam o comportamento externo, não a implementação; a dívida técnica do ORM é um risco separado.

[Cache de relatório em memória estática] → Documentado na spec de relatório como limitação conhecida (não sobrevive a restart da aplicação).
