package com.crud.kodillalibrary.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "BOOK_TITLES")
public class BookTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BOOK_ID", unique = true)
    private int bookId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String authorName;

    @Column(name = "PUBLISHED")
    private int publishmentYear;

    @OneToMany(
            targetEntity = BookPiece.class,
            mappedBy = "bookTitle",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<BookPiece> bookPieces = new ArrayList<>();

    public BookTitle(String title, String authorName, int publishmentYear) {
        this.title = title;
        this.authorName = authorName;
        this.publishmentYear = publishmentYear;
    }
}
