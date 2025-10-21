package mate.academy.application.service;

import java.util.List;
import mate.academy.application.dto.BookDto;
import mate.academy.application.dto.BookSearchParametersDto;
import mate.academy.application.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    List<BookDto> search(BookSearchParametersDto params);

    BookDto getById(long id);

    BookDto updateById(CreateBookRequestDto bookDto, long id);

    void deleteById(long id);
}
