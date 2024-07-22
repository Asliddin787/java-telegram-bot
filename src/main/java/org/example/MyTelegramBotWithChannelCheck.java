package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class MyTelegramBotWithChannelCheck extends TelegramLongPollingBot {

    private final String[] channels = {"Yashirin_guruh_java"};
    private final String confirmationCallbackData = "check_membership";

    @Override
    public String getBotUsername() {
        return "Javokhir_parmonov_bot"; // Bot foydalanuvchi nomini kiriting
    }

    @Override
    public String getBotToken() {
        return "6900497467:AAESeIXuPWTV3A3NQQoEkmKmvWbVoseuUGw"; // Bot tokenini kiriting
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            showSubscriptionButtons(chatId, channels);

        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(confirmationCallbackData)) {
                checkMembership(chatId);
            }
        }
    }

    private void showSubscriptionButtons(long chatId, String[] channels) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Quyidagi kanallarga obuna bo'ling va tasdiqlash tugmasini bosing:");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String channel : channels) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(channel);
            button.setUrl("https://t.me/" + channel.replace("@", ""));
            row.add(button);
            keyboard.add(row);
        }

        // Tasdiqlash tugmasi
        List<InlineKeyboardButton> confirmRow = new ArrayList<>();
        InlineKeyboardButton confirmButton = new InlineKeyboardButton();
        confirmButton.setText("Tasdiqlash");
        confirmButton.setCallbackData(confirmationCallbackData);
        confirmRow.add(confirmButton);
        keyboard.add(confirmRow);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void checkMembership(long chatId) {
        List<String> notSubscribedChannels = new ArrayList<>();

        for (String channel : channels) {
            if (!isUserMember(chatId, channel)) {
                notSubscribedChannels.add(channel);
            }
        }

        if (notSubscribedChannels.isEmpty()) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Siz barcha kanallarga a'zo bo'ldingiz!");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            String[] channelsArray = notSubscribedChannels.toArray(new String[0]);
            showSubscriptionButtons(chatId, channelsArray);
        }
    }

    private boolean isUserMember(long userId, String channelUsername) {
        try {
            GetChatMember getChatMember = new GetChatMember();
            getChatMember.setChatId(channelUsername);
            getChatMember.setUserId(userId);
            ChatMember chatMember = execute(getChatMember);
            String status = chatMember.getStatus();
//            System.out.println("User status in channel " + channelUsername + ": " + status);
            return !status.equals("left") && !status.equals("kicked");
        } catch (TelegramApiException e) {
            e.printStackTrace();272
            return false;
        }
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(new MyTelegramBotWithChannelCheck());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
