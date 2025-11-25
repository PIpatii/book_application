package mate.academy.application.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.book.BookDto;
import mate.academy.application.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.application.dto.book.BookSearchParametersDto;
import mate.academy.application.dto.book.CreateBookRequestDto;
import mate.academy.application.mapper.BookMapper;
import mate.academy.application.model.Book;
import mate.academy.application.repository.book.BookRepository;
import mate.academy.application.repository.book.BookSpecificationBuilder;
import mate.academy.application.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        Page<Book> pages = bookRepository.findAll(pageable);
        return pages.map(bookMapper::toDto);
    }

    @Override
    public BookDto getById(long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book by id: " + id)));
    }

    @Override
    public BookDto updateById(CreateBookRequestDto updateBookDto, long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book by id: " + id));
        bookMapper.updateBookFromDto(updateBookDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Set<BookDtoWithoutCategoryIds> getBooksByCategoryId(long id) {
        return bookRepository.findAllByCategoryId(id).stream()
                .map(bookMapper::toDtoWithoutCategory)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        Specification<Book> specification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(specification).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
