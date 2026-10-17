package ru.ulstu.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.ulstu.library.entity.Book;
import ru.ulstu.library.entity.BookStatus;

import java.time.LocalDateTime;

@Schema(description = "Данные о книге")
public class BookResponse {

    private Long id;
    private String title;
    private String authorLastName;
    private String authorFirstName;
    private String authorPatronymic;
    private String isbn;
    private BookStatus status;
    private String readerName;
    private LocalDateTime issuedAt;
    private LocalDateTime returnedAt;
    private LocalDateTime createdAt;

    public static BookResponse from(Book book) {
        BookResponse response = new BookResponse();
        response.id = book.getId();
        response.title = book.getTitle();
        response.authorLastName = book.getAuthorLastName();
        response.authorFirstName = book.getAuthorFirstName();
        response.authorPatronymic = book.getAuthorPatronymic();
        response.isbn = book.getIsbn();
        response.status = book.getStatus();
        response.readerName = book.getReaderName();
        response.issuedAt = book.getIssuedAt();
        response.returnedAt = book.getReturnedAt();
        response.createdAt = book.getCreatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorPatronymic() {
        return authorPatronymic;
    }

    public String getIsbn() {
        return isbn;
    }

    public BookStatus getStatus() {
        return status;
    }

    public String getReaderName() {
        return readerName;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
