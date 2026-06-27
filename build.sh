#!/bin/bash
docker compose down
mvn clean package
docker build -f am-sis.dockerfile . -t sowbreira/am-sis
docker compose up
