package online.store.model;

import org.springframework.http.HttpStatus;

import java.util.List;

public record PageResponse<T>(HttpStatus status, String message, int pageNumber, int pageSize,
                              long totalRecords, int totalPages, List<T> records) {

    public PageResponse(HttpStatus status, int pageNumber, int pageSize,
                        long totalRecords, int totalPages, List<T> records) {
        this(status, "processed successfully", pageNumber, pageSize, totalRecords, totalPages, records);
    }
}
