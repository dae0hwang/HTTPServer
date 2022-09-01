import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseService {
    private ObjectMapper objectMapper = new ObjectMapper();
    RequestTool requestTool = new RequestTool();
    protected static ConcurrentHashMap<String, String> stringStorage = new ConcurrentHashMap<>();

    public  RequestMethod readFirstLIneAndImplementRequestMethod(BufferedReader bufferedReader)
        throws IOException {
        RequestMethod requestMethod = new RequestMethod();
        String firstLine = bufferedReader.readLine();
        String[] firstLineArray = firstLine.split(" ");
        requestMethod.setMethod(firstLineArray[0]);
        String requestTypeAndTextId = firstLineArray[1].substring(1, firstLineArray[1].length());
        if (requestTypeAndTextId.contains("/")) {
            String[] requestTypeAndTextIdArray = requestTypeAndTextId.split("/");
            requestMethod.setRequestType(requestTypeAndTextIdArray[0]);
            requestMethod.setTextId(requestTypeAndTextIdArray[1]);
        } else {
            requestMethod.setRequestType(requestTypeAndTextId);
        }
        return requestMethod;
    }

    public byte[] responseFromGETAndTime() throws JsonProcessingException {
        TimeResponseMessageBody timeResponseMessageBody = new TimeResponseMessageBody();
        timeResponseMessageBody.setTime(String.valueOf(new Date()));
        byte[] timeJsonBytes = objectMapper.writeValueAsBytes(timeResponseMessageBody);
        return timeJsonBytes;
    }

    public byte[] responseFromGETAndImage() throws IOException {
        String rootPath = System.getProperty("user.dir");
        BufferedImage originalImage =
            ImageIO.read(new File(rootPath+File.separator+"src" +File.separator+"main"
                +File.separator+"java"+File.separator+"sea.jpg"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        return imageInByte;
    }

    public String responseFromGETAndText(RequestMethod requestMethod){
        String textId = requestMethod.getTextId();
        if (textId != null) {
            String storageString = stringStorage.get(textId);
            if (storageString != null) {
                return storageString;
            }
        }
        return null;
    }

    public void responseFromPostAndText(RequestMethod requestMethod, BufferedReader bufferedReader) throws IOException {
        String textId = requestMethod.getTextId();
        if (textId != null) {
            Map<String, String> headerInformation = requestTool.readHeader(bufferedReader);
            if (headerInformation.containsKey("Content-Length")) {
                int messageBodyLength = Integer.parseInt(headerInformation.get("Content-Length").trim());
                String messageBody = requestTool.readDate(bufferedReader, messageBodyLength);
                stringStorage.put(textId, messageBody);
            }
        }
    }

    public void responseFromDELETEAndText(RequestMethod requestMethod) throws IOException {
        String textId = requestMethod.getTextId();
        if (textId != null) {
            String getString = stringStorage.get(textId);
            if (getString != null) {
                stringStorage.remove(textId);
            } else {
            }
        } else {
        }
    }

    public void sendResponseFromGETAndTime(DataOutputStream dataOutputStream, byte[] timeJsonBytes)
        throws IOException {
        ResponseStateCode.response200OK.response(dataOutputStream);
        dataOutputStream.write(timeJsonBytes, 0, timeJsonBytes.length);
        dataOutputStream.flush();
    }

    public void sendResponseFromGETAndImage(DataOutputStream dataOutputStream, byte[] imageInByte)
        throws IOException {
        ResponseStateCode.response200OK.response(dataOutputStream);
        dataOutputStream.write(imageInByte,0,imageInByte.length);
        dataOutputStream.flush();
    }

    public void sendResponseFromGETAndText(DataOutputStream dataOutputStream, RequestMethod requestMethod,
                                           String storageString) throws IOException {
        String textId = requestMethod.getTextId();
        if (textId == null) {
            ResponseStateCode.response404NotFound.response(dataOutputStream);
        } else {
            if (storageString == null) {
                ResponseStateCode.response404NotFound.response(dataOutputStream);
            } else {
                ResponseStateCode.response200OK.response(dataOutputStream);
                dataOutputStream.writeBytes(storageString);
                dataOutputStream.flush();
            }
        }
    }

    public void sendResponseFromPostAndText(RequestMethod requestMethod, DataOutputStream dataOutputStream)
        throws IOException {
        String textId = requestMethod.getTextId();
        if (textId == null) {
            ResponseStateCode.response404NotFound.response(dataOutputStream);
        } else {
            ResponseStateCode.response201Created.response(dataOutputStream);
            dataOutputStream.flush();
        }
    }

    public void sendResponseFromDELETEAndText(RequestMethod requestMethod, DataOutputStream dataOutputStream)
        throws IOException {
        String textId = requestMethod.getTextId();
        if (textId != null) {
            String getString = stringStorage.get(textId);
            if (getString != null) {
                ResponseStateCode.response204NoContent.response(dataOutputStream);
            } else {
                ResponseStateCode.response404NotFound.response(dataOutputStream);
            }
        } else {
            ResponseStateCode.response404NotFound.response(dataOutputStream);
        }
    }
}
