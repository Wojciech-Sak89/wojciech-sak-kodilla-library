package com.crud.kodillalibrary.mapper;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.Reader;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.domain.dao.BookPieceDao;
import com.crud.kodillalibrary.domain.dao.BookTitleDao;
import com.crud.kodillalibrary.domain.dao.ReaderDao;
import com.crud.kodillalibrary.domain.dao.RentalDao;
import com.crud.kodillalibrary.domain.dto.BookPieceDto;
import com.crud.kodillalibrary.domain.dto.BookTitleDto;
import com.crud.kodillalibrary.domain.dto.ReaderDto;
import com.crud.kodillalibrary.domain.dto.RentalDto;
import com.crud.kodillalibrary.domain.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryMapperTestSuite {
    @Autowired
    LibraryMapper libraryMapper;

    @Autowired
    BookPieceDao bookPieceDao;

    @Autowired
    BookTitleDao bookTitleDao;

    @Autowired
    ReaderDao readerDao;

    @Autowired
    RentalDao rentalDao;

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

    @Test
    public void testMapToReader_ZeroRents() {
        //Given
        Date date = new Date();
        ReaderDto readerDto = new ReaderDto(1, "Mark", "Addams", date, new ArrayList<>());

        //When
        Reader reader = libraryMapper.mapToReader(readerDto);

        //Then
        assertEquals(1, reader.getReaderId());
        assertEquals("Mark", reader.getName());
        assertEquals("Addams", reader.getSurname());
        assertEquals(date, reader.getAccountCreationDate());
        assertEquals(0, reader.getRentals().size());
    }

    @Test
    public void testMapToReader_WithRents() {
        //Given
        Rental rental = new Rental(new Date(), new Date());
        rentalDao.save(rental);
        int id = rental.getRentalId();

        Date date = new Date();
        ReaderDto readerDto = new ReaderDto(1, "Mark", "Addams", date, Collections.singletonList(id));

        //When
        Reader reader = libraryMapper.mapToReader(readerDto);

        //Then
        assertEquals(1, reader.getReaderId());
        assertEquals("Mark", reader.getName());
        assertEquals("Addams", reader.getSurname());
        assertEquals(date, reader.getAccountCreationDate());
        assertEquals(1, reader.getRentals().size());
        assertEquals(id, reader.getRentals().get(0).getRentalId());

        //CleanUp
        rentalDao.deleteById(id);
    }

    @Test
    public void testMapToReaderDto_ZeroRents() {
        //Given
        Date date = new Date();
        Reader reader = new Reader(1, "Mark", "Addams", date, new ArrayList<>());

        //When
        ReaderDto readerDto = libraryMapper.mapToReaderDto(reader);

        //Then
        assertEquals(1, readerDto.getReaderId());
        assertEquals("Mark", readerDto.getName());
        assertEquals("Addams", readerDto.getSurname());
        assertEquals(date, readerDto.getAccountCreationDate());
        assertEquals(0, readerDto.getRentalsIds().size());
    }

    @Test
    public void testMapToReaderDto_WithRents() {
        //Given
        Rental rental = new Rental(new Date(), new Date());
        rentalDao.save(rental);
        int id = rental.getRentalId();

        Date date = new Date();
        Reader reader = new Reader(2, "Mark", "Addams", date, Collections.singletonList(rental));

        //When
        ReaderDto readerDto = libraryMapper.mapToReaderDto(reader);

        //Then
        assertEquals(2, readerDto.getReaderId());
        assertEquals("Mark", readerDto.getName());
        assertEquals("Addams", readerDto.getSurname());
        assertEquals(date, readerDto.getAccountCreationDate());
        assertEquals(1, readerDto.getRentalsIds().size());
        assertEquals(id, readerDto.getRentalsIds().get(0).intValue());

        //CleanUp
        rentalDao.deleteById(id);
    }

    @Test
    public void testMapToBookPiece_ZeroRents() {
        //Given
        BookTitleDto bookTitleDto = new BookTitleDto(
                2, "title", "author", 1999, new ArrayList<>());

        BookPieceDto bookPieceDto = new BookPieceDto
                (1, bookTitleDto, Status.AVAILABLE, new ArrayList<>());

        //When
        BookPiece bookPiece = libraryMapper.mapToBookPiece(bookPieceDto);

        //Then
        assertEquals(1, bookPiece.getPieceId());
        assertEquals(2, bookPiece.getBookTitle().getBookId());
        assertEquals(Status.AVAILABLE, bookPiece.getStatus());
        assertEquals(0, bookPiece.getRentalList().size());

    }

    @Test
    public void testMapToBookPiece_WithRents() {
        //Given
        Rental rental = new Rental(new Date(), new Date());
        rentalDao.save(rental);
        int rentalId = rental.getRentalId();

        BookTitleDto bookTitleDto = new BookTitleDto(
                2, "title", "author", 1999, new ArrayList<>());

        BookPieceDto bookPieceDto = new BookPieceDto
                (1, bookTitleDto, Status.AVAILABLE, Collections.singletonList(rentalId));

        //When
        BookPiece bookPiece = libraryMapper.mapToBookPiece(bookPieceDto);

        //Then
        assertEquals(1, bookPiece.getPieceId());
        assertEquals(2, bookPiece.getBookTitle().getBookId());
        assertEquals(Status.AVAILABLE, bookPiece.getStatus());
        assertEquals(1, bookPiece.getRentalList().size());
        assertEquals(rentalId, bookPiece.getRentalList().get(0).getRentalId());

        //CleanUp
        rentalDao.deleteById(rentalId);
    }

    @Test
    public void testMapToBookPieceDto_ZeroRents() {
        //Given
        BookTitle bookTitle = new BookTitle(
                2, "title", "author", 1999, new ArrayList<>());

        BookPiece bookPiece = new BookPiece
                (1, bookTitle, Status.AVAILABLE, new ArrayList<>());

        //When
        BookPieceDto bookPieceDto = libraryMapper.mapToBookPieceDto(bookPiece);

        //Then
        assertEquals(1, bookPieceDto.getPieceId());
        assertEquals(2, bookPieceDto.getBookTitle().getBookId());
        assertEquals(Status.AVAILABLE, bookPieceDto.getStatus());
        assertEquals(0, bookPieceDto.getRentalList().size());
    }

    @Test
    public void testMapToBookPieceDto_WithRents() {
        //Given
        Rental rental = new Rental(new Date(), new Date());
        rentalDao.save(rental);
        int rentalId = rental.getRentalId();

        BookTitle bookTitle = new BookTitle(
                2, "title", "author", 1999, new ArrayList<>());

        BookPiece bookPiece = new BookPiece
                (1, bookTitle, Status.IN_CIRCULATION, Collections.singletonList(rental));

        //When
        BookPieceDto bookPieceDto = libraryMapper.mapToBookPieceDto(bookPiece);

        //Then
        assertEquals(1, bookPieceDto.getPieceId());
        assertEquals(2, bookPieceDto.getBookTitle().getBookId());
        assertEquals(Status.IN_CIRCULATION, bookPieceDto.getStatus());
        assertEquals(1, bookPieceDto.getRentalList().size());
        assertEquals(rentalId, bookPieceDto.getRentalList().get(0).intValue());

        //CleanUp
        rentalDao.deleteById(rentalId);
    }

    @Test
    public void testMapToRental_ZeroPieces() {
        //Given
        ReaderDto readerDto = new ReaderDto(
                1, "Mark", "Addams", new Date(), new ArrayList<>());

        Date rentalDate = new Date();
        Date returnDate = new Date();
        RentalDto rentalDto = new RentalDto(1, readerDto, rentalDate, returnDate, new ArrayList<>());

        //When
        Rental rental = libraryMapper.mapToRental(rentalDto);

        //Then
        assertEquals(1, rental.getRentalId());
        assertEquals("Addams", rental.getBorrowingReader().getSurname());
        assertEquals(rentalDate, rental.getRentalDate());
        assertEquals(returnDate, rental.getReturnDate());
        assertEquals(0, rental.getBookPieceList().size());
    }

    @Test
    public void testMapToRental_WithPieces() {
        //Given
        ReaderDto readerDto = new ReaderDto(
                1, "Mark", "Addams", new Date(), new ArrayList<>());

        BookPiece bookPiece = new BookPiece(Status.AVAILABLE);
        bookPieceDao.save(bookPiece);
        int pieceId = bookPiece.getPieceId();

        Date rentalDate = new Date();
        Date returnDate = new Date();
        RentalDto rentalDto = new RentalDto(13, readerDto, rentalDate, returnDate, Collections.singletonList(pieceId));

        //When
        Rental rental = libraryMapper.mapToRental(rentalDto);

        //Then
        assertEquals(13, rental.getRentalId());
        assertEquals("Addams", rental.getBorrowingReader().getSurname());
        assertEquals(rentalDate, rental.getRentalDate());
        assertEquals(returnDate, rental.getReturnDate());
        assertEquals(1, rental.getBookPieceList().size());
        assertEquals(pieceId, rental.getBookPieceList().get(0).getPieceId());

        //CleanUp
        bookPieceDao.deleteById(pieceId);
    }

    @Test
    public void testMapToRentalDto_ZeroPieces() {
        //Given
        Reader reader = new Reader(
                1, "Mark", "Addams", new Date(), new ArrayList<>());

        Date rentalDate = new Date();
        Date returnDate = new Date();
        Rental rental = new Rental(1, reader, rentalDate, returnDate, new ArrayList<>());

        //When
        RentalDto rentalDto = libraryMapper.mapToRentalDto(rental);

        //Then
        assertEquals(1, rentalDto.getRentalId());
        assertEquals("Addams", rentalDto.getBorrowingReader().getSurname());
        assertEquals(rentalDate, rentalDto.getRentalDate());
        assertEquals(returnDate, rentalDto.getReturnDate());
        assertEquals(0, rentalDto.getBookPiecesIds().size());
    }

    @Test
    public void testMapToRentalDto_WithPieces() {
        //Given
        Reader reader = new Reader(
                1, "Mark", "Addams", new Date(), new ArrayList<>());

        BookPiece bookPiece = new BookPiece(Status.AVAILABLE);
        bookPieceDao.save(bookPiece);
        int pieceId = bookPiece.getPieceId();

        Date rentalDate = new Date();
        Date returnDate = new Date();
        Rental rental = new Rental(13, reader, rentalDate, returnDate, Collections.singletonList(bookPiece));

        //When
        RentalDto rentalDto = libraryMapper.mapToRentalDto(rental);

        //Then
        assertEquals(13, rentalDto.getRentalId());
        assertEquals("Addams", rentalDto.getBorrowingReader().getSurname());
        assertEquals(rentalDate, rentalDto.getRentalDate());
        assertEquals(returnDate, rentalDto.getReturnDate());
        assertEquals(1, rentalDto.getBookPiecesIds().size());
        assertEquals(pieceId, rentalDto.getBookPiecesIds().get(0).intValue());

        //CleanUp
        bookPieceDao.deleteById(pieceId);
    }

}