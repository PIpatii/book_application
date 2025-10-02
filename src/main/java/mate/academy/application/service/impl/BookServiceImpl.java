package mate.academy.application.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.BookDto;
import mate.academy.application.dto.CreateBookRequestDto;
import mate.academy.application.exception.EntityNotFoundException;
import mate.academy.application.mapper.BookMapper;
import mate.academy.application.model.Book;
import mate.academy.application.repository.BookRepository;
import mate.academy.application.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

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
        book.setAuthor(updateBookDto.getAuthor());
        book.setTitle(updateBookDto.getTitle());
        book.setPrice(updateBookDto.getPrice());
        book.setIsbn(updateBookDto.getIsbn());
        book.setDescription(updateBookDto.getDescription());
        book.setCoverImage(updateBookDto.getCoverImage());

        return bookMapper.toDto(bookRepository.save(book));

    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
