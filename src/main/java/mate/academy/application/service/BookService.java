package mate.academy.application.service;

import java.util.List;
import java.util.Set;
import mate.academy.application.dto.book.BookDto;
import mate.academy.application.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.application.dto.book.BookSearchParametersDto;
import mate.academy.application.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    List<BookDto> search(BookSearchParametersDto params);

    BookDto getById(long id);

    BookDto updateById(CreateBookRequestDto bookDto, long id);

    Set<BookDtoWithoutCategoryIds> getBooksByCategoryId(long id);

    void deleteById(long id);
}
