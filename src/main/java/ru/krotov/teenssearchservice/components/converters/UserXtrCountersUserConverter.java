package ru.krotov.teenssearchservice.components.converters;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.filters.UserFilterExecutor;
import ru.krotov.teenssearchservice.model.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserXtrCountersUserConverter implements Converter<UserXtrCounters, User> {

	private final UserFilterExecutor userFilterExecutor;

	@Override
	public User convert(UserXtrCounters userXtrCounters) {

		userFilterExecutor.filter(userXtrCounters);
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
