package com.crud.kodillalibrary.mapper;

import com.crud.kodillalibrary.domain.Reader;
import com.crud.kodillalibrary.domain.Rental;
import com.crud.kodillalibrary.dao.RentalDao;
import com.crud.kodillalibrary.domain.dto.ReaderDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReaderMapperTest {
    @Autowired
    private LibraryMapper libraryMapper;

    @Autowired
    private RentalDao rentalDao;

    @Test
    public void testMapToReader_ZeroRents() {
        //Given
        Date date = new Date();
        ReaderDto readerDto = new ReaderDto(1, "Mark", "Addams", date, new ArrayList<>());

        //When
        Reader reader = libraryMapper.mapToReader(readerDto);

        //Then
        assertEquals(1, reader.getReaderId());
        assertEquals("Mark", reader.getName());
        assertEquals("Addams", reader.getSurname());
        assertEquals(date, reader.getAccountCreationDate());
        assertEquals(0, reader.getRentals().size());
    }

    @Test
    public void testMapToReader_WithRents() {
        //Given
        Rental rental = new Rental(new Date(), new Date());
        rentalDao.save(rental);
        int id = rental.getRentalId();

        Date date = new Date();
        ReaderDto readerDto = new ReaderDto(1, "Mark", "Addams", date, Collections.singletonList(id));

        //When
        Reader reader = libraryMapper.mapToReader(readerDto);

        //Then
        assertEquals(1, reader.getReaderId());
        assertEquals("Mark", reader.getName());
        assertEquals("Addams", reader.getSurname());
        assertEquals(date, reader.getAccountCreationDate());
        assertEquals(1, reader.getRentals().size());
        assertEquals(id, reader.getRentals().get(0).getRentalId());

        //CleanUp
        rentalDao.deleteById(id);
    }

    @Test
    public void testMapToReaderDto_ZeroRents() {
        //Given
        Date date = new Date();
        Reader reader = new Reader(1, "Mark", "Addams", date, new ArrayList<>());

        //When
        ReaderDto readerDto = libraryMapper.mapToReaderDto(reader);

        //Then
        assertEquals(1, readerDto.getReaderId());
        assertEquals("Mark", readerDto.getName());
        assertEquals("Addams", readerDto.getSurname());
        assertEquals(date, readerDto.getAccountCreationDate());
        assertEquals(0, readerDto.getRentalsIds().size());
    }

    @Test
    public void testMapToReaderDto_WithRents() {
        //Given
        Rental rental = new Rental(new Date(), new Date());
        rentalDao.save(rental);
        int id = rental.getRentalId();

        Date date = new Date();
        Reader reader = new Reader(2, "Mark", "Addams", date, Collections.singletonList(rental));

        //When
        ReaderDto readerDto = libraryMapper.mapToReaderDto(reader);

        //Then
        assertEquals(2, readerDto.getReaderId());
        assertEquals("Mark", readerDto.getName());
        assertEquals("Addams", readerDto.getSurname());
        assertEquals(date, readerDto.getAccountCreationDate());
        assertEquals(1, readerDto.getRentalsIds().size());
        assertEquals(id, readerDto.getRentalsIds().get(0).intValue());

        //CleanUp
        rentalDao.deleteById(id);
    }
}
