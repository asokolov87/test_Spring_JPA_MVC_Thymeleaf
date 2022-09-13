package ru.sokolov.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sokolov.models.Book;
import ru.sokolov.models.Person;
import ru.sokolov.repositories.PersonRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person findOne(int id){
        Optional<Person> foundPerson = personRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save (Person person){
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id); //устанавливаем id, jpa поймет что надо бновить запись с таким id
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        personRepository.deleteById(id);
    }

    public List<Book> getBooksByPerson (int id){
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()){
            //подгрузить книги из базы если не обращаемся к ним
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getDate_of_taken().getTime() - new Date().getTime());
                // 864000000 милисикунд = 10 дней
                if (diffInMillies > 864000000)
                    book.setExpired(true);
            });
            return person.get().getBooks();
        }
        else
            return Collections.emptyList();
    }

    public Optional <Person> getPersonByFullName (String name){
        return Optional.ofNullable(personRepository.findByName(name));
    }
}
