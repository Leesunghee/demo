package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sunghee on 2016. 11. 7..
 */
public interface ReadingListRepository extends JpaRepository<Book, Long> {
    List<Book> findByReader(String reader);
}
