# binarios Specification

## Purpose
TBD - created by archiving change generate-initial-docs. Update Purpose after archive.
## Requirements
### Requirement: Upload de imagem com redimensionamento automático
O sistema SHALL aceitar upload de imagens via `POST /rest/binario/upload` com token válido e `Content-Type: multipart/form-data`. A imagem é lida como array de bytes e armazenada na tabela `am_binario`. Se a área da imagem (largura × altura) exceder 160.000 pixels, a imagem é redimensionada para 50% do tamanho original usando interpolação bicúbica antes de ser salva.

#### Scenario: Upload de imagem pequena (sem redimensionamento)
- **WHEN** cliente autenticado envia `POST /rest/binario/upload` com imagem cuja área é ≤ 160.000 pixels
- **THEN** o sistema armazena a imagem original e retorna HTTP 200 com o objeto `Binario` incluindo o `id` gerado

#### Scenario: Upload de imagem grande (com redimensionamento)
- **WHEN** cliente autenticado envia `POST /rest/binario/upload` com imagem cuja área é > 160.000 pixels
- **THEN** o sistema redimensiona a imagem para 50% do tamanho, armazena como JPEG e retorna HTTP 200 com o `id` gerado

#### Scenario: Upload sem token válido
- **WHEN** cliente envia `POST /rest/binario/upload` sem token ou com token inválido
- **THEN** o sistema retorna HTTP 401 com mensagem "Token inválido"

### Requirement: Download de imagem por ID
O sistema SHALL retornar o conteúdo binário de uma imagem armazenada via `GET /rest/binario/downloadImg?id={id}`. O endpoint não requer autenticação (usado internamente para incorporar imagens em relatórios BIRT). O conteúdo é retornado com `Content-Type: image/jpg`.

#### Scenario: Download bem-sucedido de imagem
- **WHEN** cliente acessa `GET /rest/binario/downloadImg?id={id}` com ID existente
- **THEN** o sistema retorna HTTP 200 com o conteúdo binário da imagem (`Content-Type: image/jpg`)

#### Scenario: Download com ID inexistente
- **WHEN** cliente acessa `GET /rest/binario/downloadImg?id={id}` com ID não encontrado
- **THEN** o sistema retorna HTTP 400 com mensagem "imágem não encontrada"

