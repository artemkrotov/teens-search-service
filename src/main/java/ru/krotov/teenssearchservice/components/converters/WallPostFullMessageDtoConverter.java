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


@Slf4j
@Component
@RequiredArgsConstructor
public class WallPostFullMessageDtoConverter implements Converter<WallPostFull, TelegramMessageDto> {

	private final UserXtrCountersUserDtoConverter userDtoConverter;
	private final UserClient userClient;

	@Override
	public TelegramMessageDto convert(WallPostFull wallPostFull) {

		try {
			UserXtrCounters realUser = userClient.getUser(wallPostFull);
			TelegramUserDto telegramUserDto = userDtoConverter.convert(realUser);

			TelegramMessageDto telegramMessageDto = new TelegramMessageDto();
			telegramMessageDto.setTelegramUserDto(telegramUserDto);
			telegramMessageDto.setMessage(wallPostFull.getText());
			return telegramMessageDto;
		} catch (UserNotFoundException e) {
			log.error(e.getMessage());
			return null;
		} catch (InvalidUserException e) {
			return null;
		}
	}


}
