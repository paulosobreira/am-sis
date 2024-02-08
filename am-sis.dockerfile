FROM tomcat:9.0.82-jdk11
MAINTAINER Paulo Sobreira
WORKDIR /usr/local/tomcat/webapps
RUN  rm -rf *
ADD target/am-sis.war /usr/local/tomcat/webapps/am-sis.war
EXPOSE 8080