FROM maven:3-eclipse-temurin-17-alpine AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 mvn -f $HOME/pom.xml clean package -DskipTests -Pdev -Dmodernizer.skip=true

FROM eclipse-temurin:17-jre-alpine

# Instalar curl en la imagen final
RUN apk update && apk add curl

COPY --from=build /usr/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
