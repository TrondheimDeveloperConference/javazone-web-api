#!/bin/bash
cd ..
mvn install -Dmaven.test.skip
mkdir -p docker/api
unzip target/javazone-web-api-1.0-TDC-appmgr.zip -d docker/api
docker build -t trondheimdc/api docker
rm -rf docker/api
docker push trondheimdc/api