package mate.academy.application.service;

import mate.academy.application.dto.book.BookDto;
import mate.academy.application.dto.book.CreateBookRequestDto;
import mate.academy.application.dto.category.CategoryDto;
import mate.academy.application.dto.category.CreateCategoryRequestDto;
import mate.academy.application.mapper.CategoryMapper;
import mate.academy.application.model.Book;
import mate.academy.application.model.Category;
import mate.academy.application.repository.category.CategoryRepository;
import mate.academy.application.service.impl.CategoryServiceImpl;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Save a category")
    public void save_correctData_success(){
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("horror");
        requestDto.setDescription("Horror");

        Category category = new Category();
        category.setId(1L);
        category.setDescription(requestDto.getDescription());
        category.setName(requestDto.getName());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setName(category.getName());

        Mockito.when(categoryMapper.toEntity(Mockito.any())).thenReturn(category);
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        Mockito.when(categoryMapper.toDto(Mockito.any())).thenReturn(categoryDto);

        CategoryDto savedCategory = categoryService.save(requestDto);

        assertEquals(categoryDto, savedCategory);
    }

    @Test
    @DisplayName("Get all categories from table")
    public void findAll_success(){
        Category categoryHorror = new Category();
        categoryHorror.setId(1L);
        categoryHorror.setName("horror");
        categoryHorror.setDescription("Horror");

        Category categoryComedy = new Category();
        categoryComedy.setId(2L);
        categoryComedy.setName("comedy");
        categoryComedy.setDescription("Comedy");

        int expectedSize = 2;

        Mockito.when(categoryRepository.findAll(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(Arrays.asList(categoryHorror, categoryComedy)));

        Page<CategoryDto> categoryDtos = categoryService.findAll(PageRequest.of(0, 10));
        assertEquals(expectedSize, categoryDtos.getContent().size());
    }

    @Test
    @DisplayName("Get a category using a category id")
    public void getById_success(){
        Long categoryId = 1L;

        Category categoryHorror = new Category();
        categoryHorror.setId(categoryId);
        categoryHorror.setName("horror");
        categoryHorror.setDescription("Horror");

        CategoryDto categoryHorrorDto = new CategoryDto();
        categoryHorrorDto.setId(categoryHorror.getId());
        categoryHorrorDto.setDescription(categoryHorror.getDescription());
        categoryHorrorDto.setName(categoryHorror.getName());

        String expected = categoryHorror.getName();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryHorror));
        Mockito.when(categoryMapper.toDto(categoryHorror)).thenReturn(categoryHorrorDto);
        CategoryDto categoryDto = categoryService.getById(categoryId);
        assertEquals(expected, categoryDto.getName());
    }

    @Test
    @DisplayName("Update a category using a category id")
    public void updateById_success(){
        Long categoryId = 1L;

        Category categoryHorror = new Category();
        categoryHorror.setId(categoryId);
        categoryHorror.setName("comedy");
        categoryHorror.setDescription("Comedy");

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("horror");
        requestDto.setDescription("Horror");

        CategoryDto categoryHorrorDto = new CategoryDto();
        categoryHorrorDto.setId(categoryHorror.getId());
        categoryHorrorDto.setDescription(requestDto.getDescription());
        categoryHorrorDto.setName(requestDto.getName());

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryHorror));
        Mockito.doAnswer(invocation -> {
            CreateCategoryRequestDto dto = invocation.getArgument(0);
            Category category = invocation.getArgument(1);

            category.setName(dto.getName());
            category.setDescription(dto.getDescription());

            return null;
        }).when(categoryMapper).updateCategoryFromDto(Mockito.any(CreateCategoryRequestDto.class),
                Mockito.any(Category.class));
        Mockito.when(categoryRepository.save(categoryHorror)).thenReturn(categoryHorror);
        Mockito.when(categoryMapper.toDto(categoryHorror)).thenReturn(categoryHorrorDto);

        String expected = requestDto.getName();
        String actual = categoryService.update(requestDto, categoryId).getName();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("delete a category using a category id")
    public void deleteById_success() {
        Long categoryId = 1L;

        Mockito.doNothing().when(categoryRepository).deleteById(categoryId);
        categoryService.deleteById(categoryId);

        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(categoryId);
    }
}
