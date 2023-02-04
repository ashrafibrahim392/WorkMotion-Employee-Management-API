## Employee management API (demo) 
A spring boot application to manage the life cycle of the employee and make sure that the process sequence is taken respectively without any violatio. 

## Note : 
This is a very simple application to prove the ability to develop and organize applications.
It's need some improvement to be better.

## Tools and technologies 
	1. java 11
	2. spring boot 2.7.8
	3. spring state machine 
	4. h2 database 

## How to start application ?
    For simplicity I used the local registry to build my docker image instead of pushing it to remote one.  

	- Pre prerequisite
		1. docker installed on your machine  
		
    - Steps	
		1. windows : 
			a. Double click on start.bat
			b. After all thing is done just open your browser and navigate to http://localhost:9999/swagger-ui/index.html#/ that will take you to openApi documentations (swagger).
		2. linux : 
            a. Run file start.sh 
            b. After all thing is done just open your browser and navigate to http://localhost:9999/swagger-ui/index.html#/ that will take you to openApi documentations (swagger).

## Recomendation 
	 It's recommended to add security layer to the web service for authentication and authorizetion using any third party gateway Like AWS API gateway.  
	 It's recommended to run this service with any orchestration tool (Kubernetes , EKS , .... ) to have our service scalability and availability 
	 It's recommended to save the sensitive data like database password to some secure storage (Kubernetes secrets , vault server ,....etc. ) 

## Integration proposal
	1. Create offline engine to calculate statistics (please check diagram attached) 
		 a. Create offline engine to get data form employee database and calculate statistics on the employee data and save it on the statistics warehouse. 
		 b. This engine will start periodically using cron job or kubernates jobs (daily, hourly, .... etc.) 
		 c. Create endpoint (may be separate microservice) to provide the aggregated data to the statistics system
	
	2. Make the integration by streams: 
		a. Employee management API send any updates (add new employee, states change, ... etc. )  on any employee to the stream 
		b. statistics system listen to the stream and update own database with the new update (add new employee, states change, ... etc. )
	  this is not recommended because of data redundancy. 
	3. Create Rest API to be integration point between the statistics system and employee management API (not recommended)
	
	Note : This proposal is based on the data provided by workmotion may be changed according more clarification and business needs.


