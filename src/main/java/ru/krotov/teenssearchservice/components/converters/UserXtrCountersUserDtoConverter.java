package ru.krotov.teenssearchservice.components.converters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.exceptions.InvalidUserException;
import ru.krotov.teenssearchservice.components.filters.Filter;
import ru.krotov.teenssearchservice.utils.WomenNameIndexUtils;
import ru.krotov.teenssearchservice.components.clients.telegram.dto.TelegramUserDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserXtrCountersUserDtoConverter implements Converter<UserXtrCounters, TelegramUserDto> {

	@Qualifier("userFilterExecutor")
	private final Filter<UserXtrCounters> userFilterExecutor;

	@Override
	public TelegramUserDto convert(UserXtrCounters user) {

		// TODO Здесь следует бросить эксепшн и перехватить его в сервисе
		if (!WomenNameIndexUtils.isWoman(user.getFirstName())) {// TODO: Сделать Бин
			throw new InvalidUserException(String.format("User with id = %d and name %s wasn't women", user.getId(), user.getFirstName()));
		}

		if (!userFilterExecutor.filter(user)) {
			throw new InvalidUserException("xz");
		}

		TelegramUserDto telegramUserDto = new TelegramUserDto();
		telegramUserDto.setFullName(user.getFirstName() + " " + user.getLastName());
		telegramUserDto.setInstagram(user.getInstagram());
		telegramUserDto.setVk("https://vk.com/" + user.getDomain());
		telegramUserDto.setCityIfPresent(user.getCity());
		telegramUserDto.setAgeAndTodayBirthDay(user.getBdate());
		telegramUserDto.setPhotoUrl(user.getPhotoMaxOrig());

		return telegramUserDto;
	}


}
