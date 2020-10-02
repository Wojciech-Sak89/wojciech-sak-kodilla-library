package com.crud.kodillalibrary.domain.dao;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.util.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface BookPieceDao extends CrudRepository<BookPiece, Integer> {
    //findByStatus need add?
    List<BookPiece> findBookPiecesByStatus(Status status);


}
