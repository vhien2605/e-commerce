FROM openjdk:21-jdk
WORKDIR /spring
ARG FILE_JAR=target/*.jar
COPY ${FILE_JAR} excute-file.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java","-jar","excute-file.jar"]




