package ru.ulstu.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Данные для редактирования книги")
public class BookUpdateRequest {

    @NotBlank
    @Schema(description = "Название книги", example = "Война и мир")
    private String title;

    @NotBlank
    @Schema(description = "Автор книги", example = "Л. Н. Толстой")
    private String author;

    @Schema(description = "ISBN книги", example = "978-5-17-090515-4")
    private String isbn;
}
