package ru.ulstu.library.report.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ulstu.library.report.dto.BookReportItem;
import ru.ulstu.library.report.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/books")
    public List<BookReportItem> listBooks() {
        return reportService.listBooks();
    }
}
