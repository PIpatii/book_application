package mate.academy.application.repository.book;

import java.util.Set;
import mate.academy.application.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Set<Book> findAllByCategoryId(Long categoryId);
}
