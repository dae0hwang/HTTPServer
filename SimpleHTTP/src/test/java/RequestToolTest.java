import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RequestToolTest {

    BufferedReader bufferedReader;
    RequestTool requestTool;

    @BeforeEach
    void init() {
        bufferedReader = mock(BufferedReader.class);
        requestTool = new RequestTool();
    }
    @Test
    void readDate() throws IOException {
        //given
        char[] headerData = new char[]{'m','e','s','s','a','g','e'};

        doAnswer(mockData -> {
            System.arraycopy(headerData, 0, mockData.getArguments()[0], 0, 7);
            return null;
        }).when(bufferedReader).read(any(char[].class), eq(0), eq(7));

        //when
        String expected = requestTool.readDate(bufferedReader, 7);

        //then
        assertEquals(expected, "message");
    }

    @Test
    void readHeader() throws IOException {
        //given
        when(bufferedReader.readLine()).thenReturn(null).thenReturn("sec-ch-ua-mobile: ?0")
            .thenReturn("sec-ch-ua-platform: \"Windows\"")
            .thenReturn(null);
        Map<String, String> resultMap1 = new HashMap<>();
        Map<String, String> resultMap2 = new HashMap<>();
        resultMap2.put("sec-ch-ua-mobile", "?0");
        resultMap2.put("sec-ch-ua-platform", "\"Windows\"");

        //when1
        Map expectedMap1 = requestTool.readHeader(bufferedReader);

        //then1
        assertEquals(expectedMap1, resultMap1);

        //when2
        Map expectedMap2 = requestTool.readHeader(bufferedReader);

        //then2
        assertEquals(expectedMap2, resultMap2);
    }
}