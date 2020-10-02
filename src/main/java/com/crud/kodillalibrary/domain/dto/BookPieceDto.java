package com.crud.kodillalibrary.domain.dto;

import com.crud.kodillalibrary.domain.util.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookPieceDto {
    private int pieceId;
    private BookTitleDto bookTitle;
    private Status status;
    private List<Integer> rentalList;
}
