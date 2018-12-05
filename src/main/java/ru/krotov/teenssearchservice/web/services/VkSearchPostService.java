package ru.krotov.teenssearchservice.web.services;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallPostFull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.krotov.teenssearchservice.core.collections.CircleLinkedListWithLimit;
import ru.krotov.teenssearchservice.configurations.properties.VkConfigurationProperties;
import ru.krotov.teenssearchservice.web.converters.WallPostFullMessageDtoConverter;
import ru.krotov.teenssearchservice.web.dto.MessageDto;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VkSearchPostService implements SearchPostService {

	//TODO: Написать анализатор который решает как частво запускать Scheduled
	private final VkApiClient vkApiClient;
	private final VkConfigurationProperties vkConfigurationProperties;
	private final WallPostFullMessageDtoConverter wallPostFullMessageDtoConverter;
	//TODO: Временно
	private UserActor userActor;
	private CircleLinkedListWithLimit<Integer> circleLinkedListWithLimit = new CircleLinkedListWithLimit<>(1000); // TODO: Подумать как посчитать оптимальный размер

	@PostConstruct
	void init() {
		userActor = new UserActor(vkConfigurationProperties.getAppId(), vkConfigurationProperties.getToken());
	}

	@Override
	public List<MessageDto> findMessages(List<String> groupDomains) {
		return groupDomains.stream()
				.map(this::findMessages)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	@Override
	public List<MessageDto> findMessages(String groupDomain) {
		try {

			// TODO: Между запросами должна быть задержка, иначе API VK даст микробан
			TimeUnit.SECONDS.sleep(1);

			List<WallPostFull> wallPostFulls = vkApiClient.wall().get(userActor).count(6).domain(groupDomain).execute().getItems();
			return wallPostFulls.stream()
					.filter(message -> {
						Integer id = message.getId();

						if (circleLinkedListWithLimit.contains(id)) {
							return false;
						}

						circleLinkedListWithLimit.add(id);
						return true;

					})
					.map(wallPostFullMessageDtoConverter::convert)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (ApiException | ClientException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
