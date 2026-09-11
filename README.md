# Лабораторная работа 1. Основной сервис

**Вариант 19.** Учёт книг и читателей в библиотеке: выдать книгу на руки,
выдать для чтения в читальном зале, принять возвращённую книгу.

Стек: **Spring Boot 3 / Java 17, PostgreSQL, Liquibase, springdoc-openapi
(Swagger UI), Docker / docker-compose**. В базе данных ровно одна таблица —
`books`, где статус книги (`IN_LIBRARY` / `ON_HANDS` / `IN_READING_ROOM`) и
поле `reader_name` покрывают весь учёт по варианту.

## Структура проекта

```
lab1-book-library/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
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
        └── db/changelog/                     — миграции Liquibase (создание таблицы books)
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

## Вариант 2. Полностью в Docker (приложение + БД в одной сети)

```bash
docker compose up -d --build
```

Compose поднимает:
- `db` — PostgreSQL 16 (сервис `db`, сеть `library_net`);
- `app` — само приложение с профилем `docker`, которое обращается к БД по
  адресу `db:5432` внутри той же docker-сети.

Приложение будет доступно на `http://localhost:8080`.

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
| POST | `/api/books` | добавить книгу (title, author, isbn) |
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
  -d '{"title":"Война и мир","author":"Л. Н. Толстой","isbn":"978-5-17-090515-4"}'

# выдать на руки
curl -X POST http://localhost:8080/api/books/1/issue-to-hands \
  -H "Content-Type: application/json" \
  -d '{"readerName":"Иванов И. И."}'

# вернуть
curl -X POST http://localhost:8080/api/books/1/return
```

## Задел на следующие лабораторные

- ЛР2 (сервис отчётов) и ЛР3 (шлюз) будут отдельными модулями/сервисами,
  читающими ту же таблицу `books` через REST (в ЛР3 — конкретно через
  `RestTemplate` из основного сервиса).
- Поэтому в этом сервисе уже заложены понятные DTO и разделение на
  контроллер/сервис/репозиторий — это упростит подключение к нему извне.
