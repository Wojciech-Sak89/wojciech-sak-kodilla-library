package com.crud.kodillalibrary.mapper;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.domain.dao.RentalDao;
import com.crud.kodillalibrary.domain.dto.BookPieceDto;
import com.crud.kodillalibrary.domain.dto.BookTitleDto;
import com.crud.kodillalibrary.domain.util.Status;
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
public class BookPieceMapperTest {
    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private RentalDao rentalDao;

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
}