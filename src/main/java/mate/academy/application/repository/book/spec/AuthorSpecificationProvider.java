package mate.academy.application.repository.book.spec;

import java.util.Arrays;
import mate.academy.application.model.Book;
import mate.academy.application.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(String[] authors) {
        return ((root, query, criteriaBuilder) ->
                root.get("author")
                .in(Arrays.stream(authors)
                .toArray()));
    }
}
