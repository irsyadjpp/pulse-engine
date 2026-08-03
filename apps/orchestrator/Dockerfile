FROM eclipse-temurin:17-jre

# curl required by docker-compose healthcheck
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY apps/orchestrator/target/orchestrator.jar app.jar

EXPOSE 7021

# Profile is injected at runtime via SPRING_PROFILES_ACTIVE (e.g. prod in docker-compose)
ENTRYPOINT ["java", "-jar", "app.jar"]