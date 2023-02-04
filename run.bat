docker build -t ashraf/workmostiontask .

set  db_pass=password
set  db_url=jdbc:h2:mem:mydb
set   db_user=sa

docker run -d  --name employee-management-api -p %1:8080   -e db_pass -e db_url -e db_user   ashraf/workmostiontask 



pause