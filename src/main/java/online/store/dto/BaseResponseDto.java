package online.store.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseResponseDto {

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd'  'HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd'  'HH:mm:ss")
    private LocalDateTime lastUpdateOn;
}
