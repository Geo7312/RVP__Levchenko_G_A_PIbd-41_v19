package ru.ulstu.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Данные для редактирования книги")
public class BookUpdateRequest {

    @NotBlank
    @Schema(description = "Название книги", example = "Война и мир")
    private String title;

    @NotBlank
    @Schema(description = "Фамилия автора", example = "Толстой")
    private String authorLastName;

    @NotBlank
    @Schema(description = "Имя автора", example = "Лев")
    private String authorFirstName;

    @Schema(description = "Отчество автора (если есть)", example = "Николаевич")
    private String authorPatronymic;

    @Schema(description = "ISBN книги", example = "978-5-17-090515-4")
    private String isbn;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorPatronymic() {
        return authorPatronymic;
    }

    public void setAuthorPatronymic(String authorPatronymic) {
        this.authorPatronymic = authorPatronymic;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
