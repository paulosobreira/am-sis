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

2.[sowbreira/mysql](https://cloud.docker.com/u/sowbreira/repository/docker/sowbreira/mysql)

>Pode ser executado no [Play with Docker](https://labs.play-with-docker.com/)


```Docker
docker run --name mysql -d sowbreira/mysql
docker run --name tomcat -d -p 8080:8080 --link mysql:db  sowbreira/am-sis
```
ou 
```Docker-Compose
Utilizar o vi para criar o docker-compose.yml
Subir os containers com docker-compose up
```

>Url de acesso:

link_gerado_playwithdocker/**am-sis/login.jsp**

