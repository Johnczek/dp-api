FROM maven:3.6.3-openjdk-11 AS MAVEN_BUILD

COPY pom.xml /build/
COPY .env /build/
COPY src /build/src/
COPY static /build/static

WORKDIR /build/
RUN mvn package -DskipTests

FROM openjdk:11.0.10-jre
COPY /static .
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/dp-api-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "dp-api-0.0.1-SNAPSHOT.jar"]