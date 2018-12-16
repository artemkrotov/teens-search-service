package ru.krotov.teenssearchservice.components.converters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.exceptions.InvalidUserException;
import ru.krotov.teenssearchservice.components.filters.Filter;
import ru.krotov.teenssearchservice.model.User;
import ru.krotov.teenssearchservice.utils.WomenNameIndexUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserXtrCountersUserConverter implements Converter<UserXtrCounters, User> {

	@Qualifier("userFilterExecutor")
	private final Filter<UserXtrCounters> userFilterExecutor;

	@Override
	public User convert(UserXtrCounters userXtrCounters) {

		// TODO Здесь следует бросить эксепшн и перехватить его в сервисе
		if (!WomenNameIndexUtils.isWoman(userXtrCounters.getFirstName())) {// TODO: Сделать Бин
			throw new InvalidUserException(String.format("User with id = %d and name %s wasn't women", userXtrCounters.getId(), userXtrCounters.getFirstName()));
		}

		if (!userFilterExecutor.filter(userXtrCounters)) {
			throw new InvalidUserException("xz");
		}

		User user = new User();
		user.setFullName(userXtrCounters.getFirstName() + " " + userXtrCounters.getLastName());
		user.setInstagramId(userXtrCounters.getInstagram());
		user.setVkDomain("https://vk.com/" + userXtrCounters.getDomain());
		user.setCityIfPresent(userXtrCounters.getCity());
		user.setBDay(userXtrCounters.getBdate());
		user.setPhotoUrl(userXtrCounters.getPhotoMaxOrig());

		return user;
	}


}
