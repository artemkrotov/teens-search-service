package ru.krotov.teenssearchservice.services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallPostFull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.krotov.teenssearchservice.components.converters.WallPostFullMessageConverter;
import ru.krotov.teenssearchservice.components.filters.Filter;
import ru.krotov.teenssearchservice.configurations.properties.VkConfigurationProperties;
import ru.krotov.teenssearchservice.model.Message;
import ru.krotov.teenssearchservice.repository.MessageRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VkSearchPostService implements SearchPostService {

	@Qualifier("wallPostFullFilterExecutor")
	private final Filter<WallPostFull> wallPostFullFilterExecutor;

	//TODO: Написать анализатор который решает как частво запускать Scheduled
	private final VkApiClient vkApiClient;
	private final VkConfigurationProperties vkConfigurationProperties;
	private final WallPostFullMessageConverter wallPostFullMessageConverter;
	//TODO: Временно
	private UserActor userActor;

	@PostConstruct
	void init() {
		userActor = new UserActor(vkConfigurationProperties.getAppId(), vkConfigurationProperties.getToken());
	}

	@Override
	public List<Message> findMessages(List<String> groupDomains) {
		return groupDomains.stream()
				.map(this::findMessages)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	@Override
	public List<Message> findMessages(String groupDomain) {
		try {
			List<WallPostFull> wallPostFulls = vkApiClient.wall()
					.get(userActor)
					.count(vkConfigurationProperties.getMessageBucketSize())
					.domain(groupDomain)
					.execute().getItems();

			return wallPostFulls.stream()
					.filter(wallPostFullFilterExecutor::filter)
					.map(wallPostFullMessageConverter::convert)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (ApiException | ClientException e) {
			throw new RuntimeException(e);
		}
	}
}
