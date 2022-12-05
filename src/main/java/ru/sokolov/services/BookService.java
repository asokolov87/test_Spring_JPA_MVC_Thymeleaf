package ru.sokolov.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sokolov.models.Book;
import ru.sokolov.models.Person;
import ru.sokolov.repositories.BookRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //вывод всех книг с сортировкой и без
    public List<Book> findAll(boolean sort_by_year){
        if (sort_by_year)
            return bookRepository.findAll(Sort.by("date"));
        else
            return bookRepository.findAll();
    }

    //вывод всех книг с пагинацией. Перегруженный метод
    public List<Book> findAll(Integer page, Integer itemPerPage, boolean sort_by_year){
        if (sort_by_year)
            //если требуется сортировка
            return bookRepository.findAll(PageRequest.of(page, itemPerPage, Sort.by("date"))).getContent();
        else
            // без сортировки
            return bookRepository.findAll(PageRequest.of(page, itemPerPage)).getContent();
    }

    public Optional<Book> findOne(int id){
        return bookRepository.findById(id);
    }

    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook){
        Book bookToBeUpdated = bookRepository.findById(id).get();

        // добавляем по сути новую книгу (которая не находится в Persistence context), поэтому нужен save() не load()
        updateBook.setId(id);
        updateBook.setOwner(bookToBeUpdated.getOwner()); //переназначить владельца чтобы не терялась связь при обновлении т.к. книга пришедшая с формы владелец null
        bookRepository.save(updateBook); // книга не отслеживалась в Persistence context, пришла с формы
    }

    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }

    //назначает человека кто взял книгу и когда взял
    @Transactional
    public void assign(int book_id, Person selectedPerson){
        bookRepository.findById(book_id).ifPresent(book -> {
            book.setOwner(selectedPerson);
            book.setDate_of_taken(new Date());
        });
    }

    //освобождение книги
    @Transactional
    public void release(int book_id){
        bookRepository.findById(book_id).ifPresent(book -> { //книга уже лижит в Persistence context
            book.setOwner(null);
            book.setDate_of_taken(null);
        });
    }

    public Optional<Person> getBooksOwner (int id){
        //Hibernate.initialize() ненужен т.к. владелец загружается не Lazy
        return bookRepository.findById(id).map(value -> Optional.ofNullable(value.getOwner())).orElse(null);
    }

    public List<Book> search (String name){
        return bookRepository.findByNameStartingWith(name);
    }

    public List<Book> searchByAuthorOrName (String author, String name){return bookRepository.findByAuthorStartingWithOrNameStartingWith(author, name);}

}
