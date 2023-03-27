FROM openjdk:17-jdk-slim

ARG CACHEBUST=1

COPY /build/libs/ch-manager-1.0.jar app.jar

ENTRYPOINT ["java", \
"-XX:MinRAMPercentage=50.0", \
"-XX:MaxRAMPercentage=75.0", \
"-XX:MaxHeapFreeRatio=75", \
"-jar", "/app.jar"]