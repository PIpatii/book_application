package mate.academy.application.mapper;

import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.category.CategoryDto;
import mate.academy.application.dto.category.CreateCategoryRequestDto;
import mate.academy.application.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryDto);

    void updateCategoryFromDto(CreateCategoryRequestDto createCategoryRequestDto,
                               @MappingTarget Category category);
}

