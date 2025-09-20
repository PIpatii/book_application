package mate.academy.application.service;

import java.util.List;
import mate.academy.application.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
