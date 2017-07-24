package ai.cubic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

/**
 * Created by rai220 on 7/21/17.
 */
@Service
public class TelegramService {

    @Autowired
    private NoteBot noteBot;

    static {
        ApiContextInitializer.init();
    }

    @PostConstruct
    public void init() {
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(noteBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
