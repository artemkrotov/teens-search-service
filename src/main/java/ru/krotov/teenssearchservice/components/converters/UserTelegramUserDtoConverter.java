package ru.krotov.teenssearchservice.components.converters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramUserDto;
import ru.krotov.teenssearchservice.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserTelegramUserDtoConverter implements Converter<User, TelegramUserDto> {

	@Override
	public TelegramUserDto convert(User user) {

		TelegramUserDto telegramUserDto = new TelegramUserDto();
		telegramUserDto.setFullName(user.getFullName());
		telegramUserDto.setInstagram(user.getInstagramId());
		telegramUserDto.setVk(user.getVkDomain());
		telegramUserDto.setCity(user.getCity());
		telegramUserDto.setAgeAndTodayBirthDay(user.getBDay());
		telegramUserDto.setPhotoUrl(user.getPhotoUrl());

		return telegramUserDto;
	}


}
