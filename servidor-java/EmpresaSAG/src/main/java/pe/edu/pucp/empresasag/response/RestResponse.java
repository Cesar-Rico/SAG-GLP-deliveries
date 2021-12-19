package pe.edu.pucp.empresasag.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@NoArgsConstructor
public class RestResponse {
    private HttpStatus status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object payload;

    public RestResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public RestResponse(HttpStatus status, String message, Object payload) {
        this(status, message);
        this.payload = payload;
    }
}