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
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BookTitleDaoTestSuite {

    @Autowired
    private BookTitleDao bookTitleDao;

    @Test
    public void testBookTitleDaoSave() {
        //Given
        BookTitle bookTitle = new BookTitle("Abomination", "Dan Simmons", 2019);

        //When
        bookTitleDao.save(bookTitle);

        //Then
        int id = bookTitle.getBookId();
        Optional<BookTitle> requestedTitle = bookTitleDao.findById(id);
        Assert.assertTrue(requestedTitle.isPresent());

        //CleanUp
        bookTitleDao.deleteById(id);
    }

    @Test
    public void testBookTitleDaoSaveWithBookPieces() {
        //Given
        BookTitle bookTitle_Abomination = new BookTitle("Abomination", "Dan Simmons", 2019);
        BookTitle bookTitle_Dracula = new BookTitle("Dracula", "Bram Stoker", 1900);

        BookPiece bookPiece_Abomination1 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece_Abomination2 = new BookPiece(Status.IN_CIRCULATION);
        BookPiece bookPiece_Abomination3 = new BookPiece(Status.DAMAGED);
        BookPiece bookPiece_Dracula1 = new BookPiece(Status.AVAILABLE);
        BookPiece bookPiece_Dracula2 = new BookPiece(Status.LOST);

        bookPiece_Abomination1.setBookTitle(bookTitle_Abomination);
        bookPiece_Abomination2.setBookTitle(bookTitle_Abomination);
        bookPiece_Abomination3.setBookTitle(bookTitle_Abomination);
        bookPiece_Dracula1.setBookTitle(bookTitle_Dracula);
        bookPiece_Dracula2.setBookTitle(bookTitle_Dracula);

        Collections.addAll(bookTitle_Abomination.getBookPieces(),
                bookPiece_Abomination1, bookPiece_Abomination2, bookPiece_Abomination3);

        Collections.addAll(bookTitle_Dracula.getBookPieces(),
                bookPiece_Dracula1, bookPiece_Dracula2);

        //When
        bookTitleDao.save(bookTitle_Abomination);
        bookTitleDao.save(bookTitle_Dracula);

        //Then
        int id_Abomination = bookTitle_Abomination.getBookId();
        int id_Dracula = bookTitle_Dracula.getBookId();

        Assert.assertNotEquals(0, id_Abomination);
        Assert.assertNotEquals(0, id_Dracula);

        //CleanUp
        bookTitleDao.deleteById(id_Abomination);
        bookTitleDao.deleteById(id_Dracula);
    }

}