package com.crud.kodillalibrary.dao;

import com.crud.kodillalibrary.domain.Rental;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface RentalDao extends CrudRepository<Rental, Integer> {
}
