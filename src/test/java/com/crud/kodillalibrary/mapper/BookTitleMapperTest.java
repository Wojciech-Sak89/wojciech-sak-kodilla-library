package com.crud.kodillalibrary.mapper;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.dao.BookPieceDao;
import com.crud.kodillalibrary.dao.BookTitleDao;
import com.crud.kodillalibrary.domain.dto.BookTitleDto;
import com.crud.kodillalibrary.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookTitleMapperTest {
    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private BookPieceDao bookPieceDao;

    @Autowired
    private BookTitleDao bookTitleDao;

    @Test
    public void testMapToBookTitle_ZeroPieces() {
        //Given
        BookTitleDto bookTitleDto = new BookTitleDto(
                11, "Abomination", "Dan Simmons", 2019, new ArrayList<>());

        //When
        BookTitle bookTitle = libraryMapper.mapToBookTitle(bookTitleDto);

        //Then
        assertEquals(11, bookTitle.getBookId());
        assertEquals("Abomination", bookTitle.getTitle());
        assertEquals("Dan Simmons", bookTitle.getAuthorName());
        assertEquals(2019, bookTitle.getPublishmentYear());
        assertEquals(0, bookTitle.getBookPieces().size());
    }

    @Test
    public void testMapToBookTitle_WithPieces() {
        //Given
        BookPiece bookPiece = new BookPiece(Status.AVAILABLE);
        bookPieceDao.save(bookPiece);
        int id = bookPiece.getPieceId();

        BookTitleDto bookTitleDto = new BookTitleDto(
                11, "Abomination", "Dan Simmons", 2019, Collections.singletonList(id));

        //When
        BookTitle bookTitle = libraryMapper.mapToBookTitle(bookTitleDto);

        //Then
        assertEquals(11, bookTitle.getBookId());
        assertEquals("Abomination", bookTitle.getTitle());
        assertEquals("Dan Simmons", bookTitle.getAuthorName());
        assertEquals(2019, bookTitle.getPublishmentYear());
        assertEquals(1, bookTitle.getBookPieces().size());

        //CleanUp
        bookPieceDao.deleteById(id);
    }

    @Test
    public void testMapToBookTitleDto_ZeroPieces() {
        //Given
        BookTitle bookTitle_Abomination = new BookTitle("Abomination", "Dan Simmons", 2019);

        //When
        BookTitleDto bookTitleDto = libraryMapper.mapToBookTitleDto(bookTitle_Abomination);

        //Then
        assertEquals("Abomination", bookTitleDto.getTitle());
        assertEquals("Dan Simmons", bookTitleDto.getAuthorName());
        assertEquals(2019, bookTitleDto.getPublishmentYear());
        assertEquals(0, bookTitleDto.getPiecesIds().size());
    }

    @Test
    public void testMapToBookTitleDto_WithPieces() {
        //Given
        BookPiece bookPiece = new BookPiece(Status.AVAILABLE);

        BookTitle bookTitle_Abomination = new BookTitle("Abomination", "Dan Simmons", 2019);

        bookTitle_Abomination.getBookPieces().add(bookPiece);
        bookPiece.setBookTitle(bookTitle_Abomination);

        bookTitleDao.save(bookTitle_Abomination);
        int bookTitleId = bookTitle_Abomination.getBookId();

        bookPieceDao.save(bookPiece);
        int pieceId = bookPiece.getPieceId();

        //When
        BookTitleDto bookTitleDto = libraryMapper.mapToBookTitleDto(bookTitle_Abomination);

        //Then
        assertEquals("Abomination", bookTitleDto.getTitle());
        assertEquals("Dan Simmons", bookTitleDto.getAuthorName());
        assertEquals(2019, bookTitleDto.getPublishmentYear());
        assertEquals(1, bookTitleDto.getPiecesIds().size());
        assertEquals(pieceId, bookTitleDto.getPiecesIds().get(0).intValue());

        //CleanUp
        bookTitleDao.deleteById(bookTitleId);
    }
}
