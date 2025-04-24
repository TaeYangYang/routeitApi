# base image
FROM openjdk:17-jdk-slim AS builder

# 작업 디렉토리 생성
WORKDIR /app

# 전체 프로젝트 복사
COPY . .

# 테스트 제외 빌드
RUN ./gradlew clean build -x test

# 실제 앱 실행용 이미지
FROM openjdk:17-jdk-slim

# 작업 디렉토리 생성
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/routeitApi-0.0.1-SNAPSHOT.jar app.jar

# 8081 포트를 열어두어 컨테이너와 통신할 수 있도록 설정
EXPOSE 8081

# 컨테이너가 생성, 시작될 때 실행되는 명령어.
ENTRYPOINT ["java", "-jar", "app.jar"]