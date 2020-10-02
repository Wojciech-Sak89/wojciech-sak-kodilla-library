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
@Entity(name = "READERS")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "READER_ID", unique = true)
    private int readerId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @NotNull
    @Column(name = "ACCOUNT_CREATED")
    private Date accountCreationDate;

    @OneToMany(
            targetEntity = Rental.class,
            mappedBy = "borrowingReader",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Rental> rentals = new ArrayList<>();

    public Reader(String name, String surname, @NotNull Date accountCreationDate) {
        this.name = name;
        this.surname = surname;
        this.accountCreationDate = accountCreationDate;
    }
}
