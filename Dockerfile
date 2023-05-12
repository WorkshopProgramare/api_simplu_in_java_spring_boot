FROM openjdk:17-alpine
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw ./
COPY pom.xml ./
RUN apk add --no-cache dos2unix
RUN dos2unix ./mvnw
RUN chmod +x ./mvnw
RUN ./mvnw dependency:resolve

COPY src ./src
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]