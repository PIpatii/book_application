package mate.academy.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.BookDto;
import mate.academy.application.dto.BookSearchParametersDto;
import mate.academy.application.dto.CreateBookRequestDto;
import mate.academy.application.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book store", description = "Endpoints for Book store")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "get all books", description = "get a list of all books")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/search")
    @Operation(summary = "search", description = "get a list of all books using some parameters")
    public List<BookDto> search(BookSearchParametersDto params) {
        return bookService.search(params);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get a book by id", description = "get a book using id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create a new book", description = "create a new book")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "update a book", description = "update a book using id")
    public BookDto updateBookById(@RequestBody @Valid CreateBookRequestDto bookDto,
                                  @PathVariable Long id) {
        return bookService.updateById(bookDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete a book", description = "delete a book using id")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

}
