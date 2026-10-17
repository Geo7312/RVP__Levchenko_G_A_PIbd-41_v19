package ru.ulstu.library.report.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/** Read-only mapping of the books table owned by the main service. */
@Entity
@Table(name = "books")
public class Book {

    @Id
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author_last_name", nullable = false)
    private String authorLastName;

    @Column(name = "author_first_name")
    private String authorFirstName;

    @Column(name = "author_patronymic")
    private String authorPatronymic;

    @Column(name = "isbn")
    private String isbn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status;

    @Column(name = "reader_name")
    private String readerName;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Book() {
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthorLastName() { return authorLastName; }
    public String getAuthorFirstName() { return authorFirstName; }
    public String getAuthorPatronymic() { return authorPatronymic; }
    public String getIsbn() { return isbn; }
    public BookStatus getStatus() { return status; }
    public String getReaderName() { return readerName; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public LocalDateTime getReturnedAt() { return returnedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
