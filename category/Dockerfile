FROM openjdk:13-ea-27-jdk-alpine3.10
#VOLUME /tmp
RUN mkdir /app && \
    apk add tcpdump
    
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/category.jar

WORKDIR /app 

CMD ["java", "-jar", "category.jar"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]