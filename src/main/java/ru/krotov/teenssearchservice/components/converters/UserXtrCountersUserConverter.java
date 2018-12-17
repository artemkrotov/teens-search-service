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

@Slf4j
@Component
@RequiredArgsConstructor
public class UserXtrCountersUserConverter implements Converter<UserXtrCounters, User> {

	@Qualifier("userFilterExecutor")
	private final Filter<UserXtrCounters> userFilterExecutor;

	@Override
	public User convert(UserXtrCounters userXtrCounters) {

		if (!userFilterExecutor.filter(userXtrCounters)) {
			throw new InvalidUserException("xz");
		}

		User user = new User();
		user.setFullName(userXtrCounters.getFirstName() + " " + userXtrCounters.getLastName());
		user.setInstagramId(userXtrCounters.getInstagram());
		user.setVkId(userXtrCounters.getId());
		user.setVkDomain("https://vk.com/" + userXtrCounters.getDomain());
		user.setCityIfPresent(userXtrCounters.getCity());
		user.setBDay(userXtrCounters.getBdate());
		user.setPhotoUrl(userXtrCounters.getPhotoMaxOrig());

		return user;
	}


}
