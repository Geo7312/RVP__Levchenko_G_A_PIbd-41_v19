package ru.ulstu.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.library.dto.BookCreateRequest;
import ru.ulstu.library.dto.BookUpdateRequest;
import ru.ulstu.library.entity.Book;
import ru.ulstu.library.entity.BookStatus;
import ru.ulstu.library.exception.BookNotFoundException;
import ru.ulstu.library.exception.InvalidBookStateException;
import ru.ulstu.library.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional
    public Book create(BookCreateRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthorLastName(request.getAuthorLastName());
        book.setAuthorFirstName(request.getAuthorFirstName());
        book.setAuthorPatronymic(request.getAuthorPatronymic());
        book.setIsbn(request.getIsbn());
        book.setStatus(BookStatus.IN_LIBRARY);
        book.setCreatedAt(LocalDateTime.now());
        return bookRepository.save(book);
    }

    @Transactional
    public Book update(Long id, BookUpdateRequest request) {
        Book book = findById(id);
        book.setTitle(request.getTitle());
        book.setAuthorLastName(request.getAuthorLastName());
        book.setAuthorFirstName(request.getAuthorFirstName());
        book.setAuthorPatronymic(request.getAuthorPatronymic());
        book.setIsbn(request.getIsbn());
        return bookRepository.save(book);
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    /** Выдать книгу на руки читателю. */
    @Transactional
    public Book issueToHands(Long id, String readerName) {
        Book book = findById(id);
        requireInLibrary(book);
        book.setStatus(BookStatus.ON_HANDS);
        book.setReaderName(readerName);
        book.setIssuedAt(LocalDateTime.now());
        book.setReturnedAt(null);
        return bookRepository.save(book);
    }

    /** Выдать книгу для чтения в читальном зале. */
    @Transactional
    public Book issueToReadingRoom(Long id, String readerName) {
        Book book = findById(id);
        requireInLibrary(book);
        book.setStatus(BookStatus.IN_READING_ROOM);
        book.setReaderName(readerName);
        book.setIssuedAt(LocalDateTime.now());
        book.setReturnedAt(null);
        return bookRepository.save(book);
    }

    /** Принять возвращённую книгу обратно в библиотеку. */
    @Transactional
    public Book returnBook(Long id) {
        Book book = findById(id);
        if (book.getStatus() == BookStatus.IN_LIBRARY) {
            throw new InvalidBookStateException(
                    "Книга с id=" + id + " уже находится в библиотеке и не может быть возвращена повторно");
        }
        book.setStatus(BookStatus.IN_LIBRARY);
        book.setReaderName(null);
        book.setReturnedAt(LocalDateTime.now());
        return bookRepository.save(book);
    }

    private void requireInLibrary(Book book) {
        if (book.getStatus() != BookStatus.IN_LIBRARY) {
            throw new InvalidBookStateException(
                    "Книга с id=" + book.getId() + " уже выдана (текущий статус: " + book.getStatus() + ")");
        }
    }
}
