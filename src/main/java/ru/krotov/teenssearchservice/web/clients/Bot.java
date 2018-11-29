package ru.krotov.teenssearchservice.web.clients;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class Bot extends TelegramLongPollingBot {


	public Bot(DefaultBotOptions options) {
		super(options);
	}

	@Override
	public void onUpdateReceived(Update update) {
		Message message = update.getMessage();
		sendMsg(message.getChatId().toString(), message.getText());

	}

	//TODO Максимальная длина сообщения 4096, возможно возникновение проблем если символов будет больше.
	//Раньше был метод spltitByLength, который резал сообщения по 4096 символов (можно посмотреть в истории гита)
	public synchronized void sendMsg(String chatId, String s) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown(true);
		sendMessage.setChatId(chatId);
		sendMessage.setText(s + "  " + chatId);
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			log.error("Exception: {}", e.toString());
		}
	}

	@Override
	public String getBotUsername() {
		return "AOEManager";
	}

	@Override
	public String getBotToken() {
		return "764444874:AAEiuZ5mNrz5jymxZFPGFgsogcJkoaAe-9o";
	}
}
