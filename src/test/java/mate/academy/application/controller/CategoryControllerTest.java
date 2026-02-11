package mate.academy.application.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.application.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.application.dto.category.CategoryDto;
import mate.academy.application.dto.category.CreateCategoryRequestDto;
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
public class CategoryControllerTest {
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
                    new ClassPathResource("/database/book/add-three-elements-to-each-table.sql"));
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
                    new ClassPathResource("/database/book/remove-all-from-each-table.sql"));
        }
    }

    private CategoryDto createCategoryDto(Long id, String name, String description) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(id);
        categoryDto.setName(name);
        categoryDto.setDescription(description);
        return categoryDto;
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("Get books by category id")
    public void getBooksByCategoryId() throws Exception {
        BookDtoWithoutCategoryIds kobzarWithoutDto = new BookDtoWithoutCategoryIds();
        kobzarWithoutDto.setAuthor("Taras");
        kobzarWithoutDto.setTitle("Kobzar");
        kobzarWithoutDto.setIsbn("1331421");
        kobzarWithoutDto.setPrice(BigDecimal.valueOf(100));
        kobzarWithoutDto.setDescription("book kobzar");
        kobzarWithoutDto.setCoverImage("coverImage");

        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(kobzarWithoutDto);

        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}/books", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        BookDtoWithoutCategoryIds[] actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        BookDtoWithoutCategoryIds[].class);


        Assertions.assertEquals(1, actual.length);
        assertThat(Arrays.asList(actual))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("Get all books")
    public void getAll() throws Exception {
        CategoryDto horrorCategory = createCategoryDto(1L, "horror", "Horror");
        CategoryDto comedyCategory = createCategoryDto(2L, "comedy", "Comedy");
        CategoryDto romanCategory = createCategoryDto(3L, "roman", "Roman");

        List<CategoryDto> expected = new ArrayList<>();
        expected.add(horrorCategory);
        expected.add(comedyCategory);
        expected.add(romanCategory);

        MvcResult mvcResult = mockMvc.perform(
                get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(mvcResult.getResponse().getContentAsString());

        CategoryDto[] actual = objectMapper.readValue(root.get("content").toString(), CategoryDto[].class);

        Assertions.assertEquals(3, actual.length);
        assertThat(Arrays.asList(actual))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    @DisplayName("Get a category by id")
    public void getCategoryById() throws Exception {
        CategoryDto expected = createCategoryDto(1L, "horror", "Horror");

        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Create a category")
    public void createCategory() throws Exception {
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto();
        categoryRequestDto.setName("Rapuncel");
        categoryRequestDto.setDescription("Rapuncel");

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertEquals(categoryRequestDto.getName(), actual.getName());
        Assertions.assertEquals(categoryRequestDto.getDescription(), actual.getDescription());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Update a category by id")
    public void updateCategory() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Rapuncel");
        requestDto.setDescription("Rapuncel");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult mvcResult = mockMvc.perform(
                        put("/categories/{id}", 1L)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), CategoryDto.class);

        Assertions.assertEquals(requestDto.getName(), actual.getName());
        Assertions.assertEquals(requestDto.getDescription(), actual.getDescription());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @DisplayName("Delete a category by id")
    public void deleteCategory() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L).with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories/{id}", 1L)
                        .with(user("user").roles("USER")))
                .andExpect(status().isNotFound());
    }
}
