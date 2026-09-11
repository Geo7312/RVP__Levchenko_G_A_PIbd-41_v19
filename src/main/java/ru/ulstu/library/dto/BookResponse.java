package ru.ulstu.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ulstu.library.entity.Book;
import ru.ulstu.library.entity.BookStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные о книге")
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BookStatus status;
    private String readerName;
    private LocalDateTime issuedAt;
    private LocalDateTime returnedAt;
    private LocalDateTime createdAt;

    public static BookResponse from(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .status(book.getStatus())
                .readerName(book.getReaderName())
                .issuedAt(book.getIssuedAt())
                .returnedAt(book.getReturnedAt())
                .createdAt(book.getCreatedAt())
                .build();
    }
}
