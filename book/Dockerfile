FROM openjdk:18-oracle
#VOLUME /tmp
RUN mkdir /app && \
    apk add tcpdump
    
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/book.jar

WORKDIR /app 

CMD ["java", "-jar", "book.jar"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]