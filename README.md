## Employee management API (demo) 
A spring boot application to manage the life cycle of the employee and make sure that the process sequence is taken respectively without any violation. 

## Note : 
This is a very simple application to prove the ability to develop and organize applications.
It's need some improvement to be better.

## Tools and technologies 
	1. java 11
	2. spring boot 2.7.8
	3. spring state machine 
	4. h2 database 

## How to start application ?
For simplicity, I used the local registry to build my docker image instead of pushing it to remote one.  

- Pre prerequisite
    1. docker installed on your machine  
    
  - Steps

      * windows : 
      
        a. Double-click on start.bat

        b. After all thing is done just open your browser and
              navigate to http://localhost:9999/swagger-ui/index.html#/       
              that will take you to openApi documentations (swagger).
      * linux : 
    
        a. Run file start.sh
    
        b. After all thing is done just open your browser and 
               navigate to http://localhost:9999/swagger-ui/index.html#/ 
               that will take you to openApi documentations (swagger).
            

## Recommendation 
 1. Add security layer to the web service for authentication and authorizetion 
        using any third party gateway Like AWS API gateway.  
 2. Run this service with any orchestration tool (Kubernetes , EKS , .... ) to have our service 
        highly scalability and highly availability. 
 3. Save the sensitive data like database password to some secure 
        storage (Kubernetes secrets , vault server ,....etc. ) 

## Integration proposal

1. Create offline engine to calculate statistics  [Check diagram](Diagram.PNG)

     a. Create offline engine to get data form employee database
         b. Calculate statistics on the employee data and save it on the statistics warehouse. 
     c. This engine will start periodically using cron job or 
            kubernates jobs (daily, hourly, .... etc.) 
     d. Create endpoint (may be separate microservice) to provide the aggregated data 
            to the statistics system

2. Make the integration by streams: 
    a. Employee management API send any updates (add new employee, states change, ... etc. )  
            on any employee to the stream 
    b. Statistics system listen to the stream and update own database 
            with the new update (add new employee, states change, ... etc. )
  Note : This is not recommended because of data redundancy. 

3. Create Rest API to be integration point between the statistics system 
            and employee management API (not recommended)
   Note : This is not recommended because hage data transactions. 

Note : This proposal is based on the data provided by workmotion and may be changed 
            according to more clarification and business needs.


