package com.crud.kodillalibrary.domain.dao;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.domain.util.Status;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentalDaoTestSuite {
    @Autowired
    private RentalDao rentalDao;

    @Test
    public void testRentalDaoSave() {
        //Given
        Rental rental = new Rental(new Date(), new Date((new Date().getTime()) + 86400000 * 5));

        //When
        rentalDao.save(rental);

        //Then
        int id = rental.getRentalId();
        Optional<Rental> currentRental = rentalDao.findById(id);
        Assert.assertTrue(currentRental.isPresent());

        //CleanUp
        rentalDao.deleteById(id);
    }

    @Test
    public void testRentalDaoSaveWithBookPieces_ManyToMany() {
        //Given
        Rental rental1 = new Rental(new Date(), new Date((new Date().getTime()) + 86400000 * 5));
        Rental rental2 = new Rental(new Date(), new Date((new Date().getTime()) + 86400000 * 10));

        BookPiece bookPiece1 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece2 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece3 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece4 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece5 = new BookPiece(Status.AVAILABLE);

        Collections.addAll(rental1.getBookPieceList(), bookPiece1, bookPiece2);
        Collections.addAll(rental2.getBookPieceList(), bookPiece3, bookPiece4, bookPiece5);

        bookPiece1.getRentalList().add(rental1);
        bookPiece2.getRentalList().add(rental1);
        bookPiece3.getRentalList().add(rental2);
        bookPiece4.getRentalList().add(rental2);
        bookPiece5.getRentalList().add(rental2);


        //When
        rentalDao.save(rental1);
        rentalDao.save(rental2);

        //Then
        int rental1Id = rental1.getRentalId();
        int rental2Id = rental2.getRentalId();
        Optional<Rental> rental1Retrieved = rentalDao.findById(rental1Id);
        Optional<Rental> rental2Retrieved = rentalDao.findById(rental2Id);

       Assert.assertEquals(2, rental1Retrieved.get().getBookPieceList().size());
       Assert.assertEquals(3, rental2Retrieved.get().getBookPieceList().size());

        //CleanUp
        rentalDao.deleteById(rental1Id);
        rentalDao.deleteById(rental2Id);
    }

}