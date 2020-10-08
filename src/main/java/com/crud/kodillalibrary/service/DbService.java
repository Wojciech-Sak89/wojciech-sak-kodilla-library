package com.crud.kodillalibrary.service;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.Reader;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.domain.dao.BookPieceDao;
import com.crud.kodillalibrary.domain.dao.BookTitleDao;
import com.crud.kodillalibrary.domain.dao.ReaderDao;
import com.crud.kodillalibrary.domain.dao.RentalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
