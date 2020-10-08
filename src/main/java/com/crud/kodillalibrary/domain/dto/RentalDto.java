package com.crud.kodillalibrary.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class RentalDto {
    private final int rentalId;
    private final ReaderDto borrowingReader;
    private final Date rentalDate;
    private final Date returnDate;
    private final List<Integer> bookPiecesIds;
}
