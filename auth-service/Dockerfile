# 빌드 스테이지
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# 필요한 패키지 설치
RUN apk add --no-cache bash dos2unix

# Gradle 파일 복사 및 줄 바꿈 문자 변환
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
RUN dos2unix gradlew

# gradlew에 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사 및 빌드
COPY src ./src
ARG SERVICE_NAME
RUN ./gradlew build -PserviceName=${SERVICE_NAME} --no-daemon

# 실행 스테이지 (변경 없음)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
ARG SERVICE_NAME
ENV SERVICE_NAME=${SERVICE_NAME}
COPY --from=build /app/build/libs/${SERVICE_NAME}-*.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]