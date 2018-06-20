package com.example.firstapp.demo.validator;

import com.example.firstapp.demo.model.Book;
import com.example.firstapp.demo.repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class BookValidator implements Validator {

    @Autowired
    BooksRepository booksRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors err) {

        Book book = (Book) obj;

        Book bookByIsbn;

        if (book.getId() == null) {
            bookByIsbn = booksRepository.findFirstByIsbnEquals(book.getIsbn());
        } else {
            bookByIsbn = booksRepository.findFirstByIdIsNotAndIsbnEquals(book.getId(), book.getIsbn());
        }

        if (bookByIsbn != null) {
            // Текст сообщения для notUnique находится в файле resources/messages.properties
            err.rejectValue("isbn", "notUnique");
        }

        //ValidationUtils.rejectIfEmpty(err, "printYear", "empty");
        //ValidationUtils.rejectIfEmpty(err, "readAlready", "empty");


    }

}
