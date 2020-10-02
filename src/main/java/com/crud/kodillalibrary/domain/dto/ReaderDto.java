package com.crud.kodillalibrary.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReaderDto {
    private final int readerId;
    private final String name;
    private final String surname;
    private final Date accountCreationDate;
    private final List<Integer> rentalsIds;
}
