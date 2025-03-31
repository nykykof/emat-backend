package ci.csi.emat.common.advice.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@Builder
public class ApiError {

    private String businessErrorMessage;
    private String path;
    private String field;
    private String businessErrorCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss", timezone = "UTC")
    private LocalDateTime timestamp;

    public LocalDateTime getTimestamp() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
