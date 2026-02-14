package mate.academy.application.repository;

import mate.academy.application.model.Book;
import mate.academy.application.repository.book.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false",
        "spring.flyway.enabled=false"
})
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = "classpath:database/book/add-three-elements-to-each-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/remove-all-from-each-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_correctData_success() {
        Set<Book> books = bookRepository.findAllByCategories_Id(1L);
        assertEquals(1, books.size());
    }

    @Test
    @Sql(scripts = "classpath:database/book/add-three-elements-to-each-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/remove-all-from-each-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findBookById_correctData_success() {
        Book excpectedBook = new Book();
        excpectedBook.setId(1L);
        excpectedBook.setAuthor("Taras");
        excpectedBook.setTitle("Kobzar");
        excpectedBook.setPrice(BigDecimal.valueOf(100));
        excpectedBook.setDescription("book");
        excpectedBook.setCoverImage("coverImage");
        excpectedBook.setIsbn("1331421");

        Book actualBook = bookRepository.findBookById(1L);
        assertEquals(excpectedBook.getId(), actualBook.getId());
    }
}


