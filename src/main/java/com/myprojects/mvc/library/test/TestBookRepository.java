package com.myprojects.mvc.library.test;

import com.myprojects.mvc.library.dao.BookDao;
import com.myprojects.mvc.library.dao.BookRepository;
import com.myprojects.mvc.library.entities.Book;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class TestBookRepository {

    private EmbeddedDatabase embeddedDatabase;

    private JdbcTemplate jdbcTemplate;

    private BookRepository bookRepository;
    private Book book;

    @Before
    public void setUp() {
        // Создадим базу данных для тестирования
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()// Добавляем скрипты schema.sql и data.sql
                .setType(EmbeddedDatabaseType.H2)// Используем базу H2
                .build();

        // Создадим JdbcTemplate
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);

        // Создадим BookRepository
        bookRepository = new BookDao(jdbcTemplate);
    }

    @After
    public void onDestroy() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testFindAll() {
        Assert.assertNotNull(bookRepository.findAll());
        Assert.assertEquals(3, bookRepository.findAll().size());
    }

    @Test
    public void testSave() {
        Long bookId = bookRepository.save(new Book("Alice in Wonderland", "Lewis Carroll", "1865"));

        Assert.assertNotNull(bookId);
        book = bookRepository.findOne(bookId);
        Assert.assertNotNull(book.getId());
        Assert.assertEquals("Alice in Wonderland", book.getTitle());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testSaveInvalid() {
        bookRepository.save(new Book());
    }

    @Test
    public void testUpdate() {
        Book book = jdbcTemplate.queryForObject("select * from book where id = 1", BookRepository.ROW_MAPPER);
        book.setTitle("White Fang");

        Long bookId = bookRepository.save(book);
        book = bookRepository.findOne(bookId);
        Assert.assertNotNull(book);
        Assert.assertNotNull(book.getId());
        Assert.assertEquals("White Fang", book.getTitle());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateInvalid() {
        Book book = jdbcTemplate.queryForObject("select * from book where id = 1", BookRepository.ROW_MAPPER);
        book.setTitle(null);

        bookRepository.save(book);
    }
}
