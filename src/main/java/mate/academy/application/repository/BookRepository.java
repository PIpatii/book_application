package mate.academy.application.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.application.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(long id);
}
