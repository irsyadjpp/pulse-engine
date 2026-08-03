FROM eclipse-temurin:17-jre

# curl required by docker-compose healthcheck
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY apps/orchestrator/target/orchestrator-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 7021

# Profile is injected at runtime via QUARKUS_PROFILE (e.g. prod in docker-compose)
ENV QUARKUS_HTTP_HOST=0.0.0.0
ENTRYPOINT ["java", "-jar", "app.jar"]
