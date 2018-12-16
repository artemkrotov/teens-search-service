package ru.krotov.teenssearchservice.components.converters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.clients.UserClient;
import ru.krotov.teenssearchservice.exceptions.InvalidUserException;
import ru.krotov.teenssearchservice.exceptions.UserNotFoundException;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramMessageDto;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramUserDto;
import ru.krotov.teenssearchservice.model.Message;
import ru.krotov.teenssearchservice.model.User;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageTelegramMessageDtoConverter implements Converter<Message, TelegramMessageDto> {

	private final UserTelegramUserDtoConverter userTelegramUserDtoConverter;

	@Override
	public TelegramMessageDto convert(Message message) {

		try {
			User user = message.getUser();
			TelegramUserDto telegramUserDto = userTelegramUserDtoConverter.convert(user);

			TelegramMessageDto telegramMessageDto = new TelegramMessageDto();
			telegramMessageDto.setTelegramUserDto(telegramUserDto);
			telegramMessageDto.setMessage(message.getText());
			return telegramMessageDto;
		} catch (UserNotFoundException e) {
			log.error(e.getMessage());
			return null;
		} catch (InvalidUserException e) {
			return null;
		}
	}


}
