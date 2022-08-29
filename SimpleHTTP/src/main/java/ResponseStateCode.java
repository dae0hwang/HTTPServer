import java.io.DataOutputStream;
import java.io.IOException;

public enum ResponseStateCode {
    response200OK {
        @Override
        void response(DataOutputStream dataOutputStream) throws IOException {
            String responseStateMessage = "HTTP/1.1 200 OK\r\n\r\n";
            dataOutputStream.write(responseStateMessage.getBytes("UTF-8"));
        }
    },
    response201Created {
        @Override
        void response(DataOutputStream dataOutputStream) throws IOException {
            String responseStateMessage = "HTTP/1.1 201 Created\r\n\r\n";
            dataOutputStream.write(responseStateMessage.getBytes("UTF-8"));
        }
    },
    response204NoContent {
        @Override
        void response(DataOutputStream dataOutputStream) throws IOException{
            String responseStateMessage = "HTTP/1.1 204 No Content\r\n\r\n";
            dataOutputStream.write(responseStateMessage.getBytes("UTF-8"));
        }
    },
    response404NotFound {
        @Override
        void response(DataOutputStream dataOutputStream) throws IOException{
            String responseStateMessage = "HTTP/1.1 404 Not Found\r\n\r\n";
            dataOutputStream.write(responseStateMessage.getBytes("UTF-8"));
        }
    };

    abstract void response(DataOutputStream dataOutputStream) throws IOException;
}

