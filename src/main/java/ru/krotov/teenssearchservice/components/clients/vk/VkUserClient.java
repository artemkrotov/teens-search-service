package ru.krotov.teenssearchservice.components.clients.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.queries.users.UserField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.krotov.teenssearchservice.components.clients.UserClient;
import ru.krotov.teenssearchservice.configurations.properties.VkConfigurationProperties;
import ru.krotov.teenssearchservice.exceptions.UserNotFoundException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class VkUserClient implements UserClient {

	private UserActor userActor; //TODO: Временно
	private final VkApiClient vkApiClient;
	private final VkConfigurationProperties vkConfigurationProperties;

	@PostConstruct
	void init() {
		userActor = new UserActor(vkConfigurationProperties.getAppId(), vkConfigurationProperties.getToken());
	}

	@Override
	public UserXtrCounters getUser(Integer userVkId) {

		try {
			TimeUnit.MILLISECONDS.sleep(200);

			List<UserXtrCounters> userXtrCounters = vkApiClient
					.users()
					.get(userActor).userIds(String.valueOf(userVkId)).fields(Arrays.asList(UserField.values()))
					.execute();

			return userXtrCounters.stream()
					.findAny()
					.orElseThrow(() -> new UserNotFoundException(String.format("User from wallPost with id = %d wasn't founded!", userVkId)));
		} catch (Exception e) {
			throw new UserNotFoundException(String.format("User wasn't founded! Reason: %s", e.getMessage()));
		}
	}
}
