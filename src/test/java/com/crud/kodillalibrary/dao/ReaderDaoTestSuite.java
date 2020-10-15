package com.crud.kodillalibrary.dao;

import com.crud.kodillalibrary.domain.Reader;
import com.crud.kodillalibrary.domain.Rental;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReaderDaoTestSuite {
    @Autowired
    private ReaderDao readerDao;

    @Test
    public void testReaderDaoSave() {
        //Given
        Reader reader = new Reader("Donald", "Duck", new Date());

        //When
        readerDao.save(reader);

        //Then
        int id = reader.getReaderId();

        try {
            Optional<Reader> currentReader = readerDao.findById(id);
            Assert.assertTrue(currentReader.isPresent());

        //CleanUp
        } finally {
            readerDao.deleteById(id);
        }
    }

    @Test
    public void testReaderDaoSaveWithRentals() {
        //Given
        Reader reader1 = new Reader("Donald", "Duck", new Date());
        Reader reader2 = new Reader("Melania", "Trump", new Date());

        Rental rental1 = new Rental(new Date(), new Date((new Date().getTime()) + 86400000 * 5));
        Rental rental2 = new Rental(new Date(), new Date((new Date().getTime()) + 86400000 * 6));
        Rental rental3 = new Rental(new Date(), new Date((new Date().getTime()) + 86400000 * 7));

        reader1.getRentals().add(rental1);
        Collections.addAll(reader2.getRentals(), rental2, rental3);

        rental1.setBorrowingReader(reader1);
        rental2.setBorrowingReader(reader2);
        rental3.setBorrowingReader(reader2);

        //When
        readerDao.save(reader1);
        readerDao.save(reader2);

        //Then
        int reader1Id = reader1.getReaderId();
        int reader2Id = reader2.getReaderId();

        try {
            Assert.assertNotEquals(0, reader1Id);
            Assert.assertNotEquals(0, reader2Id);

        //CleanUp
        } finally {
            readerDao.deleteById(reader1Id);
            readerDao.deleteById(reader2Id);
        }
    }

}