import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResponseServiceTest {
    Socket socket;
    BufferedReader bufferedReader;
    DataOutputStream dataOutputStream;
    InputStreamReader inputStreamReader;
    ResponseService responseService;
    ObjectMapper objectMapper;
    RequestMethod requestMethod;
    RequestTool requestTool;


    @BeforeEach
    void init() {
        socket = mock(Socket.class);
        objectMapper = new ObjectMapper();
        responseService = new ResponseService();
        requestMethod = mock(RequestMethod.class);
        bufferedReader = mock(BufferedReader.class);
        inputStreamReader = mock(InputStreamReader.class);
        dataOutputStream = mock(DataOutputStream.class);
        requestTool = mock(RequestTool.class);
    }

    @Test
    void readFirstLIneAndImplementRequestMethodTest() throws IOException {
        //given
        when(bufferedReader.readLine()).thenReturn("GET /time HTTP/1.1").thenReturn("GET /time/id HTTP/1.1");
        RequestMethod resultRequest1 = new RequestMethod();
        resultRequest1.setMethod("GET");
        resultRequest1.setRequestType("time");

        RequestMethod resultRequest2 = new RequestMethod();
        resultRequest2.setMethod("GET");
        resultRequest2.setRequestType("time");
        resultRequest2.setTextId("id");

        //when1
        RequestMethod expectedRequest1 = responseService.readFirstLIneAndImplementRequestMethod(bufferedReader);

        //then1
        assertEquals(expectedRequest1, resultRequest1);

        //when2
        RequestMethod expectedRequest2 = responseService.readFirstLIneAndImplementRequestMethod(bufferedReader);

        //then2
        assertEquals(expectedRequest2, resultRequest2);
    }

    @Test
    void responseFromGETAndTime() throws JsonProcessingException {
        //given
        TimeResponseMessageBody timeResponseMessageBody = new TimeResponseMessageBody();
        timeResponseMessageBody.setTime(String.valueOf(new Date()));
        byte[] resultJsonBytes = objectMapper.writeValueAsBytes(timeResponseMessageBody);

        //when
        byte[] expectedJsonBytes = responseService.responseFromGETAndTime();

        //then
        assertArrayEquals(expectedJsonBytes, resultJsonBytes);
    }

    @Test
    void responseFromGETAndImage() throws IOException {
        //given
        String rootPath = System.getProperty("user.dir");
        BufferedImage originalImage = ImageIO.read(new File(rootPath + File.separator + "src"
            + File.separator + "main" + File.separator + "java" + File.separator + "sea.jpg"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] resultImageBytes = byteArrayOutputStream.toByteArray();

        //when
        byte[] expectedImageBytes = responseService.responseFromGETAndImage();

        //then
        assertArrayEquals(expectedImageBytes, resultImageBytes);
    }

    @Test
    void responseFromGETAndText() {
        //given
        ResponseService.stringStorage.put("textId", "result");
        when(requestMethod.getTextId()).thenReturn(null).thenReturn("wrongTextId")
            .thenReturn("textId");
        String result1 = null;
        String result2 = null;
        String result3 = "result";

        //when1
        String expected1 = responseService.responseFromGETAndText(requestMethod);

        //then1
        assertEquals(expected1, result1);

        //when2
        String expected2 = responseService.responseFromGETAndText(requestMethod);

        //then2
        assertEquals(expected2, result2);

        //when3
        String expected3 = responseService.responseFromGETAndText(requestMethod);

        //then3
        assertEquals(expected3, result3);
    }

    @Test
    void treatFromPostAndText() {
        //given
        TreatStateCode result1 = TreatStateCode.FAIL;
        TreatStateCode result2 = TreatStateCode.SUCEESS;
        when(requestMethod.getTextId()).thenReturn("textId");
        ConcurrentHashMap<String, String> resultStringStorage = new ConcurrentHashMap<>();
        resultStringStorage.put("textId", "messageBody");

        //when1
        TreatStateCode expected1 = responseService.treatFromPostAndText(null, requestMethod);

        //then1
        assertEquals(expected1, result1);

        //when2
        TreatStateCode expected2 = responseService.treatFromPostAndText("messageBody", requestMethod);

        //then2
        assertEquals(expected2, result2);
        assertEquals(ResponseService.stringStorage, resultStringStorage);


    }

    @Test
    void responseFromDELETEAndText() throws IOException {
        //given
        ResponseService.stringStorage.put("textId", "message");
        ResponseService.stringStorage.put("second", "second");
        when(requestMethod.getTextId()).thenReturn(null).thenReturn("textId").thenReturn("wrongId");
        ConcurrentHashMap<String, String> result = new ConcurrentHashMap<>();
        result.put("second", "second");

        //when
        responseService.responseFromDELETEAndText(requestMethod);
        responseService.responseFromDELETEAndText(requestMethod);
        responseService.responseFromDELETEAndText(requestMethod);

        //then
        assertEquals(ResponseService.stringStorage, result);
    }

    @Test
    void sendResponseFromGETAndTime() throws IOException {
        //given
        byte[] bytes = {0, 1, 2};
        //when
        responseService.sendResponseFromGETAndTime(dataOutputStream, bytes);
        //then
        verify(dataOutputStream, times(1)).write(bytes, 0,bytes.length);
        verify(dataOutputStream,times(1)).flush();
    }

    @Test
    void sendResponseFromGETAndImage() throws IOException {
        //given
        byte[] bytes = {0, 1, 2};
        //when
        responseService.sendResponseFromGETAndImage(dataOutputStream, bytes);
        //then
        verify(dataOutputStream, times(1)).write(bytes, 0,bytes.length);
        verify(dataOutputStream,times(1)).flush();
    }

    @Test
    void sendResponseFromGETAndText() throws IOException {
        //given
        when(requestMethod.getTextId()).thenReturn(null).thenReturn("textId").thenReturn("textId");

        //when
        responseService.sendResponseFromGETAndText(dataOutputStream, requestMethod, null);
        responseService.sendResponseFromGETAndText(dataOutputStream, requestMethod, null);
        responseService.sendResponseFromGETAndText(dataOutputStream, requestMethod, "storageString");

        //then
        verify(dataOutputStream, times(1)).writeBytes("storageString");
        verify(dataOutputStream, times(1)).flush();
    }

    @Test
    void responseFromPostAndText() throws IOException {
        //given

        //when1
        responseService.responseFromPostAndText(TreatStateCode.SUCEESS, dataOutputStream);

        //then1
        verify(dataOutputStream, times(1))
            .writeBytes(ResponseStateCode.response201Created.getStateMessage());

        //when2
        responseService.responseFromPostAndText(TreatStateCode.FAIL, dataOutputStream);

        //then2
        verify(dataOutputStream, times(1))
            .writeBytes(ResponseStateCode.response404NotFound.getStateMessage());
    }

    //check
    @Test
    void sendResponseFromDELETEAndText() throws IOException {
        //given

        //when1
        responseService.sendResponseFromDELETEAndText(TreatStateCode.SUCEESS, dataOutputStream);

        //then1
        verify(dataOutputStream, times(1))
            .writeBytes(ResponseStateCode.response204NoContent.getStateMessage());
        verify(dataOutputStream, times(1)).flush();

        //when2
        responseService.sendResponseFromDELETEAndText(TreatStateCode.FAIL, dataOutputStream);

        //then2
        verify(dataOutputStream, times(1))
            .writeBytes(ResponseStateCode.response404NotFound.getStateMessage());
        verify(dataOutputStream, times(2)).flush();
    }
}