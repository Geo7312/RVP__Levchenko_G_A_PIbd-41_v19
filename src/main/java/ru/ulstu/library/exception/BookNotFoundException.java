package ru.ulstu.library.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Книга с id=" + id + " не найдена");
    }
}
