#!/usr/bin/bash
docker build -t ashraf/workmostiontask -f ./Dockerfile . 

export  db_pass=password
export  db_url=jdbc:h2:mem:mydb
export   db_user=sa

docker run -d  --name employee-management-api -p $1:8080   -e db_pass -e db_url -e db_user   ashraf/workmostiontask 



