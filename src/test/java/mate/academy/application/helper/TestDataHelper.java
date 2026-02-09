package mate.academy.application.helper;

import mate.academy.application.dto.book.BookDto;
import mate.academy.application.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.util.Set;

public final class TestDataHelper {

    private TestDataHelper() {
    }

    public static BookDto createBookDto(
            Long id,
            String author,
            String title,
            String isbn,
            BigDecimal price,
            String description,
            String coverImage,
            Set<Long> categoryIds
    ) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setAuthor(author);
        dto.setTitle(title);
        dto.setIsbn(isbn);
        dto.setPrice(price);
        dto.setDescription(description);
        dto.setCoverImage(coverImage);
        dto.setCategoryIds(categoryIds);
        return dto;
    }

    public static CreateBookRequestDto createBookRequestDto(
            String author,
            String title,
            String isbn,
            BigDecimal price,
            String description,
            String coverImage,
            Set<Long> categoryIds
    ) {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(author);
        requestDto.setTitle(title);
        requestDto.setIsbn(isbn);
        requestDto.setPrice(price);
        requestDto.setDescription(description);
        requestDto.setCoverImage(coverImage);
        requestDto.setCategoryIds(categoryIds);
        return requestDto;
    }


}
