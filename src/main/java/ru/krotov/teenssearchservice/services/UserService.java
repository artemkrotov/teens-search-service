package ru.krotov.teenssearchservice.services;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krotov.teenssearchservice.components.clients.UserClient;
import ru.krotov.teenssearchservice.components.converters.UserXtrCountersUserConverter;
import ru.krotov.teenssearchservice.exceptions.InvalidUserException;
import ru.krotov.teenssearchservice.model.User;
import ru.krotov.teenssearchservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserXtrCountersUserConverter userXtrCountersUserConverter;
	private final UserClient userClient;
	private final UserRepository userRepository;

	public User getUser(Integer userVkId) {

		User user = userRepository.findByVkId(userVkId);

		if (user == null) {
			UserXtrCounters userXtrCounters = userClient.getUser(userVkId);
			user = userXtrCountersUserConverter.convert(userXtrCounters);
			if (user == null) {
				throw new InvalidUserException("User hadn't be null!");
			}
		}

		return user;
	}
}
