package com.crud.kodillalibrary.mapper;

import com.crud.kodillalibrary.domain.BookPiece;
import com.crud.kodillalibrary.domain.BookTitle;
import com.crud.kodillalibrary.domain.Reader;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.domain.dao.BookPieceDao;
import com.crud.kodillalibrary.domain.dao.RentalDao;
import com.crud.kodillalibrary.domain.dto.BookPieceDto;
import com.crud.kodillalibrary.domain.dto.BookTitleDto;
import com.crud.kodillalibrary.domain.dto.ReaderDto;
import com.crud.kodillalibrary.domain.dto.RentalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {
    @Autowired
    private BookPieceDao bookPieceDao;

    @Autowired
    private RentalDao rentalDao;

    public BookTitle mapToBookTitle(BookTitleDto bookTitleDto) {
        return new BookTitle(
                bookTitleDto.getBookId(),
                bookTitleDto.getTitle(),
                bookTitleDto.getAuthorName(),
                bookTitleDto.getPublishmentYear(),
                mapToBookPieces(bookTitleDto.getPiecesIds())
        );
    }

    public BookTitleDto mapToBookTitleDto(BookTitle bookTitle) {
        return new BookTitleDto(
                bookTitle.getBookId(),
                bookTitle.getTitle(),
                bookTitle.getAuthorName(),
                bookTitle.getPublishmentYear(),
                mapToBookPiecesIds(bookTitle.getBookPieces())
        );
    }

    public Reader mapToReader(ReaderDto readerDto) {
        return new Reader(
                readerDto.getReaderId(),
                readerDto.getName(),
                readerDto.getSurname(),
                readerDto.getAccountCreationDate(),
                mapToRentals(readerDto.getRentalsIds())
        );
    }

    public ReaderDto mapToReaderDto(Reader reader) {
        return new ReaderDto(
                reader.getReaderId(),
                reader.getName(),
                reader.getSurname(),
                reader.getAccountCreationDate(),
                mapRentalsIds(reader.getRentals())
        );
    }

    public BookPiece mapToBookPiece (BookPieceDto bookPieceDto) {
        return new BookPiece(
                bookPieceDto.getPieceId(),
                mapToBookTitle(bookPieceDto.getBookTitle()),
                bookPieceDto.getStatus(),
                mapToRentals(bookPieceDto.getRentalList())
        );
    }

    public BookPieceDto mapToBookPieceDto (BookPiece bookPiece) {
        return new BookPieceDto(
                bookPiece.getPieceId(),
                mapToBookTitleDto(bookPiece.getBookTitle()),
                bookPiece.getStatus(),
                mapRentalsIds(bookPiece.getRentalList())
        );
    }

    public Rental mapToRental(RentalDto rentalDto) {
        return new Rental(
                rentalDto.getRentalId(),
                mapToReader(rentalDto.getBorrowingReader()),
                rentalDto.getRentalDate(),
                rentalDto.getReturnDate(),
                mapToBookPieces(rentalDto.getBookPiecesIds())
        );
    }

    public RentalDto mapToRentalDto(Rental rental) {
        return new RentalDto(
                rental.getRentalId(),
                mapToReaderDto(rental.getBorrowingReader()),
                rental.getRentalDate(),
                rental.getReturnDate(),
                mapToBookPiecesIds(rental.getBookPieceList())
        );
    }


    public List<Rental> mapToRentals(List<Integer> rentalIds) {
        return rentalIds.stream()
                .map(rentalId -> rentalDao.findById(rentalId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private List<Integer> mapRentalsIds(List<Rental> rentals) {
        return rentals.stream()
                .map(Rental::getRentalId)
                .collect(Collectors.toList());
    }

    public List<BookPiece> mapToBookPieces(List<Integer> bookPiecesIds) {
        return bookPiecesIds.stream()
                .map(pieceId -> bookPieceDao.findById(pieceId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
    private List<Integer> mapToBookPiecesIds(List<BookPiece> bookPieces) {
        return bookPieces.stream()
                .map(BookPiece::getPieceId)
                .collect(Collectors.toList());
    }

//    private List<BookPiece> mapIdsToBookPieces(List<Integer> bookPiecesIds) throws BookPieceNotFoundException { //czy liepiej taki mapper z rzucaniem wyjątku???
//        List<BookPiece> bookPieceList = new ArrayList<>();
//
//        for (Integer id : bookPiecesIds) {
//            Optional<BookPiece> bookPieceOpt = bookPieceDao.findById(id);
//            if (bookPieceOpt.isPresent()) {
//                bookPieceList.add(bookPieceOpt.get());
//            } else {
//                throw new BookPieceNotFoundException();
//            }
//        }
//
//        return bookPieceList;
//    }
}
