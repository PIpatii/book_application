package mate.academy.application.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.application.dto.book.BookDto;
import mate.academy.application.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach(@Autowired DataSource dataSource,
                                 @Autowired WebApplicationContext webApplicationContext) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/add-three-elements-to-each-table.sql"));
        }
    }

    @AfterEach
    public void afterAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/remove-all-from-each-table.sql"));
        }
    }

    private BookDto createBookDto(Long id, String author, String title,
                                  String isbn, BigDecimal price, String description,
                                  String coverImage, Set<Long> categoryId) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setAuthor(author);
        dto.setTitle(title);
        dto.setIsbn(isbn);
        dto.setPrice(price);
        dto.setDescription(description);
        dto.setCoverImage(coverImage);
        dto.setCategoryIds(categoryId);
        return dto;
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Create a book")
    public void createBook() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Taras");
        createBookRequestDto.setTitle("Kobzar");
        createBookRequestDto.setIsbn("1241214");
        createBookRequestDto.setPrice(BigDecimal.valueOf(100));
        createBookRequestDto.setDescription("book kobzar");
        createBookRequestDto.setCoverImage("coverImage");
        createBookRequestDto.setCategoryIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertEquals(createBookRequestDto.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(createBookRequestDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(createBookRequestDto.getPrice(), actual.getPrice());
        Assertions.assertEquals(createBookRequestDto.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(createBookRequestDto.getCoverImage(), actual.getCoverImage());
        Assertions.assertEquals(createBookRequestDto.getDescription(), actual.getDescription());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("get all books")
    public void getAll_GivenProductInCatalog_ShouldReturnAll() throws Exception {
        BookDto bookKobzar = createBookDto(1L, "Taras", "Kobzar", "1331421",
                BigDecimal.valueOf(100), "book kobzar", "coverImage", Set.of(1L));

        BookDto bookAzbuka = createBookDto(2L, "Babas", "Azbuka", "54121",
                BigDecimal.valueOf(90), "book azbuka", "coverImage", Set.of(2L));

        BookDto bookTable = createBookDto(3L, "Cufal", "Table", "12512",
                BigDecimal.valueOf(110), "book table", "coverImage", Set.of(3L));

        List<BookDto> expected = new ArrayList<>();
        expected.add(bookKobzar);
        expected.add(bookAzbuka);
        expected.add(bookTable);

        MvcResult mvcResult = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root =  objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        BookDto[] actual = objectMapper.readValue(
                root.get("content").toString(),
                BookDto[].class
        );

        Assertions.assertEquals(3, actual.length);
        assertThat(Arrays.asList(actual))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("get book by id")
    public void getById_CorrectData_ShouldReturnOneBook() throws Exception {
        BookDto expected = createBookDto(
                1L,
                "Taras",
                "Kobzar",
                "1331421",
                BigDecimal.valueOf(100),
                "book kobzar",
                "coverImage",
                Set.of(1L));

        MvcResult mvcResult = mockMvc.perform(
                        get("/books/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(expected.getCoverImage(), actual.getCoverImage());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getCategoryIds(), actual.getCategoryIds());
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("Search books by parameters")
    void searchBooks_ShouldReturnFilteredBooks() throws Exception {
        BookDto expected = createBookDto(
                1L,
                "Taras",
                "Kobzar",
                "1331421",
                BigDecimal.valueOf(100),
                "book kobzar",
                "coverImage",
                Set.of(1L));

        MvcResult mvcResult = mockMvc.perform(
                        get("/books/search")
                                .param("author", "Taras")
                                .param("title", "Kobzar")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<List<BookDto>>() {
                }
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(List.of(expected));
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Delete book by id")
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/{id}", 1L)
                        .with(user("user").roles("USER")))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("update book by id")
    public void updateById_ShouldReturnUpdatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Karas");
        requestDto.setTitle("Nadzemni");
        requestDto.setIsbn("5125123");
        requestDto.setPrice(BigDecimal.valueOf(100));
        requestDto.setDescription("book Nadzemni");
        requestDto.setCoverImage("coverImage");
        requestDto.setCategoryIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/books/{id}", 1L)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto.class);

        Assertions.assertEquals(requestDto.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(requestDto.getTitle(), actual.getTitle());
        Assertions.assertEquals(requestDto.getPrice(), actual.getPrice());
        Assertions.assertEquals(requestDto.getIsbn(), actual.getIsbn());
        Assertions.assertEquals(requestDto.getCoverImage(), actual.getCoverImage());
        Assertions.assertEquals(requestDto.getDescription(), actual.getDescription());
        Assertions.assertEquals(requestDto.getCategoryIds(), actual.getCategoryIds());
    }
}
