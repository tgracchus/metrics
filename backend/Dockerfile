FROM openjdk:11-jdk as builder
WORKDIR /app
COPY . .
RUN ./gradlew clean bootDistZip
RUN unzip build/distributions/backend-boot-0.0.1.zip -d /app

FROM openjdk:11-jdk as backend

COPY --from=builder /app/backend-boot-0.0.1 /app/backend-boot-0.0.1
COPY --from=builder /app/entrypoint.sh /app/entrypoint.sh
COPY --from=builder /app/wait /app/wait

ENV KAFKA_URL="kafka:29092"
ENV POSTGRES_HOST="timescaledb"


CMD ["./entrypoint.sh"]
