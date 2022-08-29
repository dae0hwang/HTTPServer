//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.util.Date;
//import java.util.Map;
//
//public class ResponseService {
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    public  RequestMethod readFirstLIneAndImplementRequestMethod(BufferedReader bufferedReader)
//        throws IOException {
//        RequestMethod requestMethod = new RequestMethod();
//        String firstLine = bufferedReader.readLine();
//        String[] firstLineArray = firstLine.split(" ");
//        requestMethod.setMethod(firstLineArray[0]);
//        String requestTypeAndTextId = firstLineArray[1].substring(1, firstLineArray[1].length());
//        if (requestTypeAndTextId.contains("/")) {
//            String[] requestTypeAndTextIdArray = requestTypeAndTextId.split("/");
//            requestMethod.setRequestType(requestTypeAndTextIdArray[0]);
//            requestMethod.setTextId(requestTypeAndTextIdArray[1]);
//        } else {
//            requestMethod.setRequestType(requestTypeAndTextId);
//        }
//        return requestMethod;
//    }
//
//    public byte[] responseFromGETAndTime() throws JsonProcessingException {
//        TimeResponseMessageBody timeResponseMessageBody = new TimeResponseMessageBody();
//        timeResponseMessageBody.setTime(String.valueOf(new Date()));
//        byte[] timeJsonBytes = objectMapper.writeValueAsBytes(timeResponseMessageBody);
//        return timeJsonBytes;
//    }
//
//    public byte[] responseFromGETAndImage() throws IOException {
//        String rootPath = System.getProperty("user.dir");
//        BufferedImage originalImage =
//            ImageIO.read(new File(rootPath+"\\SimpleHTTP\\src\\main\\java\\sea.jpg"));
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ImageIO.write(originalImage, "jpg", byteArrayOutputStream);
//        byteArrayOutputStream.flush();
//        byte[] imageInByte = byteArrayOutputStream.toByteArray();
//        return imageInByte;
//    }
//
//    public String responseFromGETAndText(RequestMethod requestMethod){
//        String textId = requestMethod.getTextId();
//        if (textId != null) {
//            String storageString = StringStorage.getString(textId);
//            if (storageString != null) {
//                return storageString;
//            }
//        }
//        return null;
//    }
//
//    public void responseFromPostAndText(RequestMethod requestMethod, BufferedReader bufferedReader) throws IOException {
//        String textId = requestMethod.getTextId();
//        if (textId == null) {
//            return;
//        } else {
//            Map<String, String> headerInformation = RequestTool.readHeader(bufferedReader);
//            int messageBodyLength = Integer.parseInt(headerInformation.get("Content-Length"));
//            String messageBody = RequestTool.readDate(bufferedReader, messageBodyLength);
//            StringStorage.storeString(textId, messageBody);
//        }
//    }
//
//    public static void responseFromDELETEAndText(RequestMethod requestMethod, DataOutputStream dataOutputStream) throws IOException {
//        String textId = requestMethod.getTextId();
//        if (textId != null) {
//            StringStorage.removeString(textId);
//            ResponseStateCode.response204NoContent.response(dataOutputStream);
//        } else {
//            ResponseStateCode.response404NotFound.response(dataOutputStream);
//        }
//    }
//}
