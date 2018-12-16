package ru.krotov.teenssearchservice.components.clients.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.krotov.teenssearchservice.configurations.properties.DefaultBotOptionsConfigurationProperties;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramMessageDto;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramUserDto;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class TelegramClientBot extends TelegramLongPollingBot {

	private static final String CHAT_ID =  "281490960"; // "-262297048";
	private final DefaultBotOptionsConfigurationProperties defaultBotOptionsConfigurationProperties;

	public TelegramClientBot(DefaultBotOptions options, DefaultBotOptionsConfigurationProperties defaultBotOptionsConfigurationProperties) {
		super(options);
		this.defaultBotOptionsConfigurationProperties = defaultBotOptionsConfigurationProperties;
	}

	private List<TelegramMessageDto> oldUnSendedMessages = new LinkedList<>();

	@Override
	public void onUpdateReceived(Update update) {

		SendMessage sendMessage = new SendMessage();
		Message message = update.getMessage();
		sendMessage.setText(String.format("%s: %s", message.getChatId(), message.getText()));
		sendMessage.setChatId(message.getChatId());
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			log.error(e.getMessage());
		}
	}

	//TODO Максимальная длина сообщения 4096, возможно возникновение проблем если символов будет больше.
	//Раньше был метод splitByLength, который резал сообщения по 4096 символов (можно посмотреть в истории гита)
	public synchronized void sendMsg(String chatId, List<TelegramMessageDto> telegramMessageDtos) {
		telegramMessageDtos.forEach(messageDto -> sendMsg(chatId, messageDto));
	}

	public synchronized void sendMsg(List<TelegramMessageDto> telegramMessageDtos) {
		telegramMessageDtos.forEach(messageDto -> sendMsg(CHAT_ID, messageDto));
		resendOldMessages();
	}

	private void resendOldMessages() {
		log.info("Try to send oldUnSendedMessages");
		List<TelegramMessageDto> oldUnSendedMessages = this.oldUnSendedMessages;
		this.oldUnSendedMessages = new LinkedList<>();
		oldUnSendedMessages.forEach(messageDto -> sendMsg(CHAT_ID, messageDto));
	}

	// TODO Зачем synchronized?
	private synchronized void sendMsg(String chatId, TelegramMessageDto telegramMessageDto) {

		SendPhoto sendPhoto = makePhoto(telegramMessageDto, chatId);

		try {
			execute(sendPhoto);
			TimeUnit.SECONDS.sleep(1);
		} catch (TelegramApiException | InterruptedException e) {
			log.error("Exception: {} \n Dto: {}", e.toString(), sendPhoto);
			oldUnSendedMessages.add(telegramMessageDto);
		}
	}

	private SendPhoto makePhoto(TelegramMessageDto telegramMessageDto, String chatId) {
		SendPhoto sendPhoto = new SendPhoto();

		sendPhoto.setCaption(makeCaption(telegramMessageDto));
		sendPhoto.setPhoto(telegramMessageDto.getTelegramUserDto().getPhotoUrl());
		sendPhoto.setChatId(chatId);
		sendPhoto.setParseMode(ParseMode.MARKDOWN);
		return sendPhoto;
	}

	private String makeCaption(TelegramMessageDto telegramMessageDto) {

		StringBuilder sb = new StringBuilder();

		TelegramUserDto telegramUserDto = telegramMessageDto.getTelegramUserDto();
		sb.append("[").append(telegramUserDto.getFullName()).append("](").append(telegramUserDto.getVk()).append(")");

		boolean hasCity = telegramUserDto.getCity() != null;
		boolean hasAge = telegramUserDto.getAge() != null;
		if (hasCity || hasAge) {

			sb.append(" (");

			if (hasAge) {
				sb.append(telegramUserDto.getAge()).append(" лет"); // TODO: Написать обработчик
			}

			if (hasCity && hasAge) {
				sb.append(", ");
			}

			if (hasCity) {
				sb.append(telegramUserDto.getCity());
			}

			sb.append(")");
		}


		if (telegramUserDto.isTodayBirthDay()) {
			sb.append(" ").append("\uD83C\uDF82");
		}

		sb.append("\n\n").append(telegramMessageDto.getMessage());

		return sb.toString();
	}

	@Override
	public String getBotUsername() {
		return defaultBotOptionsConfigurationProperties.getName();
	}

	@Override
	public String getBotToken() {
		return defaultBotOptionsConfigurationProperties.getToken();
	}
}
