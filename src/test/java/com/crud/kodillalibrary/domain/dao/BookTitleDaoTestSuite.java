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

        try {
            Optional<BookTitle> requestedTitle = bookTitleDao.findById(id);
            Assert.assertTrue(requestedTitle.isPresent());

        //CleanUp
        } finally {
            bookTitleDao.deleteById(id);
        }



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

        try {
            Assert.assertNotEquals(0, id_Abomination);
            Assert.assertNotEquals(0, id_Dracula);

        //CleanUp
        } finally {
            bookTitleDao.deleteById(id_Abomination);
            bookTitleDao.deleteById(id_Dracula);
        }
    }

    @Test
    public void testBookTitle_findByTitleAndAuthorName() {
        //Given
        BookTitle bookTitle_Abomination = new BookTitle("Abomination", "Dan Simmons", 2019);
        BookTitle bookTitle_Dracula = new BookTitle("Dracula", "Bram Stoker", 1900);

        bookTitleDao.save(bookTitle_Abomination);
        bookTitleDao.save(bookTitle_Dracula);

        int id_Abomination = bookTitle_Abomination.getBookId();
        int id_Dracula = bookTitle_Dracula.getBookId();

        //When
        Optional<BookTitle> found_Abomination = bookTitleDao.findBookTitleByTitleAndAuthorName("Abomination", "Dan Simmons");
        Optional<BookTitle> found_Dracula = bookTitleDao.findBookTitleByTitleAndAuthorName("Dracula", "Bram Stoker");

        //Then
        try {
            Assert.assertTrue(found_Abomination.isPresent());
            Assert.assertEquals("Abomination", found_Abomination.get().getTitle());
            Assert.assertEquals("Dan Simmons", found_Abomination.get().getAuthorName());
            Assert.assertEquals(2019, found_Abomination.get().getPublishmentYear());

            Assert.assertTrue(found_Dracula.isPresent());
            Assert.assertEquals("Dracula", found_Dracula.get().getTitle());
            Assert.assertEquals("Bram Stoker", found_Dracula.get().getAuthorName());
            Assert.assertEquals(1900, found_Dracula.get().getPublishmentYear());

        //CleanUp
        } finally {
            bookTitleDao.deleteById(id_Abomination);
            bookTitleDao.deleteById(id_Dracula);
        }

    }

}