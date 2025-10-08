package mate.academy.application.mapper;

import java.util.Optional;
import mate.academy.application.config.MapperConfig;
import mate.academy.application.dto.BookDto;
import mate.academy.application.dto.CreateBookRequestDto;
import mate.academy.application.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    default Book updateDto(Book existing, CreateBookRequestDto createBookRequestDto) {
        Optional.ofNullable(createBookRequestDto.getTitle())
                .ifPresent(existing::setTitle);
        Optional.ofNullable(createBookRequestDto.getAuthor())
                .ifPresent(existing::setAuthor);
        Optional.ofNullable(createBookRequestDto.getIsbn())
                .ifPresent(existing::setIsbn);
        Optional.ofNullable(createBookRequestDto.getDescription())
                .ifPresent(existing::setDescription);
        Optional.ofNullable(createBookRequestDto.getPrice())
                .ifPresent(existing::setPrice);
        Optional.ofNullable(createBookRequestDto.getCoverImage())
                .ifPresent(existing::setCoverImage);

        return existing;
    }

}
