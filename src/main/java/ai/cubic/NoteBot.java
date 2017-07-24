package ai.cubic;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Created by rai220 on 7/21/17.
 */
@Service
public class NoteBot extends TelegramLongPollingBot {
    @Autowired
    private DataStorage ds;

    public void sendMessage(String user, String text) {
        try {
            if (Strings.isNullOrEmpty(text)) {
                text = "You have no saved articles. Send it to me!";
            } else {
                text = "Look at this! " + text;
            }
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(user)
                    .setText(text);
            sendMessage(message);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                int count = ds.saveLink(update.getMessage().getFrom().getId().toString(),
                        update.getMessage().getText());
                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("Very interesting! You have " + count + " messages saved.");
                sendMessage(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "notereminderbot";
    }

    @Override
    public String getBotToken() {
        return <INSERT_YOUR>;
    }
}
