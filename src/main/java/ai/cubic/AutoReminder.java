package ai.cubic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * Created by rai220 on 7/21/17.
 */
@Service
public class AutoReminder {
    private static final Random rnd = new Random();

    @Autowired
    private DataStorage ds;
    @Autowired
    private NoteBot bot;

    @PostConstruct
    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(60 * 60 * 1000 * (6 + rnd.nextInt(6)));
                        ds.getAllUsers().forEach(user -> {
                            String link = ds.getRandomLink(user);
                            bot.sendMessage(user, link);
                        });

                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
