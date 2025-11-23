package mate.academy.application.service;

import java.util.List;
import mate.academy.application.dto.category.CategoryDto;
import mate.academy.application.dto.category.CreateCategoryRequestDto;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(CreateCategoryRequestDto categoryDto, Long id);

    void deleteById(Long id);
}
