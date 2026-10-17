package ru.ulstu.library.report.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.library.report.dto.BookReportItem;
import ru.ulstu.library.report.repository.BookRepository;

import java.util.List;

@Service
public class ReportService {

    private final BookRepository bookRepository;

    public ReportService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookReportItem> listBooks() {
        return bookRepository.findAllByOrderByIdAsc().stream()
                .map(BookReportItem::from)
                .toList();
    }
}
