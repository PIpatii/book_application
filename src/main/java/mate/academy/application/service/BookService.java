package mate.academy.application.service;

import java.util.List;
import mate.academy.application.dto.BookDto;
import mate.academy.application.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(long id);
}
