package ru.ulstu.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Данные для выдачи книги читателю")
public class IssueBookRequest {

    @NotBlank
    @Schema(description = "Имя читателя, которому выдаётся книга", example = "Иванов Иван Иванович")
    private String readerName;

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }
}
