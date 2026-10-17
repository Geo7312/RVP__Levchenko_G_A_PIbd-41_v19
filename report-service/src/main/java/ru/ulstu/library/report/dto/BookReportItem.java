package ru.ulstu.library.report.dto;

import ru.ulstu.library.report.entity.Book;
import ru.ulstu.library.report.entity.BookStatus;

import java.time.LocalDateTime;

public record BookReportItem(
        Long id,
        String title,
        String authorLastName,
        String authorFirstName,
        String authorPatronymic,
        String isbn,
        BookStatus status,
        String readerName,
        LocalDateTime issuedAt,
        LocalDateTime returnedAt,
        LocalDateTime createdAt
) {
    public static BookReportItem from(Book book) {
        return new BookReportItem(
                book.getId(), book.getTitle(), book.getAuthorLastName(),
                book.getAuthorFirstName(), book.getAuthorPatronymic(), book.getIsbn(),
                book.getStatus(), book.getReaderName(), book.getIssuedAt(),
                book.getReturnedAt(), book.getCreatedAt()
        );
    }
}
