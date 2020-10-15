package com.crud.kodillalibrary.dao;

import com.crud.kodillalibrary.domain.Reader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface ReaderDao extends CrudRepository<Reader, Integer> {
    Optional<Reader> findByReaderId(int readerId);
}
