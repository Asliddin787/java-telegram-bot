package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class MyTelegramBotButton extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        String token;
        return token = "6900497467:AAESeIXuPWTV3A3NQQoEkmKmvWbVoseuUGw";
        
    }

    @Override
    public String getBotUsername() {
        return "Javokhir_parmonov_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Quyidagi tugmalardan birini tanlang:");

            // ReplyKeyboardMarkup yaratish
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();

            KeyboardRow row1 = new KeyboardRow();
            row1.add("Tugma 1");
            row1.add("Tugma 2");

            KeyboardRow row2 = new KeyboardRow();
            row2.add("Tugma 3");
            row2.add("Tugma 4");

            keyboard.add(row1);
            keyboard.add(row2);

            keyboardMarkup.setKeyboard(keyboard);

            message.setReplyMarkup(keyboardMarkup);

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

}
