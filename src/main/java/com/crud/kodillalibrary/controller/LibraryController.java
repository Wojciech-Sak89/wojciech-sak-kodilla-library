package com.crud.kodillalibrary.controller;

import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.dto.BookPieceDto;
import com.crud.kodillalibrary.domain.dto.BookTitleDto;
import com.crud.kodillalibrary.domain.dto.ReaderDto;
import com.crud.kodillalibrary.domain.dto.RentalDto;
import com.crud.kodillalibrary.domain.util.Status;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {

/*
Oprócz zamodelowania tabel będzie potrzebne również utworzenie usług REST. Podstawowe usługi to:

//dodanie czytelnika,
//dodanie tytułu,
//dodanie egzemplarza,
//zmiana statusu egzemplarza,
//sprawdzenie ilości egzemplarzy danego tytułu dostępnych do wypożyczenia,
//wypożyczenie książki,
//zwrot książki

//!!! Dla uproszczenia możemy przyjąć, że jeśli czytelnik zgubi książkę, to w momencie zapłacenia kosztu książki następuje zwrot książki oraz zmiana statusu egzemplarza. !!!
*/



    @RequestMapping(method = RequestMethod.POST, value = "addReader", consumes = APPLICATION_JSON_VALUE)
    public ReaderDto addReader(@RequestBody ReaderDto readerDto) {
        return new ReaderDto(1, "name", "surname", new Date(), new ArrayList<>());
    }

    @RequestMapping(method = RequestMethod.POST, value = "addTitle")
    public BookTitleDto addTitle(@RequestBody BookTitleDto bookTitleDto) {
        return new BookTitleDto(1, "title", "name", 1999, new ArrayList<>());
    }

    @RequestMapping(method = RequestMethod.POST, value = "addPiece")
    public BookPieceDto addPiece(@RequestBody BookPieceDto bookPieceDto) {
        BookTitleDto bookTitleDto = new BookTitleDto(1, "title", "name", 1999, new ArrayList<>());
        return new BookPieceDto(1, bookTitleDto, Status.AVAILABLE, new ArrayList<>());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "changePieceStatus")
    public BookPieceDto changePieceStatus(@RequestParam int pieceId, @RequestParam("status") Status status) {
        BookTitleDto bookTitleDto = new BookTitleDto(1, "title", "name", 1999, new ArrayList<>());
        return new BookPieceDto(1, bookTitleDto, status, new ArrayList<>());
    }

    @RequestMapping(method = RequestMethod.GET, value = "getAvailablePieces")
    public int getAmountOfAvailablePieces(@RequestParam String title, @RequestParam String author) {
        return 15;
    }

    @RequestMapping(method = RequestMethod.POST, value = "rent")
    public RentalDto rent(@RequestParam int readerId, @RequestParam String title, @RequestParam String author) {
        ReaderDto readerDto = new ReaderDto(1, "name", "surname", new Date(), new ArrayList<>());
        return new RentalDto(1, readerDto, new Date(), new Date(), new ArrayList<>());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "return")
    public RentalDto returnBook(@RequestParam int bookPieceId) {
        ReaderDto readerDto = new ReaderDto(1, "name", "surname", new Date(), new ArrayList<>());
        return new RentalDto(1, readerDto, new Date(), new Date(), new ArrayList<>());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "payLossFee")
    public RentalDto payLossFee(@RequestParam int bookPieceId, @RequestParam boolean paid) {
        //w momencie zapłacenia kosztu książki następuje zwrot książki oraz zmiana statusu egzemplarza
        ReaderDto readerDto = new ReaderDto(1, "name", "surname", new Date(), new ArrayList<>());
        return new RentalDto(1, readerDto, new Date(), new Date(), new ArrayList<>());
    }
}
