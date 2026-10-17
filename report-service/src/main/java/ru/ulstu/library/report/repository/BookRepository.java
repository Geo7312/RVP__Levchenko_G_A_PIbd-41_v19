package ru.ulstu.library.report.repository;

import org.springframework.data.repository.Repository;
import ru.ulstu.library.report.entity.Book;

import java.util.List;

public interface BookRepository extends Repository<Book, Long> {
    List<Book> findAllByOrderByIdAsc();
}
