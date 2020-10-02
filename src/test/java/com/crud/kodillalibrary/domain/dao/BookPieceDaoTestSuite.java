package com.crud.kodillalibrary.domain.dao;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.util.Status;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookPieceDaoTestSuite {

    @Autowired
    private BookPieceDao bookPieceDao;

    @Autowired
    private BookTitleDao bookTitleDao;

    @Test
    public void testBookPieceDaoSave() {
        //Given
        BookPiece bookPiece = new BookPiece(Status.AVAILABLE);

        //When
        bookPieceDao.save(bookPiece);

        //Then
        int id = bookPiece.getPieceId();
        Optional<BookPiece> requestedPiece = bookPieceDao.findById(id);
        Assert.assertTrue(requestedPiece.isPresent());

        //CleanUp
        bookPieceDao.deleteById(id);
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

        //When
//        bookPieceDao.save(bookPiece_Abomination1); //co zrobić, by zadziało save tylko egzemplarzy?
//        bookPieceDao.save(bookPiece_Abomination2); //bez bookTitleDao.save(bookTitle_Abomination);
//        bookPieceDao.save(bookPiece_Abomination3);
//        bookPieceDao.save(bookPiece_Abomination4);
//        bookPieceDao.save(bookPiece_Abomination5);
//        bookPieceDao.save(bookPiece_Abomination6);
        bookTitleDao.save(bookTitle_Abomination);

        List<BookPiece> availablePieces = bookPieceDao.findBookPiecesByStatus(Status.AVAILABLE);
        List<BookPiece> inCirculationPieces = bookPieceDao.findBookPiecesByStatus(Status.IN_CIRCULATION);
        List<BookPiece> damagedPieces = bookPieceDao.findBookPiecesByStatus(Status.DAMAGED);

        //Then
        Assert.assertEquals(3, availablePieces.size());
        Assert.assertEquals(2, inCirculationPieces.size());
        Assert.assertEquals(1, damagedPieces.size());

        //CleanUp
        int titleId = bookTitle_Abomination.getBookId();
        bookTitleDao.deleteById(titleId);
    }

}