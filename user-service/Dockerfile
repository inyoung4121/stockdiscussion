# 빌드 스테이지
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN ./gradlew dependencies --no-daemon

COPY src ./src
ARG SERVICE_NAME
RUN ./gradlew build -x test -PserviceName=${SERVICE_NAME} --no-daemon

# 실행 스테이지
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
ARG SERVICE_NAME
ENV SERVICE_NAME=${SERVICE_NAME}
COPY --from=build /app/build/libs/${SERVICE_NAME}-*.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]