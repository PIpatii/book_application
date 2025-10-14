package mate.academy.application.repository.book.spec;

import java.util.Arrays;
import mate.academy.application.model.Book;
import mate.academy.application.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN_KEY = "isbn";

    @Override
    public String getKey() {
        return ISBN_KEY;
    }

    public Specification<Book> getSpecification(String[] isbns) {
        return ((root, query, criteriaBuilder) ->
                root.get(ISBN_KEY)
                .in(Arrays.stream(isbns)
                .toArray()));
    }
}
