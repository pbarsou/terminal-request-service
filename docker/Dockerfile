FROM azul/zulu-openjdk-alpine:25-jre-headless-latest AS build

ARG PROJECT_JAR_PATH

COPY ${PROJECT_JAR_PATH} .
RUN mkdir -p extracted && java -Djarmode=tools -jar terminal-request-service.jar extract --layers --destination extracted


FROM azul/zulu-openjdk-alpine:25

WORKDIR /app

COPY --from=build extracted/dependencies/ ./
COPY --from=build extracted/spring-boot-loader/ ./
COPY --from=build extracted/application/ ./

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/terminal-request-service.jar"]