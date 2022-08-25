import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseStateCode {
    public static void response200OK(DataOutputStream dataOutputStream) throws IOException {
        String responseStateMessage = "HTTP/1.1 200 OK\r\n\r\n";
        dataOutputStream.write(responseStateMessage.getBytes("UTF-8"));
    }

    public static void response201Created(DataOutputStream dataOutputStream) throws IOException {
        String responseStateMessage = "HTTP/1.1 201 Created\r\n\r\n";
        dataOutputStream.write(responseStateMessage.getBytes("UTF-8"));
    }

    public static void response204NoContent(DataOutputStream dataOutputStream) throws IOException {
        String responseStateMessage = "HTTP/1.1 204 No Content\r\n\r\n";
        dataOutputStream.write(responseStateMessage.getBytes("UTF-8"));
    }

    public static void response404NotFound(DataOutputStream dataOutputStream) throws IOException {
        String responseStateMessage = "HTTP/1.1 404 Not Found\r\n\r\n";
        dataOutputStream.write(responseStateMessage.getBytes("UTF-8"));
    }
}
