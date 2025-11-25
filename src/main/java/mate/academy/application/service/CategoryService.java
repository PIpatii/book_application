package mate.academy.application.service;

import mate.academy.application.dto.category.CategoryDto;
import mate.academy.application.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(CreateCategoryRequestDto categoryDto, Long id);

    void deleteById(Long id);
}
