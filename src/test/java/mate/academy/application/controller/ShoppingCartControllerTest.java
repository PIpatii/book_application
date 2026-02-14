package mate.academy.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.application.dto.cart.ShoppingCartDto;
import mate.academy.application.dto.cart.item.AddCartItemRequestDto;
import mate.academy.application.dto.cart.item.UpdateCartItemRequestDto;
import mate.academy.application.repository.cart.CartItemRepository;
import org.junit.jupiter.api.AfterEach;
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
import java.sql.Connection;
import java.sql.SQLException;
import static mate.academy.application.helper.TestDataHelper.createShoppingCartDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @BeforeEach
    public void beforeEach(@Autowired DataSource dataSource,
                           @Autowired WebApplicationContext webApplicationContext) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/cart/add-elements-to-each-table.sql"));
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
                    new ClassPathResource("database/cart/remove-from-each-table.sql"));
        }
    }

    @WithMockUser(username = "email", roles = "USER")
    @Test
    @DisplayName("get a shopping cart by user id")
    void get_ShouldReturnShoppingCart() throws Exception {
        ShoppingCartDto expected = createShoppingCartDto(2L, 2L);

        MvcResult mvcResult = mockMvc.perform(get("/carts"))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @WithMockUser(username = "email", roles = "USER")
    @Test
    @DisplayName("add an item to shopping cart")
    void addItem_ShouldReturnNoContent() throws Exception {

        AddCartItemRequestDto addCartItemRequestDto = new AddCartItemRequestDto();
        addCartItemRequestDto.setBookId(1L);
        addCartItemRequestDto.setQuantity(1);

        ShoppingCartDto expected = createShoppingCartDto(2L, 2L);

        String jsonRequest = objectMapper.writeValueAsString(addCartItemRequestDto);

        MvcResult mvcResult = mockMvc.perform(
                        post("/carts")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ShoppingCartDto.class);
        assertEquals(expected.getId(), actual.getId());
    }

    @WithMockUser(username = "email", roles = "USER")
    @Test
    @DisplayName("update field quantity in a cart item")
    void updateQuantity_ShouldReturnNoContent() throws Exception {
        ShoppingCartDto expected = createShoppingCartDto(2L, 2L);
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto();
        requestDto.setQuantity(2);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult mvcResult = mockMvc.perform(put("/carts/items/{cartItemId}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(), ShoppingCartDto.class);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @WithMockUser(username = "email", roles = "USER")
    @Test
    @DisplayName("Delete book by id")
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/carts/items/{cartItemId}", 1L).with(csrf()))
                .andExpect(status().isOk());
        boolean cartItem = cartItemRepository.findById(1L).isPresent();

        assertFalse(cartItem);
    }
}
