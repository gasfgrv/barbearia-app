FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY ${JAR_FILE} app.jar
EXPOSE 8080
RUN apk add --no-cache wget
USER appuser
ENTRYPOINT ["java","-jar","/app.jar"]
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1
