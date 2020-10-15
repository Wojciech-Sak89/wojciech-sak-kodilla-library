package com.crud.kodillalibrary.service;

import com.crud.kodillalibrary.dao.BookPieceDao;
import com.crud.kodillalibrary.dao.BookTitleDao;
import com.crud.kodillalibrary.dao.ReaderDao;
import com.crud.kodillalibrary.dao.RentalDao;
import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.Reader;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.exception.BookPieceNotFoundException;
import com.crud.kodillalibrary.exception.BookTitleNotFoundException;
import com.crud.kodillalibrary.exception.NoAvailablePiecesException;
import com.crud.kodillalibrary.exception.ReaderNotFoundException;
import com.crud.kodillalibrary.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DbService {
    @Autowired
    BookPieceDao bookPieceDao;

    @Autowired
    BookTitleDao bookTitleDao;

    @Autowired
    ReaderDao readerDao;

    @Autowired
    RentalDao rentalDao;

    public Reader saveReader(Reader reader) {
        return readerDao.save(reader);
    }

    public BookTitle saveBookTitle(BookTitle bookTitle) {
        return bookTitleDao.save(bookTitle);
    }

    public BookPiece saveBookPiece(BookPiece bookPiece) {
        return bookPieceDao.save(bookPiece);
    }

    public Rental saveRental(Rental rental) {
        return rentalDao.save(rental);
    }

    public Optional<BookTitle> getBookByTitleAndAuthorName(String title, String authorName) {
        return bookTitleDao.findBookTitleByTitleAndAuthorName(title, authorName);
    }

    public Optional<Reader> findReaderById(int id) {
        return readerDao.findByReaderId(id);
    }

    public Optional<BookPiece> findPieceById(int id) {
        return bookPieceDao.findByPieceId(id);
    }

    public Optional<Rental> findRentalById(int id) {
        return rentalDao.findById(id);
    }

    public Rental processRent(int readerId, String title, String author) throws ReaderNotFoundException, BookTitleNotFoundException, NoAvailablePiecesException {
        Reader reader = findReaderById(readerId).orElseThrow(() -> new ReaderNotFoundException(readerId));

        BookTitle bookTitle = getBookByTitleAndAuthorName(title, author)
                .orElseThrow(() -> new BookTitleNotFoundException(title, author));

        List<BookPiece> availablePieces = bookTitle.getBookPieces().stream()
                .filter(bookPiece -> bookPiece.getStatus().equals(Status.AVAILABLE))
                .collect(Collectors.toList());

        if (availablePieces.size() == 0) throw new NoAvailablePiecesException();

        BookPiece pieceToRent = availablePieces.get(0);
        pieceToRent.setStatus(Status.IN_CIRCULATION);
        saveBookPiece(pieceToRent);

        Rental rental = new Rental(new Date(), null);
        rental.setBorrowingReader(reader);
        rental.getBookPieceList().add(pieceToRent);
        pieceToRent.getRentalList().add(rental);

        return rental;
    }

    public Rental processBookReturn(int bookPieceId) throws BookPieceNotFoundException {
        BookPiece piece = findPieceById(bookPieceId).orElseThrow(() -> new BookPieceNotFoundException(bookPieceId));
        piece.setStatus(Status.AVAILABLE);
        saveBookPiece(piece);

        Rental rental = piece.getRentalList().get(piece.getRentalList().size() - 1);

        boolean allBooksReturned = rental.getBookPieceList().stream()
                .noneMatch(bookPiece -> bookPiece.getStatus() != Status.AVAILABLE);

        if (allBooksReturned) rental.setReturnDate(new Date());
        return rental;
    }

    public BookPiece processChangingPieceStatus(int pieceId, Status status) throws BookPieceNotFoundException {
        BookPiece bookPiece =
                findPieceById(pieceId).orElseThrow(() -> new BookPieceNotFoundException(pieceId));
        bookPiece.setStatus(status);
        return bookPiece;
    }
}
