package com.crud.kodillalibrary.mapper;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.Reader;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.dao.BookPieceDao;
import com.crud.kodillalibrary.domain.dto.ReaderDto;
import com.crud.kodillalibrary.domain.dto.RentalDto;
import com.crud.kodillalibrary.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RentalMapperTest {
    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private BookPieceDao bookPieceDao;

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
