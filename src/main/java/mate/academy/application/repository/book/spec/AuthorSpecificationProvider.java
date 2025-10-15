package mate.academy.application.repository.book.spec;

import java.util.Arrays;
import mate.academy.application.model.Book;
import mate.academy.application.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR_KEY = "author";

    @Override
    public String getKey() {
        return AUTHOR_KEY;
    }

    public Specification<Book> getSpecification(String[] authors) {
        return ((root, query, criteriaBuilder) ->
                root.get(AUTHOR_KEY)
                .in(Arrays.stream(authors)
                .toArray()));
    }
}
