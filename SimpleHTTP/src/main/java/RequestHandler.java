import com.fasterxml.jackson.databind.ObjectMapper;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHandler implements Runnable {
    private Socket connection;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static ConcurrentHashMap<String, String> stringStorage = new ConcurrentHashMap<>();

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            RequestMethod requestMethod = readFirstLIneAndImplementRequestMethod(bufferedReader);
            System.out.println(requestMethod.toString());

            switch (requestMethod.getMethod()) {
                case "GET":
                    switch (requestMethod.getRequestType()) {
                        case "time":
                            responseFromGETAndTime(dataOutputStream);
                            break;
                        case "image":
                            responseFromGETAndImage(dataOutputStream);
                            break;
                        case "text":
                            responseFromGETAndText(dataOutputStream, requestMethod);
                            break;
                    }
                    break;
                case "POST":
                    switch (requestMethod.getRequestType()) {
                        case "text":
                            responseFromPostAndText(requestMethod, dataOutputStream, bufferedReader);
                            break;
                    }
                    break;
                case "DELETE":
                    switch (requestMethod.getRequestType()) {
                        case "text":
                            responseFromDELETEAndText(requestMethod, dataOutputStream);
                            break;
                    }
            }
            System.out.println("checking StringStorage: " + stringStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static RequestMethod readFirstLIneAndImplementRequestMethod(BufferedReader bufferedReader)
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

    private static void responseFromGETAndTime(DataOutputStream dataOutputStream) throws IOException {
        TimeResponseMessageBody timeResponseMessageBody = new TimeResponseMessageBody();
        timeResponseMessageBody.setTime(String.valueOf(new Date()));
        byte[] timeJsonBytes = objectMapper.writeValueAsBytes(timeResponseMessageBody);
        ResponseStateCode.response200OK.response(dataOutputStream);
        dataOutputStream.write(timeJsonBytes, 0, timeJsonBytes.length);
        dataOutputStream.flush();
    }

    private static void responseFromGETAndImage(DataOutputStream dataOutputStream) throws IOException {
        String rootPath = System.getProperty("user.dir");
        BufferedImage originalImage =
            ImageIO.read(new File(rootPath+File.separator+"SimpleHTTP"+File.separator+"src"+File.separator+
                "main"+File.separator+"java"+File.separator+"sea.jpg"));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        ResponseStateCode.response200OK.response(dataOutputStream);
        dataOutputStream.write(imageInByte,0,imageInByte.length);
        dataOutputStream.flush();
    }

    private static void responseFromGETAndText(DataOutputStream dataOutputStream, RequestMethod requestMethod) throws IOException {
        String textId = requestMethod.getTextId();
        if (textId == null) {
            ResponseStateCode.response404NotFound.response(dataOutputStream);
        } else {
            String storageString = stringStorage.get(textId);
            if (storageString == null) {
                ResponseStateCode.response404NotFound.response(dataOutputStream);
            } else {
                ResponseStateCode.response200OK.response(dataOutputStream);
                dataOutputStream.writeBytes(storageString);
                dataOutputStream.flush();
            }
        }
    }
    private static void responseFromPostAndText(RequestMethod requestMethod, DataOutputStream dataOutputStream,
                                                BufferedReader bufferedReader) throws IOException {
        String textId = requestMethod.getTextId();
        if (textId == null) {
            ResponseStateCode.response404NotFound.response(dataOutputStream);
        } else {
            Map<String, String> headerInformation = RequestTool.readHeader(bufferedReader);
            int messageBodyLength = Integer.parseInt(headerInformation.get("Content-Length"));
            String messageBody = RequestTool.readDate(bufferedReader, messageBodyLength);
            stringStorage.put(textId, messageBody);
            ResponseStateCode.response201Created.response(dataOutputStream);
            dataOutputStream.flush();
        }
    }

    private static void responseFromDELETEAndText(RequestMethod requestMethod, DataOutputStream dataOutputStream)
        throws IOException {
        String textId = requestMethod.getTextId();
        if (textId != null) {
            String getString = stringStorage.get(textId);
            if (getString != null) {
                stringStorage.remove(textId);
                ResponseStateCode.response204NoContent.response(dataOutputStream);
            } else {
                ResponseStateCode.response404NotFound.response(dataOutputStream);
            }
        } else {
            ResponseStateCode.response404NotFound.response(dataOutputStream);
        }
    }
}
