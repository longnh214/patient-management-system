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