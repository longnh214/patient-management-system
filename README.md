# 🏢 환자 방문 관리 시스템

## 프로젝트 개요 및 기술 스택/버전

언어: Java 17
프레임워크: Spring Boot 3.5.4
데이터베이스: H2 Database
DB + 엔티티 연동: Spring Data JPA + Openfeign queryDsl
API 문서화: spring-restdocs
테스트: JUnit 5 (단위/통합 테스트)
빌드 도구: Gradle

## 실행 전 application.yml 설정

### 경로

```text
└── src
    └── main
        └── resources
            └── application.yml
```

### application.yml 내용

```yaml
spring:
  main:
    allow-bean-definition-overriding: true
  datasource:
    url: jdbc:h2:tcp://localhost/~/test
    username: sa
    password:
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      generate_statistics: true
      show_sql: true
      format_sql: true
  batch:
    jdbc:
      initialize-schema: always

logging.level:
  org.hibernate.SQL: debug
```
`application.yml`파일을 해당 경로에 생성 후
H2 접속 host와 port를 정확히 기입하고 실행하시면 정상 작동합니다.

## spring-restdocs 기반 API 문서 조회

1. `gradle`의 `copyDocument` 명령어를 통해 controller 테스트 코드 기반 `adoc` 파일을 프로젝트 static 폴더에 복사합니다.

```shell
./gradlew copyDocument
```

2. Application 실행 후 `http://localhost:8080/docs/index.html` url에서 API 문서를 볼 수 있습니다.