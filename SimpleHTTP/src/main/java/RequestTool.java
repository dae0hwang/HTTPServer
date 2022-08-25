import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestTool {

    public static String readDate(BufferedReader br, int contentLength) throws IOException {
        char[] messageBody = new char[contentLength];
        br.read(messageBody, 0, contentLength);
        return String.copyValueOf(messageBody);
    }

    public static Map<String, String> readHeader(BufferedReader br) throws IOException {
        Map<String, String> headerInformation = new HashMap<>();
        String headerLine;
        while ((headerLine = br.readLine()) != null) {
            if (!headerLine.equals("")) {
                String key = headerLine.split(":")[0];
                String value = headerLine.split(":")[1].trim();
                headerInformation.put(key, value);
            } else {
                break;
            }
        }
        return headerInformation;
    }
}
