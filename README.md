# Spring Boot MVC с Thymeleaf

Този проект демонстрира базово Spring Boot MVC приложение, използващо Thymeleaf като слой за визуализация. Използва се вградена H2 база данни и са включени конфигурации за логване на Spring Security и уеб компонентите.

## Необходими изисквания

- **Java Development Kit (JDK) 8 или по-нова версия**
- **Maven** или **Gradle** (в зависимост от използвания инструмент за билдване)

## Настройка

**Сглобяване на проекта**

С помощта на Maven:
```
mvn clean install
```

**Стартиране на приложението**

С помощта на Maven:
```
mvn spring-boot:run
```

Или стартирайте генерирания `.jar` файл:
```
java -jar target/nastolni_igri-0.0.1-SNAPSHOT.jar
```

**Достъп до приложението**

Отворете браузъра и навигирайте до:

```
http://localhost:8080/
```

**Достъп до H2 конзолата**

Навигирайте до:

```
http://localhost:8080/h2-console
```

Използвайте следните данни за вход:

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Потребител**: `sa`
- **Парола**: `password`

## Application Properties

Конфигурацията в `src/main/resources/application.properties` е както следва:

```
spring.application.name=nastolni_igri

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG
```

## Зависимости

Уверете се, че проектът съдържа следните зависимости (например в `pom.xml`):

```xml
<dependencies>
    <!-- Spring Boot стартер за изграждане на уеб приложения -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!-- Поддръжка на Thymeleaf темплейт енджин -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <!-- Spring Data JPA за работа с базата данни -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <!-- H2 вградена база данни -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
    </dependency>
    <!-- По избор: Spring Security -->
    <!--
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    -->
</dependencies>
```

## Структура на проекта

Типичната структура на проекта за това Spring Boot MVC приложение изглежда така:

```
nastolni_igri/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── yourpackage/
│       │           ├── Application.java         // Основен клас на приложението
│       │           ├── controller/
│       │           │   └── HomeController.java  // Контролери
│       │           ├── model/                   // Домейн модели
│       │           ├── repository/              // Достъп до данни
│       │           └── service/                 // Бизнес логика
│       └── resources/
│           ├── static/       // Статични ресурси (CSS, JS, изображения)
│           ├── templates/    // Thymeleaf шаблони (например, index.html)
│           └── application.properties // Конфигурация на приложението
└── pom.xml или build.gradle
```

## Стартиране на тестове

За да стартирате тестовете, използвайте следната команда:

С помощта на Maven:
```
mvn test
```