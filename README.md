# am-sis 

Exemplo de implementação basica de um sistema para arquivo morto.

Lista de tecnologias usadas:
1. Java
2. JS
3. Hibernate
4. REST
5. MySql
6. Maven
   
## Como testar

Dockerhub:

1.[sowbreira/am-sis](https://cloud.docker.com/u/sowbreira/repository/docker/sowbreira/am-sis)

2.[sowbreira/mysql](https://cloud.docker.com/u/sowbreira/repository/docker/sowbreira/mysql)

Pode ser executado no [Play with Docker](https://labs.play-with-docker.com/)


```Docker
docker run --name mysql -d sowbreira/mysql
docker run --name tomcat -d -p 8080:8080 --link mysql:db  sowbreira/am-sis
```
