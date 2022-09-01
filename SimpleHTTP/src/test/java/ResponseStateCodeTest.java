//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.DataOutputStream;
//import java.io.IOException;
//
//import static org.mockito.Mockito.*;
//
//class ResponseStateCodeTest {
//
//    DataOutputStream dataOutputStream;
//    @BeforeEach
//    void init() {
//        dataOutputStream = mock(DataOutputStream.class);
//    }
//    @Test
//    void response200OK() throws IOException {
//        //given
//        String responseStateMessage = "HTTP/1.1 200 OK\r\n\r\n";
//
//        //when
//        ResponseStateCode.response200OK.response(dataOutputStream);
//
//        //then
//        verify(dataOutputStream, times(1))
//            .write(responseStateMessage.getBytes("UTF-8"));
//    }
//
//    @Test
//    void response201Created() throws IOException {
//        //given
//        String responseStateMessage = "HTTP/1.1 201 Created\r\n\r\n";
//
//        //when
//        ResponseStateCode.response201Created.response(dataOutputStream);
//
//        //then
//        verify(dataOutputStream, times(1))
//            .write(responseStateMessage.getBytes("UTF-8"));
//    }
//
//    @Test
//    void response204NoContent() throws IOException {
//        //given
//        String responseStateMessage = "HTTP/1.1 204 No Content\r\n\r\n";
//
//        //when
//        ResponseStateCode.response204NoContent.response(dataOutputStream);
//
//        //then
//        verify(dataOutputStream, times(1))
//            .write(responseStateMessage.getBytes("UTF-8"));
//    }
//
//    @Test
//    void response404NotFound() throws IOException {
//        //given
//        String responseStateMessage = "HTTP/1.1 404 Not Found\r\n\r\n";
//
//        //when
//        ResponseStateCode.response404NotFound.response(dataOutputStream);
//
//        //then
//        verify(dataOutputStream, times(1))
//            .write(responseStateMessage.getBytes("UTF-8"));
//    }
//}