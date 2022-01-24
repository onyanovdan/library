package com.myprojects.mvc.library.dao;

import com.myprojects.mvc.library.entities.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface BookRepository {

    // Mapper, make Book-class object from DB table string
    RowMapper<Book> ROW_MAPPER = (ResultSet resultSet, int rowNum) ->
            new Book(resultSet.getLong("id"), resultSet.getString("title"), resultSet.getString("author"), resultSet.getString("description"));

    List<Book> findAll();

    Book findOne(Long id);

    Long save(Book book);

    List<Book> findAllSortedByTitleDesc();

    Map<String, List<Book>> findAllGroupedByAuthor();
}
