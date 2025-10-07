package mate.academy.application.repository.book.spec;

import java.util.Arrays;
import mate.academy.application.model.Book;
import mate.academy.application.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return "title";
    }

    public Specification<Book> getSpecification(String[] titles) {
        return ((root, query, criteriaBuilder) ->
                root.get("title")
                .in(Arrays.stream(titles)
                .toArray()));
    }
}
