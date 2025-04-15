package online.store.utils;

import online.store.model.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.function.Function;

public final class PageResponseUtil {
    private PageResponseUtil() {}

    public static <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {
        Page<R> mappedPage = page.map(mapper);
        return new PageResponse<>(
                HttpStatus.OK,
                page.getNumber(),
                page.getSize(),
                mappedPage.getTotalElements(),
                mappedPage.getTotalPages(),
                mappedPage.getContent()
        );
    }
}
