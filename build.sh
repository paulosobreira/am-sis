#!/bin/bash
docker compose down
mvn clean package
mvn war:war
docker build -f am-sis.dockerfile . -t sowbreira/am-sis
docker compose up
