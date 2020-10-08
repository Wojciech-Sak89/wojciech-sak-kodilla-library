package com.crud.kodillalibrary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "RENTALS")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RENTAL_ID", unique = true)
    private int rentalId;

    @ManyToOne
    @JoinColumn(name = "READER_ID")
    private Reader borrowingReader;

    @NotNull
    @Column(name = "RENTED")
    private Date rentalDate;

    @Column(name = "RETURNED")
    private Date returnDate;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "rentalList", fetch = FetchType.EAGER)
    private List<BookPiece> bookPieceList = new ArrayList<>();

    public Rental(@NotNull Date rentalDate, Date returnDate) {
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }

    public void setBorrowingReader(Reader borrowingReader) {
        this.borrowingReader = borrowingReader;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
