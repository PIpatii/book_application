package mate.academy.application.service;

import mate.academy.application.dto.book.BookDto;
import mate.academy.application.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.application.dto.book.BookSearchParametersDto;
import mate.academy.application.dto.book.CreateBookRequestDto;
import mate.academy.application.mapper.BookMapper;
import mate.academy.application.model.Book;
import mate.academy.application.model.Category;
import mate.academy.application.repository.SpecificationProvider;
import mate.academy.application.repository.book.BookRepository;
import mate.academy.application.repository.book.BookSpecificationBuilder;
import mate.academy.application.repository.book.BookSpecificationProviderManager;
import mate.academy.application.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder  bookSpecificationBuilder;

    @Test
    @DisplayName("Save a book")
    public void save_correctData_savedSuccessfully(){
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Taras");
        createBookRequestDto.setTitle("Kobzar");
        createBookRequestDto.setIsbn("12412151");
        createBookRequestDto.setPrice(BigDecimal.valueOf(100));
        createBookRequestDto.setDescription("description");
        createBookRequestDto.setCoverImage("coverImage");

        Book book = new Book();
        book.setAuthor(createBookRequestDto.getAuthor());
        book.setTitle(createBookRequestDto.getTitle());
        book.setIsbn(createBookRequestDto.getIsbn());
        book.setPrice(createBookRequestDto.getPrice());
        book.setDescription(createBookRequestDto.getDescription());
        book.setCoverImage(createBookRequestDto.getCoverImage());

        BookDto bookDto = new BookDto();
        bookDto.setAuthor(createBookRequestDto.getAuthor());
        bookDto.setTitle(createBookRequestDto.getTitle());
        bookDto.setIsbn(createBookRequestDto.getIsbn());
        bookDto.setPrice(createBookRequestDto.getPrice());
        bookDto.setDescription(createBookRequestDto.getDescription());
        bookDto.setCoverImage(createBookRequestDto.getCoverImage());

        Mockito.when(bookMapper.toEntity(createBookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.save(createBookRequestDto);

        assertEquals(bookDto,savedBookDto);
        Mockito.verify(bookRepository,Mockito.times(1)).save(book);
        Mockito.verify(bookMapper,Mockito.times(1)).toDto(book);
        Mockito.verify(bookMapper,Mockito.times(1)).toEntity(createBookRequestDto);
        Mockito.verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Get all book from table")
    public void findAll_success(){
        Book bookKobzar = new Book();
        bookKobzar.setId(1L);
        bookKobzar.setAuthor("Taras");
        bookKobzar.setTitle("Kobzar");
        bookKobzar.setIsbn("12412151");
        bookKobzar.setPrice(BigDecimal.valueOf(100));
        bookKobzar.setDescription("description");
        bookKobzar.setCoverImage("coverImage");

        Book bookJokes = new Book();
        bookJokes.setId(1L);
        bookJokes.setAuthor("Jora");
        bookJokes.setTitle("Jokes");
        bookJokes.setIsbn("4124231");
        bookJokes.setPrice(BigDecimal.valueOf(90));
        bookJokes.setDescription("description");
        bookJokes.setCoverImage("coverImage");

        int expectedSize = 2;

        Mockito.when(bookRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Arrays.asList(bookKobzar, bookJokes)));
        Page<BookDto> bookDto = bookService.findAll(PageRequest.of(0, 10));
        assertEquals(expectedSize, bookDto.getContent().size());
    }

    @Test
    @DisplayName("Find a book using specification")
    public void search_bySpecification_success(){
        String[] title = new String[]{"Kobzar"};
        String[] author = new String[]{"Taras"};
        String[] isbn = new String[]{"2342"};

        Book bookKobzar = new Book();
        bookKobzar.setId(1L);
        bookKobzar.setAuthor("Taras");
        bookKobzar.setTitle("Kobzar");
        bookKobzar.setIsbn("12412151");
        bookKobzar.setPrice(BigDecimal.valueOf(100));
        bookKobzar.setDescription("description");
        bookKobzar.setCoverImage("coverImage");

        Book bookJokes = new Book();
        bookJokes.setId(2L);
        bookJokes.setAuthor("Jora");
        bookJokes.setTitle("Jokes");
        bookJokes.setIsbn("4124231");
        bookJokes.setPrice(BigDecimal.valueOf(90));
        bookJokes.setDescription("description");
        bookJokes.setCoverImage("coverImage");

        BookDto bookKobzarDto = new BookDto();
        bookKobzarDto.setId(bookKobzar.getId());
        bookKobzarDto.setAuthor(bookKobzar.getAuthor());
        bookKobzarDto.setTitle(bookKobzar.getTitle());
        bookKobzarDto.setIsbn(bookKobzar.getIsbn());
        bookKobzarDto.setPrice(bookKobzar.getPrice());
        bookKobzarDto.setDescription(bookKobzar.getDescription());
        bookKobzarDto.setCoverImage(bookKobzar.getCoverImage());

        BookSearchParametersDto bookSearchParametersDto = new BookSearchParametersDto(title, author, isbn);
        Specification<Book> spec = (root, query, cb) ->
                cb.equal(root.get("title"), title[0]);

        Mockito.when(bookSpecificationBuilder.build(bookSearchParametersDto)).thenReturn(spec);
        Mockito.when(bookRepository.findAll(spec)).thenReturn(List.of(bookKobzar));
        Mockito.when(bookMapper.toDto(bookKobzar)).thenReturn(bookKobzarDto);

        List<BookDto> listDto = bookService.search(bookSearchParametersDto);

        int expectedSize = 1;
        assertEquals(expectedSize, listDto.size());
    }

    @Test
    @DisplayName("Get a book using a book id")
    public void getById_success(){
        Long bookId = 1L;
        Book bookKobzar = new Book();
        bookKobzar.setId(bookId);
        bookKobzar.setAuthor("Taras");
        bookKobzar.setTitle("Kobzar");
        bookKobzar.setIsbn("12412151");
        bookKobzar.setPrice(BigDecimal.valueOf(100));
        bookKobzar.setDescription("description");
        bookKobzar.setCoverImage("coverImage");

        BookDto bookKobzarDto = new BookDto();
        bookKobzarDto.setId(bookId);
        bookKobzarDto.setAuthor("Taras");
        bookKobzarDto.setTitle("Kobzar");
        bookKobzarDto.setIsbn("12412151");
        bookKobzarDto.setPrice(BigDecimal.valueOf(100));
        bookKobzarDto.setDescription("description");
        bookKobzarDto.setCoverImage("coverImage");

        String expected = bookKobzar.getAuthor();

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookKobzar));
        Mockito.when(bookMapper.toDto(bookKobzar)).thenReturn(bookKobzarDto);
        BookDto bookDto = bookService.getById(bookId);
        assertEquals(expected, bookDto.getAuthor());
    }

    @Test
    @DisplayName("Update a book using a book id")
    public void updateById_success(){
        Long bookId = 1L;
        Book bookKobzar = new Book();
        bookKobzar.setId(bookId);
        bookKobzar.setAuthor("Taras");
        bookKobzar.setTitle("Kobzar");
        bookKobzar.setIsbn("12412151");
        bookKobzar.setPrice(BigDecimal.valueOf(100));
        bookKobzar.setDescription("description");
        bookKobzar.setCoverImage("coverImage");

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Stepan");
        requestDto.setTitle("Lilya");
        requestDto.setIsbn("12412151");
        requestDto.setPrice(BigDecimal.valueOf(90));
        requestDto.setDescription("description");
        requestDto.setCoverImage("coverImage");

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(bookKobzar.getId());
        updatedBookDto.setAuthor(requestDto.getAuthor());
        updatedBookDto.setTitle(requestDto.getTitle());
        updatedBookDto.setIsbn(requestDto.getIsbn());
        updatedBookDto.setPrice(requestDto.getPrice());
        updatedBookDto.setDescription(requestDto.getDescription());
        updatedBookDto.setCoverImage(requestDto.getCoverImage());

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookKobzar));
        Mockito.doAnswer(invocation -> {
            CreateBookRequestDto dto = invocation.getArgument(0);
            Book book = invocation.getArgument(1);

            book.setTitle(dto.getTitle());
            book.setAuthor(dto.getAuthor());
            book.setIsbn(dto.getIsbn());
            book.setPrice(dto.getPrice());
            book.setDescription(dto.getDescription());
            book.setCoverImage(dto.getCoverImage());

            return null;
        }).when(bookMapper).updateBookFromDto(Mockito.any(CreateBookRequestDto.class), Mockito.any(Book.class));
        Mockito.when(bookRepository.save(bookKobzar)).thenReturn(bookKobzar);
        Mockito.when(bookMapper.toDto(bookKobzar)).thenReturn(updatedBookDto);

        BookDto bookDto = bookService.updateById(requestDto, bookId);

        assertEquals(requestDto.getAuthor(), bookDto.getAuthor());
    }

    @Test
    @DisplayName("get a book using a category id")
    public void getBooksByCategoryId_success(){
        Long categoryId = 1L;
        Category categoryHorror = new Category();
        categoryHorror.setId(categoryId);
        categoryHorror.setDescription("Horror");
        categoryHorror.setName("horror");

        Book bookKobzar = new Book();
        bookKobzar.setId(1L);
        bookKobzar.setAuthor("Taras");
        bookKobzar.setTitle("Kobzar");
        bookKobzar.setIsbn("12412151");
        bookKobzar.setPrice(BigDecimal.valueOf(100));
        bookKobzar.setDescription("description");
        bookKobzar.setCoverImage("coverImage");
        bookKobzar.setCategories(Set.of(categoryHorror));

        BookDtoWithoutCategoryIds bookKobzarWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookKobzarWithoutCategoryIds.setAuthor(bookKobzar.getAuthor());
        bookKobzarWithoutCategoryIds.setTitle(bookKobzar.getTitle());
        bookKobzarWithoutCategoryIds.setIsbn(bookKobzar.getIsbn());
        bookKobzarWithoutCategoryIds.setPrice(bookKobzar.getPrice());
        bookKobzarWithoutCategoryIds.setDescription(bookKobzar.getDescription());
        bookKobzarWithoutCategoryIds.setCoverImage(bookKobzar.getCoverImage());

        Mockito.when(bookRepository.findAllByCategories_Id(categoryId)).thenReturn(Set.of(bookKobzar));
        Mockito.when(bookMapper.toDtoWithoutCategory(bookKobzar)).thenReturn(bookKobzarWithoutCategoryIds);

        Set<BookDtoWithoutCategoryIds> booksDto = bookService.getBooksByCategoryId(categoryId);

        assertEquals(1, booksDto.size());
        assertTrue(booksDto.contains(bookKobzarWithoutCategoryIds));
    }

    @Test
    @DisplayName("delete a book using a book id")
    public void deleteById_success(){
        Long bookId = 1L;

        Mockito.doNothing().when(bookRepository).deleteById(bookId);
        bookService.deleteById(bookId);

        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(bookId);
    }
}
