package com.crud.kodillalibrary.domain;

import com.crud.kodillalibrary.domain.util.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "BOOK_PIECES")
public class BookPiece {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PIECE_ID", unique = true)
    private int pieceId;

    @ManyToOne
    @JoinColumn(name = "TITLE_ID")
    private BookTitle bookTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name = "JOIN_RENTAL_BOOKPIECE",
            joinColumns = {@JoinColumn(name = "PIECE_ID", referencedColumnName = "PIECE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "RENTAL_ID", referencedColumnName = "RENTAL_ID")}
    )
    private List<Rental> rentalList = new ArrayList<>();

    public BookPiece(Status status) {
        this.status = status;
    }



    public void setBookTitle(BookTitle bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
