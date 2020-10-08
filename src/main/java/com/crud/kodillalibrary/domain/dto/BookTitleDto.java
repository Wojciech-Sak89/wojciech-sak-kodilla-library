package com.crud.kodillalibrary.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BookTitleDto {
    private final int bookId;
    private final String title;
    private final String authorName;
    private final int publishmentYear;
    private final List<Integer> piecesIds;


}
