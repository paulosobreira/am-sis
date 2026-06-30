# am-sis — Arquivo Morto

Sistema de gestão de arquivo morto: cadastro, pesquisa e geração de relatórios de arquivamentos físicos de documentos. Desenvolvido em Java com Tomcat embutido, banco H2 persistente e frontend JSP/Bootstrap.

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Backend | Java 21, JAX-RS (Jersey 4.x), Hibernate 7 |
| Banco | H2 (fat JAR) / MySQL 8 (Docker) |
| Relatórios | Thymeleaf (HTML, imprimível via browser) |
| Frontend | JSP, Bootstrap 3, jQuery |
| Servidor | Tomcat 11 embutido |
| Build/Deploy | Maven (fat JAR), Docker |

---

## Build e execução

### Local (fat JAR)

```bash
mvn clean package
java -jar target/am-sis.jar
```

Acesse: `http://localhost:8080/am-sis`  
Banco H2 criado automaticamente em `./data/am-sis.mv.db`  
Logs em `./logs/am-sis.log`

### Docker (MySQL)

```bash
# Build da imagem e sobe ambiente completo
./build.sh

# Ou passo a passo:
docker build -f am-sis.dockerfile . -t sowbreira/am-sis
docker compose up
```

Acesse: `http://localhost/am-sis`  
Dados do H2 persistidos em `./data/`.

---

## Login padrão

| Usuário | Senha | Papel |
|---|---|---|
| `admin` | `am-sis` | Administrador — acesso total |
| `guest` | `guest` | Visitante — somente leitura (sem exclusões) |

Usuários adicionais são cadastrados pelo administrador no módulo de gestão de usuários.

---

## Papéis de usuário

O sistema possui três papéis com permissões distintas:

- **Administrador** — credenciais configuradas em `config.properties`. Token único por sessão, armazenado em memória. Acesso total a todas as operações.
- **Visitante** — login `guest` / senha `guest`. Token com prefixo `guest-`. Pode consultar e criar registros, mas **não pode excluir**.
- **Usuário cadastrado** — autenticado via banco de dados (`am_usuario`, campo `ativo = true`). Mesmas permissões do administrador, exceto configurações do sistema.

Todos os endpoints (exceto `POST /rest/login` e `GET /rest/binario/downloadImg`) exigem o header `token` com valor válido.

---

## Capacidades funcionais

### Autenticação — `/rest/login`

| Endpoint | Descrição |
|---|---|
| `POST /rest/login` | Autentica e retorna objeto usuário com token de sessão |

**Regras:**
- Senha comparada como hash MD5
- Usuários inativos (`ativo = false`) não recebem token
- Token inválido ou ausente em endpoints protegidos retorna HTTP 401

---

### Arquivamento — `/rest/arquivamento`

Cadastro de documentos/caixas arquivadas.

| Endpoint | Descrição |
|---|---|
| `POST /rest/arquivamento` | Cria (sem `id`) ou atualiza (com `id`) um arquivamento |
| `DELETE /rest/arquivamento` | Exclusão lógica (`apagado = true`) — visitantes não podem |

**Campos obrigatórios:** `codigo`, `descricao`, `empresa`, `tipoArquivamento`

**Controle de versão otimista:** a cada atualização, o campo `versao` é incrementado. Se a versão enviada for menor que a versão atual no banco (edição concorrente), a operação é rejeitada com HTTP 400 informando o login que fez a última alteração.

**Soft-delete:** registros excluídos permanecem no banco com `apagado = true` e ficam invisíveis nas pesquisas.

---

### Pesquisa de arquivamentos — `/rest/pesquisaArquivamento`

| Endpoint | Descrição |
|---|---|
| `POST /rest/pesquisaArquivamento` | Retorna arquivamentos filtrados, ordenados por descrição |

**Filtros disponíveis** (todos opcionais, combinam entre si):

| Campo | Tipo de busca |
|---|---|
| `id` | Exato |
| `tipoArquivamento` | Exato |
| `codigo` | Contém (case-insensitive) |
| `descricao` | Contém (case-insensitive) |
| `dataReferencia` | Exato (mês/ano) |
| `dataExpurgoStrINI` + `dataExpurgoStrFIM` | Intervalo de data de expurgo |

Registros com `apagado = true` nunca aparecem nos resultados.

---

### Relatório de arquivamento — `/rest/relatorioArquivamento`

Gera relatório HTML imprimível com logo da empresa, código, descrição, observação e data de referência (duas cópias por página).

| Endpoint | Descrição |
|---|---|
| `POST /rest/relatorioArquivamento/gerar` | Gera relatório de uma lista de arquivamentos; retorna chave de acesso (timestamp) |
| `GET /rest/relatorioArquivamento/imprimir/{chave}` | Retorna o HTML gerado (disponível por 60 segundos) |
| `GET /rest/relatorioArquivamento/gerarHtml/{id}` | Gera e retorna HTML diretamente para um arquivamento por ID |
| `GET /rest/relatorioArquivamento/gerarPdf/{id}` | Alias de `gerarHtml` — retorna HTML imprimível (use Ctrl+P do browser para gerar PDF) |

---

### Gestão de empresas — `/rest/empresa`

| Endpoint | Descrição |
|---|---|
| `GET /rest/empresa` | Lista todas as empresas ordenadas por nome |
| `POST /rest/empresa` | Cria ou atualiza empresa (`nome` obrigatório) |
| `DELETE /rest/empresa` | Remove empresa — visitantes não podem |

---

### Gestão de usuários — `/rest/usuario`

| Endpoint | Descrição |
|---|---|
| `GET /rest/usuario` | Lista todos os usuários (ativos e inativos) por nome |
| `POST /rest/usuario` | Cria (`nome` e `login` obrigatórios) ou atualiza usuário |
| `DELETE /rest/usuario` | Desativa usuário (`ativo = false`) — visitantes não podem |

**Senha gerada automaticamente:** na criação e atualização, o sistema gera uma senha aleatória, armazena como MD5 e retorna a senha em texto claro no campo `senhaStr` da resposta.

---

### Gestão de tipos — `/rest/tipoArquivamento` e `/rest/tipoExpurgo`

| Endpoint | Descrição |
|---|---|
| `GET /rest/tipoArquivamento` | Lista tipos de arquivamento por descrição |
| `POST /rest/tipoArquivamento` | Cria ou atualiza tipo de arquivamento (`descricao` obrigatório e único) |
| `DELETE /rest/tipoArquivamento` | Remove tipo — visitantes não podem |
| `GET /rest/tipoExpurgo` | Lista tipos de expurgo |
| `POST /rest/tipoExpurgo` | Cria ou atualiza tipo de expurgo |
| `DELETE /rest/tipoExpurgo` | Remove tipo de expurgo — visitantes não podem |

---

### Binários (imagens) — `/rest/binario`

| Endpoint | Descrição |
|---|---|
| `POST /rest/binario/upload` | Upload de imagem (`multipart/form-data`); requer token |
| `GET /rest/binario/downloadImg?id={id}` | Download de imagem por ID; não requer autenticação |

**Redimensionamento automático:** imagens com área superior a 160.000 pixels (ex: 400×400) são redimensionadas para 50% com interpolação bicúbica antes de serem armazenadas.

---

## Configuração

`src/main/resources/config.properties`:

| Chave | Descrição |
|---|---|
| `admin` | Login do administrador |
| `pass` | Senha do admin em MD5 (gere com `Util.md5("senha")` em `Recursos.java`) |
| `versao` | Incrementado automaticamente pelo Maven a cada build |
