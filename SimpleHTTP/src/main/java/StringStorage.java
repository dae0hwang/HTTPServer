import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StringStorage {
    private static HashMap<String, String> stringStorage = new HashMap<>();

    public static void storeString(String textId, String str) {
        stringStorage.put(textId, str);
    }

    public static String getString(String textId) {
        if (stringStorage.containsKey(textId)) {
            String getString = stringStorage.get(textId);
            return getString;
        } else {
            return null;
        }
    }

    public  static void removeString(String textId) {
        stringStorage.remove(textId);
    }

    public static Map<String, String> getMap() {
        return stringStorage;
    }
}
