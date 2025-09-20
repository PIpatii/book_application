package mate.academy.application;

import java.math.BigDecimal;
import mate.academy.application.model.Book;
import mate.academy.application.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setAuthor("Conan");
            book.setIsbn("978-3-540-119010");
            book.setTitle("sherlok");
            book.setDescription("about Sherlok");
            book.setPrice(BigDecimal.valueOf(1200));
            book.setCoverImage("cover.jpg");

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }

}
