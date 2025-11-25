package mate.academy.application.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.book.BookDto;
import mate.academy.application.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.application.dto.book.CreateBookRequestDto;
import mate.academy.application.model.Book;
import mate.academy.application.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "category", ignore = true)
    Book toEntity(CreateBookRequestDto createBookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategory(Book book);

    void updateBookFromDto(CreateBookRequestDto createBookRequestDto, @MappingTarget Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoryIdsSet = book.getCategory().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());

        bookDto.setCategoryIds(categoryIdsSet);
    }

    @AfterMapping
    default void setCategory(@MappingTarget Book book, CreateBookRequestDto bookRequestDto) {
        Set<Category> categorySet = bookRequestDto.getCategoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet());

        book.setCategory(categorySet);
    }
}
