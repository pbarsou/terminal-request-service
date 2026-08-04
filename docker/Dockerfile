FROM azul/zulu-openjdk-alpine:21-jre-headless-latest AS build

WORKDIR /build
ARG PROJECT_JAR_PATH
COPY ${PROJECT_JAR_PATH} app.jar

RUN mkdir -p extracted && java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM azul/zulu-openjdk-alpine:21
WORKDIR /app

COPY target/dd-java-agent.jar /app/dd-java-agent.jar

COPY --from=build /build/extracted/dependencies/lib/ /app/lib/
COPY --from=build /build/extracted/application/ /app/application/

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} \
  -javaagent:/app/dd-java-agent.jar \
  -Ddd.service=${DD_SERVICE:-terminal-request-service} \
  -Ddd.env=${DD_ENV:-local} \
  -Ddd.version=${DD_VERSION:-1.0.0} \
  -cp \"/app/application/*:/app/lib/*\" \
  com.desafio.terminalrequest.TerminalRequestApplication"]