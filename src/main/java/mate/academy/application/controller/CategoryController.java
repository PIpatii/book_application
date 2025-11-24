package mate.academy.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.application.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.application.dto.category.CategoryDto;
import mate.academy.application.dto.category.CreateCategoryRequestDto;
import mate.academy.application.service.BookService;
import mate.academy.application.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "Books category")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    @Operation(summary = "Get a books by category id",
            description = "Get a books using category id")
    public Set<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.getBooksByCategoryId(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all", description = "Get all category")
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Get a category using id")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    @Operation(summary = "Update a category", description = "Update a category using id")
    public CategoryDto updateCategory(@RequestBody @Valid CreateCategoryRequestDto categoryDto,
                                      @PathVariable Long id) {
        return categoryService.update(categoryDto, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create", description = "Create a category")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category", description = "Delete a category using id")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

}
