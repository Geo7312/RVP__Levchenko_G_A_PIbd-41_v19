package ru.ulstu.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ulstu.library.entity.Book;
import ru.ulstu.library.entity.BookStatus;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByStatus(BookStatus status);
}
