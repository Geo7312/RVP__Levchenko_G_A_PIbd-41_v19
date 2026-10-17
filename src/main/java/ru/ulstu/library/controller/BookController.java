package ru.ulstu.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.library.dto.BookCreateRequest;
import ru.ulstu.library.dto.BookResponse;
import ru.ulstu.library.dto.BookUpdateRequest;
import ru.ulstu.library.dto.IssueBookRequest;
import ru.ulstu.library.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Книги", description = "Учёт книг и читателей в библиотеке (вариант 19)")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех книг")
    public List<BookResponse> getAll() {
        return bookService.findAll().stream()
                .map(BookResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить книгу по идентификатору")
    public BookResponse getById(@PathVariable Long id) {
        return BookResponse.from(bookService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить новую книгу в библиотеку")
    public BookResponse create(@Valid @RequestBody BookCreateRequest request) {
        return BookResponse.from(bookService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Отредактировать данные книги")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookUpdateRequest request) {
        return BookResponse.from(bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить книгу из учёта")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

    @PostMapping("/{id}/issue-to-hands")
    @Operation(summary = "Выдать книгу читателю на руки")
    public BookResponse issueToHands(@PathVariable Long id, @Valid @RequestBody IssueBookRequest request) {
        return BookResponse.from(bookService.issueToHands(id, request.getReaderName()));
    }

    @PostMapping("/{id}/issue-to-reading-room")
    @Operation(summary = "Выдать книгу для чтения в читальном зале")
    public BookResponse issueToReadingRoom(@PathVariable Long id, @Valid @RequestBody IssueBookRequest request) {
        return BookResponse.from(bookService.issueToReadingRoom(id, request.getReaderName()));
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Принять возвращённую книгу обратно в библиотеку")
    public BookResponse returnBook(@PathVariable Long id) {
        return BookResponse.from(bookService.returnBook(id));
    }
}
