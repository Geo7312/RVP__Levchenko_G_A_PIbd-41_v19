# Лабораторные работы 1–2. Библиотека, отчёты и шлюз

**Вариант 19.** Учёт книг и читателей в библиотеке: выдать книгу на руки,
выдать для чтения в читальном зале, принять возвращённую книгу.

Стек: **Spring Boot 3 / Java 17, PostgreSQL, Liquibase, springdoc-openapi
(Swagger UI), Docker / docker-compose**. В базе данных ровно одна таблица —
`books`, где статус книги (`IN_LIBRARY` / `ON_HANDS` / `IN_READING_ROOM`) и
поле `reader_name` покрывают весь учёт по варианту.

## Структура проекта

```
RVP__Levchenko_G_A_PIbd-41_v19/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── report-service/                    — сервис отчётов (отдельный Maven-проект)
├── gateway/                           — Spring Cloud Gateway (отдельный Maven-проект)
├── postman/                           — коллекция запросов для ЛР2
└── src/main/
    ├── java/ru/ulstu/library/
    │   ├── LibraryApplication.java
    │   ├── controller/BookController.java   — REST API + Swagger-аннотации
    │   ├── service/BookService.java          — бизнес-логика
    │   ├── repository/BookRepository.java    — Spring Data JPA
    │   ├── entity/Book.java, BookStatus.java
    │   ├── dto/                              — DTO для запросов/ответов
    │   └── exception/                        — обработка ошибок (404/409/400)
    └── resources/
        ├── application.yml                   — общий конфиг (профиль по умолчанию: local)
        ├── application-local.yml             — БД на localhost
        ├── application-docker.yml            — БД по имени сервиса `db` в docker-сети
        └── db/changelog/                     — миграции Liquibase (таблица books и разделение имени автора)
```

## Вариант 1. Локальный запуск (БД в контейнере, приложение — на хосте)

1. Поднять только базу данных:

   ```bash
   docker compose up -d db
   ```

2. Собрать и запустить приложение локально (профиль `local` активен по умолчанию):

   ```bash
   mvn clean package
   java -jar target/lab1-book-library.jar
   ```

   Либо через IDE / `mvn spring-boot:run`.

3. При старте Liquibase автоматически создаст таблицу `books` в БД.

## Вариант 2. Полностью в Docker (все сервисы и БД в одной сети)

```bash
docker compose up -d --build
```

Compose поднимает:
- `db` — PostgreSQL 16 (сервис `db`, сеть `library_net`);
- `app` — само приложение с профилем `docker`, которое обращается к БД по
  адресу `db:5432` внутри той же docker-сети;
- `report-service` — читает ту же БД по `db:5432`;
- `gateway` — направляет HTTP-запросы к двум сервисам.

Основной сервис доступен на `http://localhost:8080`, сервис отчётов — на
`http://localhost:8081`, шлюз — на `http://localhost:8082`.

## Swagger

После запуска (любым из вариантов) открыть:

```
http://localhost:8080/swagger-ui.html
```

Там доступны и через него можно продемонстрировать все CRUD-операции:

| Метод | Путь | Описание |
|---|---|---|
| GET | `/api/books` | список всех книг |
| GET | `/api/books/{id}` | книга по id |
| POST | `/api/books` | добавить книгу (title, authorLastName, authorFirstName, authorPatronymic, isbn) |
| PUT | `/api/books/{id}` | отредактировать книгу |
| DELETE | `/api/books/{id}` | удалить книгу |
| POST | `/api/books/{id}/issue-to-hands` | выдать книгу читателю на руки (readerName) |
| POST | `/api/books/{id}/issue-to-reading-room` | выдать книгу в читальный зал (readerName) |
| POST | `/api/books/{id}/return` | принять возвращённую книгу |

Попытка выдать уже выданную книгу или вернуть книгу, которая и так в
библиотеке, вернёт `409 Conflict` с понятным сообщением — это удобно
показать на защите как проверку бизнес-правил.

## Проверка вручную (curl)

```bash
# добавить книгу
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Война и мир","authorLastName":"Толстой","authorFirstName":"Лев","authorPatronymic":"Николаевич","isbn":"978-5-17-090515-4"}'

# выдать на руки
curl -X POST http://localhost:8080/api/books/1/issue-to-hands \
  -H "Content-Type: application/json" \
  -d '{"readerName":"Иванов И. И."}'

# вернуть
curl -X POST http://localhost:8080/api/books/1/return
```

## Лабораторная работа 2. Сервис отчётов и шлюз

В репозитории теперь три независимых Spring Boot приложения:

| Папка | Роль | Порт |
|---|---|---:|
| корень проекта | основной сервис: CRUD, выдача и возврат книг | 8080 |
| `report-service/` | читает ту же таблицу `books`, отдаёт список для отчёта | 8081 |
| `gateway/` | пересылает запросы к двум сервисам | 8082 |

Сервис отчётов выполняет только чтение. Он не создаёт дополнительных таблиц и
не запускает миграции: структурой БД владеет основной сервис. Шлюз не
подключается к БД. Его маршруты сохраняют исходные пути:

- `http://localhost:8082/api/books/**` → основной сервис;
- `http://localhost:8082/api/reports/books` → сервис отчётов.

Для запуска всех сервисов и PostgreSQL в Docker:

```bash
docker compose up -d --build
docker compose ps
```

Дождитесь запуска приложений и выполните в Postman коллекцию
`postman/lab2.postman_collection.json` сверху вниз. Первый запрос создаёт книгу
и сохраняет её ID в переменную коллекции; следующие проверяют чтение, выдачу
и появление книги в отчёте через шлюз. Последний запрос показывает прямой
доступ к отчётам на порту 8081 — в ЛР2 он ещё разрешён. В ЛР3 его нужно убрать.

Для локального запуска Java-приложений (PostgreSQL остаётся в Docker):

```bash
docker compose up -d db
mvn -f pom.xml clean package
mvn -f report-service/pom.xml clean package
mvn -f gateway/pom.xml clean package
java -jar target/lab1-book-library.jar
java -jar report-service/target/library-report-service.jar
java -jar gateway/target/library-gateway.jar
```

Каждую команду `java -jar` запускайте в отдельном терминале, в указанном
порядке. Для тестирования через шлюз используйте порт 8082. Swagger
основного сервиса остаётся доступен напрямую на порту 8080.
