package ru.sokolov.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;
    @Column(name = "author")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String author;
    @Column(name = "date")
    @Min(value = 0, message = "Age should be greater than 0")
    private int date;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id") //owner side
    private Person owner;

    @Column(name = "date_of_taken")
    @Temporal(TemporalType.TIMESTAMP)                //Даты в spring
    private Date date_of_taken;

    @Transient
    private boolean expired; // Hibernate не будет замечать этого поля, что нам и нужно. По-умолчанию false.

    public Book() {
    }

    public Book(String name, String author, int date) {
        this.name = name;
        this.author = author;
        this.date = date;
    }

    public int getId() {return id;    }
    public void setId(int id) {        this.id = id;    }
    public String getName() {        return name;    }
    public void setName(String name) {        this.name = name;    }
    public String getAuthor() {        return author;    }
    public void setAuthor(String author) {        this.author = author;    }
    public int getDate() {        return date;    }
    public void setDate(int date) {        this.date = date;    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getDate_of_taken() {
        return date_of_taken;
    }

    public void setDate_of_taken(Date date_of_taken) {
        this.date_of_taken = date_of_taken;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", date=" + date +
                '}';
    }
}
