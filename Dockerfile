From maven:3.8-eclipse-temurin-11-alpine As builder

WORKDIR '/app'
COPY . .
RUN  mvn clean package


From  adoptopenjdk/openjdk11:jdk-11.0.3_7 
COPY --from=builder /app/target/WorkmotionTask-0.0.1.jar /usr/src/WorkmotionTask.jar
EXPOSE 8080

# ENV db_pass=password
# ENV db_url=jdbc:h2:mem:mydb
# ENV db_user=sa


CMD ["java","-jar","/usr/src/WorkmotionTask.jar"]