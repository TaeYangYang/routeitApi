import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
  kotlin("plugin.jpa") version "1.9.25"
  kotlin("kapt") version "1.7.10"
  id("org.springframework.boot") version "3.4.4"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "com.routeit"
version = "0.0.1-SNAPSHOT"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web:3.4.4")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.hibernate:hibernate-core:6.6.11.Final")
  implementation("org.springframework.boot:spring-boot-starter-logging")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
  implementation("org.springframework.boot:spring-boot-starter-data-redis:3.4.4")
  implementation("io.github.oshai:kotlin-logging:7.0.5")
  implementation("com.googlecode.json-simple:json-simple:1.1.1")

  // DTO, Entity Mapper
  implementation("org.mapstruct:mapstruct:1.6.3")
  kapt("org.mapstruct:mapstruct-processor:1.6.3")

  // JWT
  implementation("io.jsonwebtoken:jjwt-api:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
  runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

  runtimeOnly("com.h2database:h2")
  developmentOnly("org.springframework.boot:spring-boot-devtools")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
  testImplementation("org.springframework.security:spring-security-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

allOpen {
  annotation("javax.persistence.Entity")
}

noArg {
  annotation("jakarta.persistence.Entity")
  annotation("jakarta.persistence.MappedSuperclass")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll("-Xjsr305=strict")
  }
}

tasks.named<Test>("test") {
  useJUnitPlatform()
  ignoreFailures = true
  // 테스트를 제외하려면 exclude를 여기에 설정
  //exclude("**/*")  // 이 줄을 테스트에만 적용되도록 변경
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "17"
}

tasks.withType<BootJar> {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}