package mate.academy.application.mapper;

import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.BookDto;
import mate.academy.application.dto.CreateBookRequestDto;
import mate.academy.application.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);
}
