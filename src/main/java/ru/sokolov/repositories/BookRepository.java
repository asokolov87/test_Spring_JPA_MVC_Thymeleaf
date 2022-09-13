package ru.sokolov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sokolov.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

     @Query("select b from Book  b where b.name like %?1")
    List<Book> findLikeName (String name);

     //findBy*название столбца*StartingWith - важно! поиск по столбцу
    List <Book> findByNameStartingWith(String title);

    List <Book> findByAuthorStartingWithOrNameStartingWith(String author, String name);

}
