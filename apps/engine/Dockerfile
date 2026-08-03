FROM eclipse-temurin:17-jre

# curl required by docker-compose healthcheck
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY apps/engine/target/quarkus-app quarkus-app

EXPOSE 7020

# Profile is injected at runtime via QUARKUS_PROFILE (e.g. prod in docker-compose)
ENV QUARKUS_HTTP_HOST=0.0.0.0
ENTRYPOINT ["java", "-jar", "quarkus-app/quarkus-run.jar"]