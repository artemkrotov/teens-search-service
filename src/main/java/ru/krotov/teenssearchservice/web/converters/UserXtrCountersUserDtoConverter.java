package ru.krotov.teenssearchservice.web.converters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.configurations.exceptions.InvalidUserException;
import ru.krotov.teenssearchservice.configurations.filters.Filter;
import ru.krotov.teenssearchservice.core.utils.WomenNameIndexUtils;
import ru.krotov.teenssearchservice.web.dto.UserDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserXtrCountersUserDtoConverter implements Converter<UserXtrCounters, UserDto> {

	@Qualifier("userFilterExecutor")
	private final Filter<UserXtrCounters> userFilterExecutor;

	@Override
	public UserDto convert(UserXtrCounters user) {

		// TODO Здесь следует бросить эксепшн и перехватить его в сервисе
		if (!WomenNameIndexUtils.isWoman(user.getFirstName())) {// TODO: Сделать Бин
			throw new InvalidUserException(String.format("User with id = %d and name %s wasn't women", user.getId(), user.getFirstName()));
		}

		if (!userFilterExecutor.filter(user)) {
			throw new InvalidUserException("xz");
		}

		UserDto userDto = new UserDto();
		userDto.setFullName(user.getFirstName() + " " + user.getLastName());
		userDto.setInstagram(user.getInstagram());
		userDto.setVk("https://vk.com/" + user.getDomain());
		userDto.setCityIfPresent(user.getCity());
		userDto.setAgeAndTodayBirthDay(user.getBdate());
		userDto.setPhotoUrl(user.getPhotoMaxOrig());

		return userDto;
	}


}
