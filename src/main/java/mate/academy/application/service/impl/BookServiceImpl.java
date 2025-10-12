package mate.academy.application.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.BookDto;
import mate.academy.application.dto.BookSearchParametersDto;
import mate.academy.application.dto.CreateBookRequestDto;
import mate.academy.application.mapper.BookMapper;
import mate.academy.application.model.Book;
import mate.academy.application.repository.BookRepository;
import mate.academy.application.repository.book.BookSpecificationBuilder;
import mate.academy.application.service.BookService;
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
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
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
