FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# The cache mount keeps downloaded dependencies between builds. Maven doesn't retry failed TLS
# downloads itself, so retry the step; each attempt resumes from what is already cached.
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q dependency:go-offline || mvn -q dependency:go-offline || mvn -q dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q package -DskipTests || mvn -q package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
