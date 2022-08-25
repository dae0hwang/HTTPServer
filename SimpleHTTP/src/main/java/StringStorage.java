import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class StringStorage {
    private static HashMap<String, String> stringStorage = new HashMap<>();
    private static ReentrantLock lockForStringStorage = new ReentrantLock();

    public static void storeString(String textId, String str) {
        lockForStringStorage.lock();
        try {
            stringStorage.put(textId, str);
        } finally {
            lockForStringStorage.unlock();
        }
    }

    public static String getString(String textId) {
        lockForStringStorage.lock();
        try {
            if (stringStorage.containsKey(textId)) {
                String getString = stringStorage.get(textId);
                return getString;
            } else {
                return null;
            }
        }finally {
            lockForStringStorage.unlock();
        }
    }

    public  static void removeString(String textId) {
        lockForStringStorage.lock();
        try {
            stringStorage.remove(textId);
        }finally {
            lockForStringStorage.unlock();
        }
    }

    public static Map<String, String> getMap() {
        return stringStorage;
    }
}
