package ru.sokolov.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sokolov.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByName(String name); // поиск по столбцу name, важно название метода findByНазваниеСтолбца
}
