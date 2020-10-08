package com.crud.kodillalibrary.domain.dao;

import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.Reader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface BookTitleDao extends CrudRepository<BookTitle, Integer> {
    Optional<BookTitle> findBookTitleByTitleAndAuthorName(String title, String authorName);
}
