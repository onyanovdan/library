package com.myprojects.mvc.library.controllers;

import com.myprojects.mvc.library.dao.BookDao;
import com.myprojects.mvc.library.dao.BookRepository;
import com.myprojects.mvc.library.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = "*")
    public ResponseEntity redirect() {
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, "/books/sorted")
                .build();
    }

    @GetMapping(value = "/books/sorted")
    @ResponseBody
    public List<Book> getSortedByTitleDescList() {
        return bookRepository.findAllSortedByTitleDesc();
    }

    @GetMapping(value = "/books/grouped")
    @ResponseBody
    public Map<String, List<Book>> getGroupedByAuthorList() {
        return bookRepository.findAllGroupedByAuthor();
    }

    @PostMapping(value = "/books")
    public Long postBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }
}
