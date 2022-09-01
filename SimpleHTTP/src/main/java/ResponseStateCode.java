import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStateCode {
    response200OK("HTTP/1.1 200 OK\r\n\r\n"),
    response201Created("HTTP/1.1 201 Created\r\n\r\n") ,
    response204NoContent("HTTP/1.1 204 No Content\r\n\r\n"),
    response404NotFound("HTTP/1.1 404 Not Found\r\n\r\n") ;

    private final String stateMessage;
}