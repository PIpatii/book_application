package mate.academy.application.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.application.exception.DataProcessingException;
import mate.academy.application.model.Book;
import mate.academy.application.repository.SpecificationProvider;
import mate.academy.application.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationsProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationsProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new DataProcessingException(
                "Book Specification Provider Not Found"));
    }
}
