package com.myprojects.mvc.library.dao;

import com.myprojects.mvc.library.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BookDao implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", ROW_MAPPER);
    }

    @Override
    public Book findOne(Long id) {
        Book book = null;
        try {
            book = jdbcTemplate.queryForObject("SELECT * FROM book WHERE id = ?", ROW_MAPPER, id);
        } catch (DataAccessException dataAccessException) {
            System.out.println(new StringBuilder("Couldn't find entity of type Person with id ").append(id));
        }

        return book;
    }

    @Override
    public Long save(Book book) {
        if (book.getId() == null) {
            book.setId(UUID.randomUUID().getMostSignificantBits());

            jdbcTemplate.update("INSERT INTO book VALUES (?, ?, ?, ?)",
                    book.getId(), book.getTitle(), book.getAuthor(), book.getDescription());
        } else {
            jdbcTemplate.update("UPDATE book SET title = ?2, author = ?3, description = ?4 WHERE id = ?1",
                    book.getId(), book.getTitle(), book.getAuthor(), book.getDescription());
        }

        return book.getId();
    }

    @Override
    public List<Book> findAllSortedByTitleDesc() {
        return jdbcTemplate.query("SELECT * FROM book ORDER BY title DESC", ROW_MAPPER);
    }

    @Override
    public Map<String, List<Book>> findAllGroupedByAuthor() {
        return this.findAll().parallelStream().collect(Collectors.groupingByConcurrent(Book::getAuthor));
    }
}
