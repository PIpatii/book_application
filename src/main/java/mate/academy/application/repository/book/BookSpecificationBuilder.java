package mate.academy.application.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.BookSearchParametersDto;
import mate.academy.application.model.Book;
import mate.academy.application.repository.SpecificationBuilder;
import mate.academy.application.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
    private static final String TITLE_KEY = "title";

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> specification = Specification.not(null);
        if (searchParametersDto.title() != null && searchParametersDto.title().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParametersDto.title()));
        }
        if (searchParametersDto.author() != null && searchParametersDto.author().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParametersDto.author()));

        }
        if (searchParametersDto.isbn() != null && searchParametersDto.isbn().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParametersDto.isbn()));
        }
        return specification;
    }
}
