import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private Socket connection;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private ResponseService responseService = new ResponseService();

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            RequestMethod requestMethod = responseService.readFirstLIneAndImplementRequestMethod(bufferedReader);
            System.out.println(requestMethod.toString());

            switch (requestMethod.getMethod()) {
                case "GET":
                    switch (requestMethod.getRequestType()) {
                        case "time":
                            byte[] timeJsonBytes = responseService.responseFromGETAndTime();
                            responseService.sendResponseFromGETAndTime(dataOutputStream, timeJsonBytes);
                            break;
                        case "image":
                            byte[] imageInByte = responseService.responseFromGETAndImage();
                            responseService.sendResponseFromGETAndImage(dataOutputStream, imageInByte);
                            break;
                        case "text":
                            String storageString = responseService.responseFromGETAndText(requestMethod);
                            responseService.sendResponseFromGETAndText(dataOutputStream, requestMethod, storageString);
                            break;
                    }
                    break;
                case "POST":
                    switch (requestMethod.getRequestType()) {
                        case "text":
                            responseService.responseFromPostAndText(requestMethod, bufferedReader);
                            responseService.sendResponseFromPostAndText(requestMethod, dataOutputStream);
                            break;
                    }
                    break;
                case "DELETE":
                    switch (requestMethod.getRequestType()) {
                        case "text":
                            responseService.responseFromDELETEAndText(requestMethod);
                            responseService.sendResponseFromDELETEAndText(requestMethod, dataOutputStream);
                            break;
                    }
            }
            System.out.println("checking StringStorage: " + responseService.stringStorage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
