package com.crud.kodillalibrary.controller;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.Reader;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.domain.dto.BookPieceDto;
import com.crud.kodillalibrary.domain.dto.BookTitleDto;
import com.crud.kodillalibrary.domain.dto.ReaderDto;
import com.crud.kodillalibrary.domain.dto.RentalDto;
import com.crud.kodillalibrary.domain.util.*;
import com.crud.kodillalibrary.mapper.LibraryMapper;
import com.crud.kodillalibrary.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {

    @Autowired
    DbService service;

    @Autowired
    LibraryMapper mapper;

    @RequestMapping(method = RequestMethod.POST, value = "addReader", consumes = APPLICATION_JSON_VALUE)
    public ReaderDto addReader(@RequestBody ReaderDto readerDto) {

        return mapper.mapToReaderDto(service.saveReader(mapper.mapToReader(readerDto)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "addTitle")
    public BookTitleDto addTitle(@RequestBody BookTitleDto bookTitleDto) {

        return mapper.mapToBookTitleDto(service.saveBookTitle(mapper.mapToBookTitle(bookTitleDto)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "addPiece")
    public BookPieceDto addPiece(@RequestBody BookPieceDto bookPieceDto) {

        return mapper.mapToBookPieceDto(service.saveBookPiece(mapper.mapToBookPiece(bookPieceDto)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "changePieceStatus")
    public BookPieceDto changePieceStatus(@RequestParam int pieceId, @RequestParam("status") Status status)
            throws BookPieceNotFoundException {

        BookPiece bookPiece =
                service.findPieceById(pieceId).orElseThrow(BookPieceNotFoundException::new);
        bookPiece.setStatus(status);

        return mapper.mapToBookPieceDto(service.saveBookPiece(bookPiece));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getAvailablePieces")
    public long getAmountOfAvailablePieces(@RequestParam String title, @RequestParam String author)
            throws BookTitleNotFoundException {

        BookTitle bookTitle = service
                .getBookByTitleAndAuthorName(title, author)
                .orElseThrow(BookTitleNotFoundException::new);

        return bookTitle.getBookPieces().stream()
                .filter(bookPiece -> bookPiece.getStatus().equals(Status.AVAILABLE))
                .count();
    }

    @RequestMapping(method = RequestMethod.POST, value = "rent")
    public RentalDto rent(
            @RequestParam int readerId,
            @RequestParam String title,
            @RequestParam String author)
            throws ReaderNotFoundException, BookTitleNotFoundException, NoAvailablePiecesException {

        Reader reader = service.findReaderById(readerId).orElseThrow(ReaderNotFoundException::new);

        BookTitle bookTitle = service
                .getBookByTitleAndAuthorName(title, author)
                .orElseThrow(BookTitleNotFoundException::new);

        List<BookPiece> availablePieces = bookTitle.getBookPieces().stream()
                .filter(bookPiece -> bookPiece.getStatus().equals(Status.AVAILABLE))
                .collect(Collectors.toList());

        if (availablePieces.size() == 0) throw new NoAvailablePiecesException();

        BookPiece pieceToRent = availablePieces.get(0);
        pieceToRent.setStatus(Status.IN_CIRCULATION);
        service.saveBookPiece(pieceToRent);

        Rental rental = new Rental(new Date(), null);
        rental.setBorrowingReader(reader);
        rental.getBookPieceList().add(pieceToRent);
        pieceToRent.getRentalList().add(rental);

        return mapper.mapToRentalDto(service.saveRental(rental));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "return")
    public RentalDto returnBook(@RequestParam int bookPieceId)
            throws BookPieceNotFoundException {

        BookPiece piece = service.findPieceById(bookPieceId).orElseThrow(BookPieceNotFoundException::new);
        piece.setStatus(Status.AVAILABLE);
        service.saveBookPiece(piece);

        Rental rental = piece.getRentalList().get(piece.getRentalList().size()-1);

        boolean allBooksReturned = rental.getBookPieceList().stream()
                .noneMatch(bookPiece -> bookPiece.getStatus() != Status.AVAILABLE);

        if (allBooksReturned) rental.setReturnDate(new Date());

        return mapper.mapToRentalDto(service.saveRental(rental));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "payLossFee")
    public RentalDto payLossFee(@RequestParam int bookPieceId, @RequestParam boolean paid)
            throws FaildToPayFeeException, BookPieceNotFoundException {

        if (paid) {
            return returnBook(bookPieceId);
        } else {
            throw new FaildToPayFeeException("Payment unsuccessful.");
        }
    }
}
