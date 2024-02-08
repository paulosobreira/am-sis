# am-sis 

Exemplo de implementação basica de um sistema para arquivo morto.

Lista de tecnologias usadas:
1. Java
2. JS
3. Bootstrap
4. Hibernate
5. REST
6. MySql
7. Maven
   
## Como testar

>No repositório Dockerhub:

1.[sowbreira/am-sis](https://cloud.docker.com/u/sowbreira/repository/docker/sowbreira/am-sis)

## Construção Maven e Docker

- mvn clean package
- mvn war:war
- docker build -f am-sis.dockerfile . -t sowbreira/am-sis
- docker push sowbreira/am-sis

## Como testar no Play with Docker

Pode ser executado no [Play with Docker](https://labs.play-with-docker.com/)

>Baixar o aqruivo do docker compose
```
curl -LfO 'https://raw.githubusercontent.com/paulosobreira/am-sis/master/docker-compose.yaml'
```

>Iniciar containers do Mysql,PhpMyAdmin e Am-sis
```
docker compose up
```

>Url de acesso:

link_gerado_playwithdocker/**am-sis/login.jsp**

Login : admin 
Senha : am-sis
