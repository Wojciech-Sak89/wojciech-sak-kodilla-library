package com.crud.kodillalibrary.domain.dao;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.Rental;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface RentalDao extends CrudRepository<Rental, Integer> {

}
