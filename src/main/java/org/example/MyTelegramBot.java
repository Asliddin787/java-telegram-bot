package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class MyTelegramBot extends TelegramLongPollingBot {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public String getBotToken() {
        String token = "6900497467:AAESeIXuPWTV3A3NQQoEkmKmvWbVoseuUGw";
        return token;
    }

    @Override
    public String getBotUsername() {
        return "Javokhir_parmonov_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            message.setChatId(chatId);

            if (messageText.startsWith("/start")) {
                message.setText("Assalomu alaykum! Iltimos, ismingizni kiriting.");
                users.put(chatId, new User());
                users.get(chatId).setStep(1);
            } else if (users.containsKey(chatId)) {
                User user = users.get(chatId);
                if (user.getStep() == 1) {
                    user.setFirstName(messageText);
                    message.setText("Familiyangizni kiriting.");
                    user.setStep(2);
                } else if (user.getStep() == 2) {
                    user.setLastName(messageText);
                    message.setText("Ro'yxatdan o'tish muvaffaqiyatli yakunlandi, " + user.getFirstName() + " " + user.getLastName());
                    user.setStep(0);
                }
            } else {
                message.setText("Noma'lum komanda. Iltimos, /start komandasini yuboring.");
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
