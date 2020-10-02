package com.crud.kodillalibrary.domain.dao;

import com.crud.kodillalibrary.domain.Reader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface ReaderDao extends CrudRepository<Reader, Integer> {
}
