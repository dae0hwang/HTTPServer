//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.*;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.Socket;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class RequestHandlerTest {
//
//    Socket socket;
//    BufferedReader bufferedReader;
//    DataOutputStream dataOutputStream;
//    RequestHandler requestHandler;
//    InputStreamReader inputStreamReader;
//
//    @BeforeEach
//    void init() {
//        socket = mock(Socket.class);
//        requestHandler = new RequestHandler(socket);
//        bufferedReader = mock(BufferedReader.class);
//        inputStreamReader = mock(InputStreamReader.class);
//        dataOutputStream = mock(DataOutputStream.class);
//    }
//
//    @Test
//    void readFirstLIneAndImplementRequestMethodTest() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
//        //reflection
//        Method method =
//            requestHandler.getClass().getDeclaredMethod
//                ("readFirstLIneAndImplementRequestMethod", BufferedReader.class);
//        method.setAccessible(true);
//
//        //given
//        when(bufferedReader.readLine()).thenReturn("GET /time HTTP/1.1");
//        RequestMethod resultRequest = new RequestMethod();
//        resultRequest.setMethod("GET");
//        resultRequest.setRequestType("time");
//
//        //when
//        RequestMethod expectedRequest = (RequestMethod) method.invoke(requestHandler, bufferedReader);
//
//        //then
//        assertEquals(expectedRequest, resultRequest);
//    }
//
//    @Test
//    void readFirstLIneAndImplementRequestMethodTest2() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
//        //reflection
//        Method method =
//            requestHandler.getClass().getDeclaredMethod
//                ("readFirstLIneAndImplementRequestMethod", BufferedReader.class);
//        method.setAccessible(true);
//
//        //given
//        when(bufferedReader.readLine()).thenReturn("POST /text/%7Ba%7D HTTP/1.1");
//        RequestMethod resultRequest = new RequestMethod();
//        resultRequest.setMethod("POST");
//        resultRequest.setRequestType("text");
//        resultRequest.setTextId("%7Ba%7D");
//
//        //when
//        RequestMethod expectedRequest = (RequestMethod) method.invoke(requestHandler, bufferedReader);
//
//        //then
//        assertEquals(expectedRequest, resultRequest);
//    }
//
//    @Test
//    void responseFromGETAndTimeTest() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException {
//        //reflection
//        Method method =
//            requestHandler.getClass().getDeclaredMethod
//                ("responseFromGETAndTime", BufferedReader.class);
//        method.setAccessible(true);
//
//        //given
//
//        when(bufferedReader.readLine()).thenReturn("POST /text/%7Ba%7D HTTP/1.1");
//        RequestMethod resultRequest = new RequestMethod();
//        resultRequest.setMethod("POST");
//        resultRequest.setRequestType("text");
//        resultRequest.setTextId("%7Ba%7D");
//
//        //when
//        RequestMethod expectedRequest = (RequestMethod) method.invoke(requestHandler, bufferedReader);
//
//        //then
//        assertEquals(expectedRequest, resultRequest);
//    }
//
//}