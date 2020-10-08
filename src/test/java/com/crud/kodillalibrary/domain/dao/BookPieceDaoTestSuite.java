package com.crud.kodillalibrary.domain.dao;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
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
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookPieceDaoTestSuite {

    @Autowired
    private BookPieceDao bookPieceDao;

    @Autowired
    private BookTitleDao bookTitleDao;

    @Autowired
    private RentalDao rentalDao;

    @Test
    public void testBookPieceDaoSave() {
        //Given
        BookPiece bookPiece = new BookPiece(Status.AVAILABLE);

        //When
        bookPieceDao.save(bookPiece);

        //Then
        int id = bookPiece.getPieceId();

        try {
            Optional<BookPiece> requestedPiece = bookPieceDao.findById(id);
            Assert.assertTrue(requestedPiece.isPresent());

        //CleanUp
        } finally {
            bookPieceDao.deleteById(id);
        }
    }

    @Test
    public void testBookPieceDaoSave_WithRentals() {
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
        bookPieceDao.save(bookPiece1);
        bookPieceDao.save(bookPiece2);
        bookPieceDao.save(bookPiece3);
        bookPieceDao.save(bookPiece4);
        bookPieceDao.save(bookPiece5);

        int piece1Id = bookPiece1.getPieceId();
        int piece2Id = bookPiece2.getPieceId();
        int piece3Id = bookPiece3.getPieceId();
        int piece4Id = bookPiece4.getPieceId();
        int piece5Id = bookPiece5.getPieceId();

        int rental1Id = rental1.getRentalId();
        int rental2Id = rental2.getRentalId();

        //Then
        try {
            Optional<BookPiece> piece1Retrieved = bookPieceDao.findById(piece1Id);
            Optional<BookPiece> piece2Retrieved = bookPieceDao.findById(piece2Id);
            Optional<BookPiece> piece3Retrieved = bookPieceDao.findById(piece3Id);
            Optional<BookPiece> piece4Retrieved = bookPieceDao.findById(piece4Id);
            Optional<BookPiece> piece5Retrieved = bookPieceDao.findById(piece5Id);

            Assert.assertTrue(piece1Retrieved.isPresent());
            Assert.assertTrue(piece2Retrieved.isPresent());
            Assert.assertTrue(piece3Retrieved.isPresent());
            Assert.assertTrue(piece4Retrieved.isPresent());
            Assert.assertTrue(piece5Retrieved.isPresent());

            Assert.assertEquals(1, piece1Retrieved.get().getRentalList().size());
            Assert.assertEquals(rental1.getRentalId(), piece2Retrieved.get().getRentalList().get(0).getRentalId());

        //CleanUp
        } finally {
            bookPieceDao.deleteById(piece1Id);
            bookPieceDao.deleteById(piece2Id);
            bookPieceDao.deleteById(piece3Id);
            bookPieceDao.deleteById(piece4Id);
            bookPieceDao.deleteById(piece5Id);
            rentalDao.deleteById(rental1Id);
            rentalDao.deleteById(rental2Id);
        }
    }

    @Test
    public void testBookPieceDaoFindByStatus() {
        BookTitle bookTitle_Abomination = new BookTitle("Abomination", "Dan Simmons", 2019);

        BookPiece bookPiece_Abomination1 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece_Abomination2 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece_Abomination3 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece_Abomination4 = new BookPiece(Status.IN_CIRCULATION);
        BookPiece bookPiece_Abomination5 = new BookPiece(Status.IN_CIRCULATION);
        BookPiece bookPiece_Abomination6 = new BookPiece(Status.DAMAGED);

        bookPiece_Abomination1.setBookTitle(bookTitle_Abomination);
        bookPiece_Abomination2.setBookTitle(bookTitle_Abomination);
        bookPiece_Abomination3.setBookTitle(bookTitle_Abomination);
        bookPiece_Abomination4.setBookTitle(bookTitle_Abomination);
        bookPiece_Abomination5.setBookTitle(bookTitle_Abomination);
        bookPiece_Abomination6.setBookTitle(bookTitle_Abomination);

        Collections.addAll(bookTitle_Abomination.getBookPieces(),
                bookPiece_Abomination1, bookPiece_Abomination2, bookPiece_Abomination3,
                bookPiece_Abomination4, bookPiece_Abomination5, bookPiece_Abomination6);

        bookTitleDao.save(bookTitle_Abomination);

        List<BookPiece> availablePieces = bookPieceDao.findBookPiecesByStatus(Status.AVAILABLE);
        List<BookPiece> inCirculationPieces = bookPieceDao.findBookPiecesByStatus(Status.IN_CIRCULATION);
        List<BookPiece> damagedPieces = bookPieceDao.findBookPiecesByStatus(Status.DAMAGED);

        //Then
        try {
            Assert.assertEquals(3, availablePieces.size());
            Assert.assertEquals(2, inCirculationPieces.size());
            Assert.assertEquals(1, damagedPieces.size());

        //CleanUp
        } finally {
            int titleId = bookTitle_Abomination.getBookId();
            bookTitleDao.deleteById(titleId);
        }
    }

}