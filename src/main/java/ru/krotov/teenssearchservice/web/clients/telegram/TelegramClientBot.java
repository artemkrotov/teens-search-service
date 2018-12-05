package ru.krotov.teenssearchservice.web.clients.telegram;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.krotov.teenssearchservice.web.dto.MessageDto;
import ru.krotov.teenssearchservice.web.dto.UserDto;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TelegramClientBot extends TelegramLongPollingBot {

	public TelegramClientBot(DefaultBotOptions options) {
		super(options);
	}

	@Override
	public void onUpdateReceived(Update update) {
		Message message = update.getMessage();
		MessageDto messageDto = new MessageDto();
		messageDto.setMessage(message.getText());
		sendMsg(message.getChatId().toString(), messageDto);
	}

	//TODO Максимальная длина сообщения 4096, возможно возникновение проблем если символов будет больше.
	//Раньше был метод splitByLength, который резал сообщения по 4096 символов (можно посмотреть в истории гита)
	public synchronized void sendMsg(String chatId, List<MessageDto> messageDtos) {
		messageDtos.forEach(messageDto -> sendMsg(chatId, messageDto));
	}

	// TODO Зачем synchronized?
	private synchronized void sendMsg(String chatId, MessageDto messageDto) {

		SendPhoto sendPhoto = makePhoto(messageDto, chatId);

		try {
			execute(sendPhoto);
			TimeUnit.SECONDS.sleep(1);
		} catch (TelegramApiException | InterruptedException e) {
			log.error("Exception: {}", e.toString());
		}
	}

	private SendPhoto makePhoto(MessageDto messageDto, String chatId) {
		SendPhoto sendPhoto = new SendPhoto();

		sendPhoto.setCaption(makeCaption(messageDto));
		sendPhoto.setPhoto(messageDto.getUserDto().getPhotoUrl());
		sendPhoto.setChatId(chatId);
		sendPhoto.setParseMode(ParseMode.MARKDOWN);
		return sendPhoto;
	}

	private String makeCaption(MessageDto messageDto) {

		StringBuilder sb = new StringBuilder();

		UserDto userDto = messageDto.getUserDto();
		sb.append("[").append(userDto.getFullName()).append("](").append(userDto.getVk()).append(")");

		boolean hasCity = userDto.getCity() != null;
		boolean hasAge = userDto.getAge() != null;
		if (hasCity || hasAge) {

			sb.append(" (");

			if (hasAge) {
				sb.append(userDto.getAge()).append(" лет"); // TODO: Написать обработчик
			}

			if (hasCity && hasAge) {
				sb.append(", ");
			}

			if (hasCity) {
				sb.append(userDto.getCity());
			}

			sb.append(")");
		}


		if (userDto.isTodayBirthDay()) {
			sb.append(" ").append("\uD83C\uDF82");
		}

		sb.append("\n\n").append(messageDto.getMessage());

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
