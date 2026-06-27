# am-sis

Sistema de arquivo morto com Tomcat embutido e banco H2 persistente.

## Tecnologias

- Java 11, JAX-RS (Jersey 2.x), Hibernate 6, H2, Tomcat 9 embutido
- Bootstrap, jQuery, BIRT 4.5 (relatórios)
- Maven (fat JAR), Docker

## Build

```bash
mvn clean package
```

Gera `target/am-sis.jar` com todas as dependências e Tomcat embutido.

## Execução local

```bash
java -jar target/am-sis.jar
```

A aplicação inicia em `http://localhost:8080/am-sis`. O banco H2 é criado automaticamente em `./data/am-sis.mv.db`.

Login padrão: **admin** / **am-sis**

## Docker

```bash
# Build da imagem
docker build -f am-sis.dockerfile . -t sowbreira/am-sis

# Iniciar
docker compose up
```

A aplicação fica disponível em `http://localhost/am-sis`. Os dados do H2 são persistidos em `./data/`.

## Script completo

```bash
./build.sh
```

Executa: `docker compose down` → `mvn clean package` → `docker build` → `docker compose up`.
