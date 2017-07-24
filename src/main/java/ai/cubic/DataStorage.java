package ai.cubic;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rai220 on 7/21/17.
 */
@Service
public class DataStorage {
    private static Random rnd = new Random();
    private Map<String, List<String>> users = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();
    private File f = new File("file.json");

    @PostConstruct
    public void init() {
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            String data = FileUtils.readFileToString(f, Charset.forName("UTF-8"));
            Map<String, List<String>> dataMap = gson.fromJson(data, Map.class);
            if (dataMap != null) {
                dataMap.entrySet().forEach(entry -> {
                    List<String> lst = new LinkedList<String>();
                    lst.addAll(entry.getValue());
                    users.put(entry.getKey(), lst);
                });
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    public synchronized String getRandomLink(String user) {
        List<String> data = users.get(user);
        if (data == null || data.size() == 0) {
            return null;
        }
        String link = data.remove(rnd.nextInt(data.size()));
        saveData();
        return link;
    }

    public synchronized int saveLink(String user, String link) {
        List<String> data;
        if (!users.containsKey(user)) {
            data = new LinkedList<>();
            users.put(user, data);
        } else {
            data = users.get(user);
        }
        data.add(link);
        saveData();
        return data.size();
    }

    private synchronized void saveData() {
        try {
            FileUtils.writeStringToFile(f, gson.toJson(users),
                    Charset.forName("UTF-8"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Set<String> getAllUsers() {
        return users.keySet();
    }
}
