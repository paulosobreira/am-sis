# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build e Deploy

```bash
# Build completo + sobe ambiente Docker (fluxo padrão)
./build.sh

# Apenas gerar o WAR
mvn clean package

# Build da imagem Docker
docker build -f am-sis.dockerfile . -t sowbreira/am-sis

# Subir/derrubar containers
docker compose up
docker compose down
```

O Maven incrementa automaticamente o campo `versao` em `config.properties` a cada build (plugin `maven-antrun`).

## Arquitetura

Aplicação Java empacotada como fat JAR, rodando em **Tomcat 11 + JDK 21**. Não usa Spring — depende diretamente de **JAX-RS (Jersey 4.x)** para a API REST e **Hibernate 7** via JPA para persistência.

```
br.com.am
├── entidades/      JPA entities (tabelas am_*)
├── rest/           Recursos JAX-RS — um arquivo por endpoint
│   └── RestApp     Classe base: validaToken(), incluir(), atualizar(), remover()
├── servlet/        HibernateServlet (inicialização)
├── recursos/       Recursos.java — carrega config.properties em singleton
└── util/           HibernateUtil (SessionFactory singleton), utilitários de data/número
```

**Frontend:** JSPs com Bootstrap 3 + jQuery. Toda interação de dados vai via chamadas AJAX para a API REST em `/rest/*`.

## Banco de Dados

- MySQL 8 (`am-sis` schema). Credenciais em `src/main/resources/META-INF/persistence.xml`.
- `hibernate.hbm2ddl.auto=update` — o schema é criado/atualizado automaticamente no boot.
- No ambiente Docker, o banco sobe como serviço `db` (host `db`, senha root `am-sis`).
- Para desenvolvimento local, ajuste a URL JDBC em `persistence.xml`.

## Padrões da API REST

- Todos os endpoints exigem header `token` (exceto `POST /rest/login` e `GET /rest/binario/downloadImg`).
- Autenticação em `RestApp.validaToken()`: aceita token de admin (memória estática em `LoginApp.adminToken`), token `guest-*` (visitante sem permissão de DELETE) ou token de usuário ativo no banco.
- Endpoints de escrita usam `POST` tanto para criação (sem `id`) quanto para atualização (com `id`).
- `Arquivamento` usa controle de versão otimista — o campo `versao` deve ser enviado na atualização; conflito retorna HTTP 400.
- Soft-delete: `Arquivamento` usa campo `apagado=true`; `Usuario` usa `ativo=false`.

## Configuração

`src/main/resources/config.properties` — lido por `Recursos.java` em singleton:

| Chave | Descrição |
|-------|-----------|
| `admin` | Login do usuário administrador |
| `pass` | Senha do admin em MD5 |
| `versao` | Incrementado automaticamente pelo Maven a cada build |
| `versao` | Incrementado automaticamente pelo Maven a cada build |

Para gerar hash MD5 de uma senha: execute `main()` em `Recursos.java` chamando `Util.md5("senha")`.
