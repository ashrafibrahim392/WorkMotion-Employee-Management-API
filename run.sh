#!/usr/bin/bash
docker build -t ashraf/workmostiontask -f ./Dockerfile .
docker run -d  --name employee-management-api -p $1:8080 --env-file=env-file    ashraf/workmostiontask



