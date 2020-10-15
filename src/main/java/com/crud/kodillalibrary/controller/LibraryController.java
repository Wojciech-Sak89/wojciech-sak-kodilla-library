package com.crud.kodillalibrary.controller;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.domain.dto.BookPieceDto;
import com.crud.kodillalibrary.domain.dto.BookTitleDto;
import com.crud.kodillalibrary.domain.dto.ReaderDto;
import com.crud.kodillalibrary.domain.dto.RentalDto;
import com.crud.kodillalibrary.exception.*;
import com.crud.kodillalibrary.mapper.LibraryMapper;
import com.crud.kodillalibrary.service.DbService;
import com.crud.kodillalibrary.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        BookPiece bookPiece = service.processChangingPieceStatus(pieceId, status);
        return mapper.mapToBookPieceDto(service.saveBookPiece(bookPiece));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getAvailablePieces")
    public long getAmountOfAvailablePieces(@RequestParam String title, @RequestParam String author)
            throws BookTitleNotFoundException {

        BookTitle bookTitle = service
                .getBookByTitleAndAuthorName(title, author)
                .orElseThrow(() -> new BookTitleNotFoundException(title, author));

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
        Rental rental = service.processRent(readerId, title, author);
        return mapper.mapToRentalDto(service.saveRental(rental));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "return")
    public RentalDto returnBook(@RequestParam int bookPieceId)
            throws BookPieceNotFoundException {
        Rental rental = service.processBookReturn(bookPieceId);
        return mapper.mapToRentalDto(service.saveRental(rental));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "payLossFee")
    public RentalDto payLossFee(@RequestParam int bookPieceId, @RequestParam boolean paid)
            throws FailedToPayFeeException, BookPieceNotFoundException {
        if (paid) {
            return returnBook(bookPieceId);
        } else {
            throw new FailedToPayFeeException("Payment unsuccessful.");
        }
    }
}
